"""Shared fetching and parsing logic for justjoin.it scrapers.

justjoin.it is a Next.js (App Router) site: listing and offer pages embed their
data in the React Server Components payload pushed by
<script>self.__next_f.push([1,"..."])</script>. We read that payload over plain
HTTP (no browser) and decode the JSON objects we need from it:

- listing page: the "offers" array (50 per page, with skills and salaries) and
  the total "count" of the search,
- offer page: the full offer object; its HTML description is a separate text
  row referenced as "$<id>".

Used by scrape_justjoin.py (bulk) and scrape_justjoin_offer.py (single).
"""
import html
import json
import logging
import re
import sys
import time
from datetime import datetime, timezone
from html.parser import HTMLParser
from urllib.parse import parse_qsl, urlencode, urlsplit, urlunsplit

import requests

SOURCE = "JustJoin"
BASE_URL = "https://justjoin.it"
OFFER_PATH_PREFIX = "/job-offer/"
ALLOWED_HOSTS = {"justjoin.it", "www.justjoin.it"}

DEFAULT_USER_AGENT = (
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
    "(KHTML, like Gecko) Chrome/140.0 Safari/537.36 OfferBrowserPrototype/1.0"
)
CONNECT_TIMEOUT = 10
READ_TIMEOUT = 30
MAX_ATTEMPTS = 3
BACKOFF_SECONDS = (3, 8, 20)
MAX_RETRY_AFTER = 60

FLIGHT_PUSH_RE = re.compile(r'self\.__next_f\.push\(\[1,"((?:[^"\\]|\\.)*)"\]\)')
LD_JSON_RE = re.compile(r'<script type="application/ld\+json"[^>]*>(.*?)</script>', re.S)
# optional locale prefix (/pl/job-offer/...) redirects to the same offer
OFFER_URL_RE = re.compile(r"^(?:/[a-z]{2})?/job-offer/([a-z0-9][a-z0-9-]*)$", re.I)
COUNT_RE = re.compile(r'"count":(\d+),"label"')
TEXT_REF_RE = re.compile(r"^\$([0-9a-f]+)$")

# Exit codes shared by both scripts (Java maps them to readable errors).
EXIT_OK = 0
EXIT_UNEXPECTED = 1
EXIT_USAGE = 2
EXIT_BLOCKED = 3
EXIT_NOT_FOUND = 4
EXIT_STRUCTURE = 5
EXIT_NETWORK = 6

LEVEL_LABELS = {
    "intern": "Trainee",
    "trainee": "Trainee",
    "junior": "Junior",
    "mid": "Mid",
    "senior": "Senior",
    "expert": "Expert",
    "c_level": "Expert",
}
UNKNOWN_LEVEL = "Nieokreślony poziom"
WANTED_LEVELS = {"intern", "trainee", "junior", "mid"}

PREFERRED_CITIES = ["warszawa", "łódź"]
# English / ASCII spellings used by some employers → Polish names (city statistics group by them)
CITY_ALIASES = {
    "warsaw": "Warszawa", "warszawa": "Warszawa",
    "lodz": "Łódź", "łódź": "Łódź",
    "cracow": "Kraków", "krakow": "Kraków", "kraków": "Kraków",
    "wroclaw": "Wrocław", "wrocław": "Wrocław",
    "gdansk": "Gdańsk", "gdańsk": "Gdańsk",
    "poznan": "Poznań", "poznań": "Poznań",
}

WORK_MODE_LABELS = {
    "remote": "praca zdalna",
    "hybrid": "praca hybrydowa",
    "office": "praca stacjonarna",
}

CONTRACT_LABELS = {
    "b2b": "B2B",
    "permanent": "UoP",
    "mandate_contract": "umowa zlecenie",
    "contract": "umowa o dzieło",
    "internship": "staż",
}
UNIT_LABELS = {"month": "mies.", "hour": "godz.", "day": "dzień", "year": "rok"}

log = logging.getLogger("justjoin")


class ScraperError(Exception):
    exit_code = EXIT_UNEXPECTED


class BlockedError(ScraperError):
    """Anti-bot challenge or access denied. Never retried aggressively."""
    exit_code = EXIT_BLOCKED


class NotFoundError(ScraperError):
    """Offer removed, expired or never existed."""
    exit_code = EXIT_NOT_FOUND


class StructureError(ScraperError):
    """Page no longer has the data layout this parser understands."""
    exit_code = EXIT_STRUCTURE


class NetworkError(ScraperError):
    exit_code = EXIT_NETWORK


