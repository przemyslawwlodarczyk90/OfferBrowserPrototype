"""Shared fetching and parsing logic for theprotocol.it scrapers.

theprotocol.it is a Next.js site: every listing and offer page embeds its data
as JSON in <script id="__NEXT_DATA__">. We read that JSON over plain HTTP
(no browser), which is lighter and more stable than DOM selectors.

Used by scrape_theprotocol.py (bulk) and scrape_theprotocol_offer.py (single).
"""
import json
import logging
import re
import sys
import time
from datetime import datetime, timezone
from urllib.parse import urlsplit

import requests

SOURCE = "TheProtocol"
BASE_URL = "https://theprotocol.it"
OFFER_PATH_PREFIX = "/szczegoly/praca/"
ALLOWED_HOSTS = {"theprotocol.it", "www.theprotocol.it"}

DEFAULT_USER_AGENT = (
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
    "(KHTML, like Gecko) Chrome/140.0 Safari/537.36 OfferBrowserPrototype/1.0"
)
CONNECT_TIMEOUT = 10
READ_TIMEOUT = 30
MAX_ATTEMPTS = 3
BACKOFF_SECONDS = (3, 8, 20)
MAX_RETRY_AFTER = 60

NEXT_DATA_RE = re.compile(
    r'<script id="__NEXT_DATA__" type="application/json"[^>]*>(.*?)</script>', re.S)
OFFER_URL_RE = re.compile(r"^/szczegoly/praca/[^/?#]+,oferta,[0-9a-f-]{36}$", re.I)

# Exit codes shared by both scripts (Java maps them to readable errors).
EXIT_OK = 0
EXIT_UNEXPECTED = 1
EXIT_USAGE = 2
EXIT_BLOCKED = 3
EXIT_NOT_FOUND = 4
EXIT_STRUCTURE = 5
EXIT_NETWORK = 6

# Lowest level wins: an offer for "mid, senior" is shown as Mid.
LEVEL_ORDER = ["trainee", "assistant", "junior", "mid", "senior", "expert"]
LEVEL_LABELS = {
    "trainee": "Trainee",
    "assistant": "Trainee",
    "junior": "Junior",
    "mid": "Mid",
    "senior": "Senior",
    "expert": "Expert",
}
UNKNOWN_LEVEL = "Nieokreślony poziom"
WANTED_LEVELS = {"trainee", "assistant", "junior", "mid"}

PREFERRED_CITIES = ["warszawa", "łódź", "lodz"]

DESCRIPTION_SECTIONS = [
    ("about-project", "O projekcie"),
    ("responsibilities", "Zakres obowiązków"),
    ("requirements-expected", "Wymagania"),
    ("requirements-optional", "Mile widziane"),
    ("work-organization-work-style", "Sposób pracy"),
    ("development-practices", "Praktyki zespołu"),
    ("offered", "Oferujemy"),
    ("training-space", "Rozwój"),
    ("benefits", "Benefity"),
]
PARAGRAPH_SECTIONS = {"about-project"}

WORK_MODE_LABELS = {
    "home-office": "praca zdalna",
    "hybrid": "praca hybrydowa",
    "full-office": "praca stacjonarna",
}

log = logging.getLogger("theprotocol")


class ScraperError(Exception):
    exit_code = EXIT_UNEXPECTED


class BlockedError(ScraperError):
    """Anti-bot challenge or access denied. Never retried aggressively."""
    exit_code = EXIT_BLOCKED