def setup_logging():
    """Logs go to stderr; stdout is reserved for the JSON result."""
    for stream in (sys.stdout, sys.stderr):
        try:
            stream.reconfigure(encoding="utf-8")
        except (AttributeError, ValueError):
            pass
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s - %(levelname)s - %(message)s",
        stream=sys.stderr,
    )


def now_utc():
    return datetime.now(timezone.utc)


def format_fetched_at(dt, digits):
    """Bulk importer (Offer entity) expects 6 fraction digits, single-offer
    import (OfferDTO) expects 3 — mirrors the other portals' scripts."""
    frac = dt.strftime("%f")[:digits]
    return dt.strftime("%Y-%m-%dT%H:%M:%S.") + frac + "Z"


# ── URLs ──────────────────────────────────────────────────────────────────────

def offer_url(slug):
    return BASE_URL + OFFER_PATH_PREFIX + slug.lower()


def slug_from_offer_url(url):
    """Slug of an offer URL from justjoin.it, or None for anything else."""
    if not isinstance(url, str) or not url.strip():
        return None
    parts = urlsplit(url.strip())
    if (parts.hostname or "").lower() not in ALLOWED_HOSTS:
        return None
    m = OFFER_URL_RE.match(parts.path.rstrip("/"))
    return m.group(1).lower() if m else None


def normalize_offer_url(url):
    """Validate a user-supplied offer URL and return its canonical form
    (https, no www, no locale prefix, no query/fragment). Raises ValueError otherwise."""
    if not isinstance(url, str) or not url.strip():
        raise ValueError("Pusty URL")
    parts = urlsplit(url.strip())
    if parts.scheme not in ("http", "https"):
        raise ValueError(f"Nieobsługiwany schemat URL: {parts.scheme!r}")
    host = (parts.hostname or "").lower()
    if host not in ALLOWED_HOSTS:
        raise ValueError(f"Domena spoza justjoin.it: {host!r}")
    slug = slug_from_offer_url(url)
    if not slug:
        raise ValueError("To nie jest adres ogłoszenia justjoin.it (/job-offer/<nazwa>)")
    return offer_url(slug)


def validate_search_url(url):
    """A listing URL entered by the admin: https on justjoin.it. Returns it trimmed."""
    if not isinstance(url, str) or not url.strip():
        raise ValueError("Pusty URL wyszukiwania")
    parts = urlsplit(url.strip())
    if parts.scheme not in ("http", "https") or (parts.hostname or "").lower() not in ALLOWED_HOSTS:
        raise ValueError(f"Adres wyszukiwania spoza justjoin.it: {url!r}")
    return url.strip()


def page_url(search_url, page):
    """The search URL with ?page=N (any page parameter already there is replaced)."""
    parts = urlsplit(search_url)
    query = [(k, v) for k, v in parse_qsl(parts.query, keep_blank_values=True) if k != "page"]
    if page > 1:
        query.append(("page", str(page)))
    return urlunsplit((parts.scheme, parts.netloc, parts.path, urlencode(query, safe=","), ""))


# ── HTTP ──────────────────────────────────────────────────────────────────────

def create_session(user_agent=DEFAULT_USER_AGENT):
    session = requests.Session()
    session.headers.update({
        "User-Agent": user_agent,
        "Accept": "text/html,application/xhtml+xml",
        "Accept-Language": "pl-PL,pl;q=0.9,en;q=0.5",
    })
    return session


def looks_like_challenge(status, text):
    t = text[:20000]
    return (
        "challenges.cloudflare.com" in t
        or "<title>Just a moment" in t
        or "Cierpliwości" in t
        or (status in (403, 429) and "cf-" in t.lower())
    )


def fetch_html(session, url, sleep=time.sleep):
    """GET with bounded retries for transient failures only."""
    last_error = None
    for attempt in range(MAX_ATTEMPTS):
        try:
            resp = session.get(url, timeout=(CONNECT_TIMEOUT, READ_TIMEOUT))
        except (requests.ConnectionError, requests.Timeout) as e:
            last_error = NetworkError(f"Błąd sieci dla {url}: {e}")
            log.warning("Próba %d/%d nieudana: %s", attempt + 1, MAX_ATTEMPTS, e)
        else:
            if resp.headers.get("cf-mitigated") or looks_like_challenge(resp.status_code, resp.text):
                raise BlockedError(f"Ochrona antybotowa (HTTP {resp.status_code}) dla {url} — przerywam")
            if resp.status_code in (404, 410):
                raise NotFoundError(f"HTTP {resp.status_code} dla {url}")
            if resp.status_code == 403:
                raise BlockedError(f"Brak dostępu (HTTP 403) dla {url}")
            if resp.status_code == 429 or resp.status_code >= 500:
                last_error = NetworkError(f"HTTP {resp.status_code} dla {url}")
                retry_after = resp.headers.get("Retry-After")
                if retry_after and retry_after.isdigit():
                    delay = min(int(retry_after), MAX_RETRY_AFTER)
                    log.warning("HTTP %d, serwer prosi o odczekanie %ds", resp.status_code, delay)
                    if attempt + 1 < MAX_ATTEMPTS:
                        sleep(delay)
                        continue
                log.warning("Próba %d/%d: HTTP %d", attempt + 1, MAX_ATTEMPTS, resp.status_code)
            elif resp.status_code != 200:
                raise StructureError(f"Nieoczekiwany HTTP {resp.status_code} dla {url}")
            else:
                resp.encoding = "utf-8"
                return resp.text
        if attempt + 1 < MAX_ATTEMPTS:
            sleep(BACKOFF_SECONDS[attempt])
    raise last_error


# ── Page payload ──────────────────────────────────────────────────────────────

_DECODER = json.JSONDecoder()


class Page:
    """Decoded server-components payload of one page plus its JSON-LD blocks."""

    def __init__(self, flight, ld_json):
        self.flight = flight
        self.ld_json = ld_json

    def decode_at(self, index):
        return _DECODER.raw_decode(self.flight, index)[0]

    def text_row(self, ref):
        """Resolve a "$<id>" reference to a text row ("<id>:T<hex byte length>,<text>")."""
        m = TEXT_REF_RE.match(ref or "")
        if not m:
            return None
        row = re.search(r"(?<![0-9a-zA-Z])" + m.group(1) + r":T([0-9a-f]+),", self.flight)
        if not row:
            return None
        length = int(row.group(1), 16)
        rest = self.flight[row.end():row.end() + length].encode("utf-8")
        return rest[:length].decode("utf-8", "ignore")


def parse_page(html_text):
    chunks = FLIGHT_PUSH_RE.findall(html_text)
    if not chunks:
        if looks_like_challenge(200, html_text):
            raise BlockedError("Strona wyzwania antybotowego zamiast treści")
        raise StructureError("Brak danych strony (self.__next_f) — zmieniła się struktura serwisu")
    try:
        flight = "".join(json.loads('"' + c + '"') for c in chunks)
    except json.JSONDecodeError as e:
        raise StructureError(f"Niepoprawne dane strony: {e}")
    ld = []
    for block in LD_JSON_RE.findall(html_text):
        try:
            ld.append(json.loads(block))
        except json.JSONDecodeError:
            continue
    return Page(flight, ld)


# ── Listing ───────────────────────────────────────────────────────────────────

def _value(field):
    """Some fields are plain strings on listings and {"label", "value"} on offer pages."""
    if isinstance(field, dict):
        return field.get("value") or field.get("label")
    return field


def _skill_names(skills):
    names = []
    for s in skills or []:
        name = s.get("name") if isinstance(s, dict) else s
        if isinstance(name, str) and name.strip():
            names.append(name.strip())
    return names


def parse_listing(page):
    """Return (offers, total_count) from a listing page.

    offers: [{slug, title, company, level, slugs (all location variants), cities}]
    total_count: number of offers matching the search, or None when not shown.
    A page past the last one has no "offers" array — it returns ([], count).
    """
    count_match = COUNT_RE.search(page.flight)
    total = int(count_match.group(1)) if count_match else None
    raw = None
    for m in re.finditer(r'"offers":\[', page.flight):
        candidate = page.decode_at(m.end() - 1)
        if isinstance(candidate, list) and (not candidate or isinstance(candidate[0], dict)
                                            and "slug" in candidate[0]):
            raw = candidate
            break
    if raw is None:
        if total is not None:
            return [], total
        raise StructureError("Brak listy ofert na stronie wyszukiwania")
    offers = []
    for o in raw:
        if not isinstance(o, dict) or not o.get("slug"):
            continue
        locations = [m for m in o.get("multilocation") or [] if isinstance(m, dict) and m.get("slug")]
        slugs = [o["slug"].lower()] + [m["slug"].lower() for m in locations if m["slug"].lower() != o["slug"].lower()]
        offers.append({
            "slug": o["slug"].lower(),
            "title": o.get("title"),
            "company": o.get("companyName"),
            "level": (_value(o.get("experienceLevel")) or "").lower(),
            "slugs": slugs,
            "cities": [m.get("city") for m in locations if m.get("city")] or [o.get("city")],
        })
    return offers, total