class NotFoundError(ScraperError):
    """Offer removed, archived or never existed."""
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
    import (OfferDTO) expects 3 — mirrors the existing NoFluff scripts."""
    frac = dt.strftime("%f")[:digits]
    return dt.strftime("%Y-%m-%dT%H:%M:%S.") + frac + "Z"


# ── URLs ──────────────────────────────────────────────────────────────────────

def normalize_offer_url(url):
    """Validate a user-supplied offer URL and return its canonical form
    (https, no www, no query/fragment). Raises ValueError otherwise."""
    if not isinstance(url, str) or not url.strip():
        raise ValueError("Pusty URL")
    parts = urlsplit(url.strip())
    if parts.scheme not in ("http", "https"):
        raise ValueError(f"Nieobsługiwany schemat URL: {parts.scheme!r}")
    host = (parts.hostname or "").lower()
    if host not in ALLOWED_HOSTS:
        raise ValueError(f"Domena spoza theprotocol.it: {host!r}")
    path = parts.path.rstrip("/")
    if not OFFER_URL_RE.match(path):
        raise ValueError("To nie jest adres ogłoszenia theprotocol.it (/szczegoly/praca/...,oferta,<id>)")
    return BASE_URL + path


def offer_url_from_name(offer_url_name):
    return BASE_URL + OFFER_PATH_PREFIX + offer_url_name


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
            if resp.status_code == 404:
                raise NotFoundError(f"HTTP 404 dla {url}")
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


def extract_next_data(html):
    m = NEXT_DATA_RE.search(html)
    if not m:
        if looks_like_challenge(200, html):
            raise BlockedError("Strona wyzwania antybotowego zamiast treści")
        raise StructureError("Brak __NEXT_DATA__ na stronie — zmieniła się struktura serwisu")
    try:
        return json.loads(m.group(1))
    except json.JSONDecodeError as e:
        raise StructureError(f"Niepoprawny JSON w __NEXT_DATA__: {e}")


def page_props(next_data):
    try:
        return next_data["props"]["pageProps"]
    except (KeyError, TypeError):
        raise StructureError("Brak props.pageProps w __NEXT_DATA__")


# ── Listing ───────────────────────────────────────────────────────────────────

def parse_listing(next_data):
    """Return (offers, page_number, page_count, total_count) from a listing page."""
    pp = page_props(next_data)
    resp = pp.get("offersResponse")
    if not isinstance(resp, dict) or not isinstance(resp.get("offers"), list):
        raise StructureError("Brak offersResponse.offers na stronie listy")
    page = resp.get("page") or {}
    offers = []
    for o in resp["offers"]:
        if not isinstance(o, dict) or not o.get("offerUrlName"):
            continue
        offers.append({
            "id": o.get("id"),
            "groupId": o.get("groupId") or o.get("id"),
            "title": o.get("title"),
            "offerUrlName": o["offerUrlName"],
            "levels": [p.get("value") for p in o.get("positionLevels") or [] if isinstance(p, dict)],
            "cities": [w.get("city") for w in o.get("workplace") or [] if isinstance(w, dict) and w.get("city")],
        })
    return offers, int(page.get("number") or 1), int(page.get("count") or 1), int(resp.get("offersCount") or 0)


def is_wanted_level(levels):
    """Keep offers with no level info or at least one non-senior level."""
    return not levels or any(lv in WANTED_LEVELS for lv in levels)


# ── Offer ─────────────────────────────────────────────────────────────────────

def _city_of(location):
    return (location or "").split(",")[0].strip()


def choose_canonical_partition(partitions, current_url_name):
    """Pick one stable URL per offer group so repeated runs (and a user
    pasting any location variant) map to the same database row."""
    valid = [p for p in partitions or [] if isinstance(p, dict) and p.get("url")]
    if not valid:
        return current_url_name
    for preferred in PREFERRED_CITIES:
        for p in valid:
            loc = ((p.get("workplaces") or [{}])[0] or {}).get("location")
            if _city_of(loc).lower() == preferred:
                return p["url"]
    return min(valid, key=lambda p: p.get("id") or p["url"])["url"]


def map_level(level_ids):
    known = [lv for lv in LEVEL_ORDER if lv in (level_ids or [])]
    if known:
        return LEVEL_LABELS[known[0]]
    if level_ids:
        return "Manager"
    return UNKNOWN_LEVEL


def _contract_label(name):
    n = (name or "").lower()
    if "b2b" in n:
        return "B2B"
    if "o pracę" in n or "employment" in n:
        return "UoP"
    if "zlecen" in n or "mandate" in n:
        return "umowa zlecenie"
    if "dzieło" in n or "specific" in n:
        return "umowa o dzieło"
    if "staż" in n or "praktyk" in n or "intern" in n:
        return "staż"
    return (name or "").strip()


def _salary_kind(kind):
    k = (kind or "").lower()
    if "vat" in k:
        return "netto (+VAT)"
    if "brutto" in k or "gross" in k:
        return "brutto"
    if "netto" in k or "net" in k:
        return "netto"
    return (kind or "").strip()


def _amount(value):
    if value is None:
        return None
    if isinstance(value, float) and not value.is_integer():
        return f"{value:,.2f}".replace(",", " ")
    return f"{int(value):,}".replace(",", " ")


def format_salary(contracts):
    """'12 000 – 18 000 PLN brutto/mies. (UoP) | 70 – 90 PLN netto (+VAT)/godz. (B2B)'.
    Monthly and hourly rates are never mixed or converted."""
    parts = []
    for c in contracts or []:
        s = c.get("salary") if isinstance(c, dict) else None
        if not s:
            continue
        lo, hi = _amount(s.get("from")), _amount(s.get("to"))
        if lo is None and hi is None:
            continue
        if lo and hi and lo != hi:
            amount = f"{lo} – {hi}"
        elif lo and hi:
            amount = lo
        else:
            amount = f"od {lo}" if lo else f"do {hi}"
        currency = (s.get("currencyCode") or s.get("currencySymbol") or "PLN").strip()
        if currency.lower() in ("zł", "zl", "pln"):
            currency = "PLN"
        unit_id = s.get("timeUnitId")
        unit = {0: "mies.", 1: "godz."}.get(unit_id) or ((s.get("timeUnit") or {}).get("shortForm") or "").strip()
        kind = _salary_kind(s.get("kindCode") or s.get("kindName"))
        text = f"{amount} {currency}"
        if kind:
            text += f" {kind}"
        if unit:
            text += f"/{unit}"
        label = _contract_label(c.get("name"))
        if label:
            text += f" ({label})"
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


def build_location(cities, work_mode_codes):
    cities = _unique([c for c in cities if c])
    modes = [WORK_MODE_LABELS[m] for m in ["home-office", "hybrid", "full-office"] if m in work_mode_codes]
    if not cities:
        if work_mode_codes == {"home-office"}:
            return "Zdalnie"
        return "Nieokreślona"
    text = ", ".join(cities + (["/".join(modes)] if modes else []))
    return text[:255]


def build_description(text_sections):
    by_type = {}
    for s in text_sections or []:
        if isinstance(s, dict) and s.get("type"):
            by_type[s["type"]] = s
    blocks = []
    for section_type, heading in DESCRIPTION_SECTIONS:
        s = by_type.get(section_type)
        if not s:
            continue
        elements = [e.strip() for e in s.get("elements") or [] if isinstance(e, str) and e.strip()]
        if not elements:
            continue
        if section_type in PARAGRAPH_SECTIONS:
            body = "\n".join(elements)
        else:
            body = "\n".join(f"- {e}" for e in elements)
        blocks.append(f"{heading}\n{body}")
    return "\n\n".join(blocks) if blocks else "Brak opisu"


def parse_offer(next_data, fetched_at_digits=6, fetched_at=None):
    """Map one offer page to the backend JSON contract."""
    pp = page_props(next_data)
    if "offer" not in pp:
        raise StructureError("Brak pola 'offer' na stronie ogłoszenia")
    offer = pp["offer"]
    if offer is None:
        raise NotFoundError("Ogłoszenie nie istnieje lub zostało usunięte")
    if not isinstance(offer, dict):
        raise StructureError("Pole 'offer' ma nieoczekiwany typ")

    pub = offer.get("publicationDetails") or {}
    if pub.get("isActive") is False or pub.get("active") is False:
        raise NotFoundError("Ogłoszenie jest nieaktywne (zarchiwizowane)")

    attrs = offer.get("attributes") or {}
    title = ((attrs.get("title") or {}).get("value") or "").strip()
    company = ((attrs.get("employer") or {}).get("name") or "").strip()
    url_name = offer.get("offerUrlName") or pp.get("offerUrlName")
    if not title or not company or not url_name:
        raise StructureError("Brak tytułu, firmy lub adresu ogłoszenia — zmieniła się struktura danych")

    workplaces = [w for w in attrs.get("workplaces") or [] if isinstance(w, dict)]
    countries = {(w.get("country") or "").strip().lower() for w in workplaces if w.get("country")}

    partitions = offer.get("partitions") or []
    canonical = choose_canonical_partition(partitions, url_name)
    partition_cities = [
        _city_of(((p.get("workplaces") or [{}])[0] or {}).get("location"))
        for p in partitions if isinstance(p, dict)
    ]
    canonical_city = next(
        (_city_of(((p.get("workplaces") or [{}])[0] or {}).get("location"))
         for p in partitions if isinstance(p, dict) and p.get("url") == canonical), None)
    cities = ([canonical_city] if canonical_city else []) + partition_cities + [w.get("city") for w in workplaces]

    employment = attrs.get("employment") or {}
    work_modes = {m.get("code") for m in employment.get("detailedWorkModes") or [] if isinstance(m, dict)}

    tech = offer.get("technologies") or {}
    requirements = _unique([t.get("name", "") for t in tech.get("expected") or [] if isinstance(t, dict)])
    required_keys = {r.lower() for r in requirements}
    nice_to_have = _unique([t.get("name", "") for t in tech.get("optional") or []
                            if isinstance(t, dict) and t.get("name", "").strip().lower() not in required_keys])

    return {
        "title": title,
        "description": build_description(offer.get("textSections")),
        "location": build_location(cities, work_modes),
        "salaryRange": format_salary(employment.get("typesOfContracts")),
        "level": map_level(employment.get("positionLevelIds")),
        "fetchedAt": format_fetched_at(fetched_at or now_utc(), fetched_at_digits),
        "offerUrl": offer_url_from_name(canonical),
        "company": company,
        "requirements": requirements,
        "niceToHave": nice_to_have,
        "source": SOURCE,
        "_inPoland": not countries or bool(countries & {"polska", "poland"}),
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