def is_wanted_level(level):
    """Keep offers with no level info or a non-senior level."""
    return not level or level in WANTED_LEVELS


# ── Offer ─────────────────────────────────────────────────────────────────────

def normalize_city(name):
    """'Warszawa (Mazowieckie)' / 'Warsaw' → 'Warszawa'."""
    if not isinstance(name, str):
        return ""
    city = re.sub(r"\s*\(.*?\)\s*$", "", name).strip()
    alias = CITY_ALIASES.get(city.lower())
    if alias:
        return alias
    return city[:1].upper() + city[1:] if city.islower() else city


def choose_canonical_slug(locations, current_slug):
    """One stable URL per offer group (the same posting in many cities), so
    repeated runs — and a user pasting any city variant — hit the same row.
    Warszawa first, then Łódź, then the alphabetically first variant."""
    valid = [m for m in locations or [] if isinstance(m, dict) and m.get("slug")]
    if not valid:
        return current_slug.lower()
    for preferred in PREFERRED_CITIES:
        for m in valid:
            if normalize_city(m.get("city")).lower() == preferred:
                return m["slug"].lower()
    return min(m["slug"].lower() for m in valid)


def map_level(level):
    level = (level or "").lower()
    if not level:
        return UNKNOWN_LEVEL
    return LEVEL_LABELS.get(level, UNKNOWN_LEVEL)


def _amount(value):
    if value is None:
        return None
    if isinstance(value, float) and not value.is_integer():
        return f"{value:,.2f}".replace(",", " ")
    return f"{int(value):,}".replace(",", " ")


def format_salary(employment_types):
    """'12 000 – 18 000 PLN brutto/mies. (UoP) | 160 – 190 PLN netto (+VAT)/godz. (B2B)'.
    Only the currency the employer entered (not justjoin.it's conversions); rates are never converted."""
    entries = [e for e in employment_types or [] if isinstance(e, dict)]
    if any("currencySource" in e for e in entries):
        entries = [e for e in entries if e.get("currencySource") == "original"]
    parts, seen = [], set()
    for e in entries:
        lo, hi = _amount(e.get("from")), _amount(e.get("to"))
        if lo is None and hi is None:
            continue
        if lo and hi and lo != hi:
            amount = f"{lo} – {hi}"
        elif lo and hi:
            amount = lo
        else:
            amount = f"od {lo}" if lo else f"do {hi}"
        contract = _value(e.get("type")) or ""
        currency = (e.get("currency") or "PLN").upper()
        if e.get("gross"):
            kind = "brutto"
        else:
            kind = "netto (+VAT)" if contract == "b2b" else "netto"
        unit = UNIT_LABELS.get(e.get("unit") or "month", e.get("unit") or "")
        text = f"{amount} {currency} {kind}" + (f"/{unit}" if unit else "")
        label = CONTRACT_LABELS.get(contract, contract)
        if label:
            text += f" ({label})"
        if text not in seen:
            seen.add(text)
            parts.append(text)
    return " | ".join(parts) if parts else "Brak wynagrodzenia"


def _unique(items):
    seen, out = set(), []
    for it in items:
        key = it.strip().lower()
        if it.strip() and key not in seen:
            seen.add(key)
            out.append(it.strip())
    return out


def build_location(cities, workplace_type):
    cities = _unique([normalize_city(c) for c in cities if c])
    mode = WORK_MODE_LABELS.get(workplace_type or "")
    if not cities:
        return "Zdalnie" if workplace_type == "remote" else "Nieokreślona"
    return ", ".join(cities + ([mode] if mode else []))[:255]


class _TextExtractor(HTMLParser):
    """HTML description → plain text: paragraphs/headings as lines, list items as '- '."""

    BLOCK = {"p", "div", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "br", "tr"}

    def __init__(self):
        super().__init__(convert_charrefs=True)
        self.lines, self.current = [], []
        self.bullet = False  # the next text line starts a list item (<li><p>text</p></li>)

    def _flush(self):
        text = " ".join("".join(self.current).split())
        if text:
            self.lines.append(f"- {text}" if self.bullet else text)
            self.bullet = False
        self.current = []

    def handle_starttag(self, tag, attrs):
        if tag == "li" or tag in self.BLOCK:
            self._flush()
        if tag == "li":
            self.bullet = True

    def handle_endtag(self, tag):
        if tag == "li" or tag in self.BLOCK:
            self._flush()
        if tag == "li":
            self.bullet = False

    def handle_data(self, data):
        self.current.append(data)

    def text(self):
        self._flush()
        return "\n".join(self.lines)


def html_to_text(body):
    if not body:
        return ""
    parser = _TextExtractor()
    parser.feed(body)
    parser.close()
    return parser.text().strip()


def _job_posting(page):
    for block in page.ld_json:
        items = block if isinstance(block, list) else [block]
        for item in items:
            if isinstance(item, dict) and item.get("@type") == "JobPosting":
                return item
    return None


def find_offer(page, slug=None):
    """The full offer object of an offer page (has companyName, body and skills)."""
    pattern = r'\{"slug":"' + (re.escape(slug) if slug else r'[^"]+') + r'","title":'
    for m in re.finditer(pattern, page.flight, re.I):
        try:
            candidate = page.decode_at(m.start())
        except json.JSONDecodeError:
            continue
        if isinstance(candidate, dict) and "companyName" in candidate and "body" in candidate \
                and "requiredSkills" in candidate:
            return candidate
    return None


def parse_offer(page, slug=None, fetched_at_digits=6, fetched_at=None):
    """Map one offer page to the backend JSON contract."""
    offer = find_offer(page, slug) or (find_offer(page) if slug else None)
    if offer is None:
        if _job_posting(page) is None:
            raise NotFoundError("Ogłoszenie nie istnieje lub zostało usunięte")
        raise StructureError("Brak danych ogłoszenia na stronie — zmieniła się struktura serwisu")
    if offer.get("isOfferActive") is False:
        raise NotFoundError("Ogłoszenie jest nieaktywne (wygasło)")

    title = (offer.get("title") or "").strip()
    company = (offer.get("companyName") or "").strip()
    if not title or not company or not offer.get("slug"):
        raise StructureError("Brak tytułu, firmy lub adresu ogłoszenia — zmieniła się struktura danych")

    locations = [m for m in offer.get("multilocation") or [] if isinstance(m, dict)]
    canonical = choose_canonical_slug(locations, offer["slug"])
    canonical_city = next((m.get("city") for m in locations if (m.get("slug") or "").lower() == canonical),
                          offer.get("city"))
    cities = [canonical_city] + [m.get("city") for m in locations] + [offer.get("city")]

    body = offer.get("body")
    if isinstance(body, str) and TEXT_REF_RE.match(body):
        body = page.text_row(body)
    description = html_to_text(body)
    if not description:
        posting = _job_posting(page) or {}
        description = html.unescape(posting.get("description") or "").strip()

    requirements = _unique(_skill_names(offer.get("requiredSkills")))
    required_keys = {r.lower() for r in requirements}
    nice_to_have = _unique([s for s in _skill_names(offer.get("niceToHaveSkills")) if s.lower() not in required_keys])

    country = (offer.get("countryCode") or "").upper()
    return {
        "title": title,
        "description": description or "Brak opisu",
        "location": build_location(cities, _value(offer.get("workplaceType"))),
        "salaryRange": format_salary(offer.get("employmentTypes")),
        "level": map_level(_value(offer.get("experienceLevel"))),
        "fetchedAt": format_fetched_at(fetched_at or now_utc(), fetched_at_digits),
        "offerUrl": offer_url(canonical),
        "company": company,
        "requirements": requirements,
        "niceToHave": nice_to_have,
        "source": SOURCE,
        "_inPoland": not country or country == "PL",
        "_levelCode": (_value(offer.get("experienceLevel")) or "").lower(),
    }


def to_contract(offer):
    """Drop internal helper keys before printing/saving."""
    return {k: v for k, v in offer.items() if not k.startswith("_")}


REQUIRED_FIELDS = ("title", "company", "offerUrl", "source", "fetchedAt")


def validate_offer(offer):
    missing = [f for f in REQUIRED_FIELDS if not offer.get(f)]
    if missing:
        raise StructureError(f"Brak wymaganych pól: {missing}")
    if not isinstance(offer.get("requirements"), list):
        raise StructureError("Pole requirements musi być tablicą")
    if not offer["offerUrl"].startswith(BASE_URL + OFFER_PATH_PREFIX):
        raise StructureError(f"Nieprawidłowy offerUrl: {offer['offerUrl']}")
