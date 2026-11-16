"""Bulk scraper for justjoin.it offers (trainee / junior / mid).

Phase A (discovery): walks every search URL page by page (?page=N, 50 offers
per page) and collects unique offer groups (one group = one posting, possibly
published in many cities under different URLs).
Phase B (details): opens each unique offer that is not in the database yet
(--known-urls) once and maps it to the backend JSON contract.

The search URLs come from the database (admin panel) via --urls-file; the
defaults below are used only when the script is run by hand.

Result: data/offers/justjoin_offers.json (written atomically; the previous
file is kept when the run fails). stdout: one JSON run summary; stderr: logs.

Usage:
  python scrape_justjoin.py [--urls-file PATH] [--known-urls PATH] [--limit N]
                            [--delay S] [--max-minutes M] [--max-pages P]
                            [--output PATH] [--url URL ...]
"""
import argparse
import json
import logging
import os
import sys
import tempfile
import time

import justjoin_common as jj

log = logging.getLogger("justjoin.bulk")

LEVELS = "experience-levels=junior,mid"
SEARCH_URLS = [
    f"{jj.BASE_URL}/job-offers/warszawa/java?{LEVELS}",                  # Java – Warszawa
    f"{jj.BASE_URL}/job-offers/lodz/java?{LEVELS}",                      # Java – Łódź
    f"{jj.BASE_URL}/job-offers/all-locations/java?{LEVELS}&remote=yes",  # Java – zdalnie, cała Polska
]
DEFAULT_OUTPUT = os.path.join("data", "offers", "justjoin_offers.json")
DEFAULT_DELAY = 1.5
DEFAULT_MAX_PAGES = 20
DEFAULT_MAX_MINUTES = 40


class Fetcher:
    """HTTP access with polite pacing; replaced by a fake in tests."""

    def __init__(self, delay, session=None):
        self.delay = delay
        self.session = session or jj.create_session()
        self._last = 0.0

    def page(self, url):
        wait = self.delay - (time.monotonic() - self._last)
        if wait > 0:
            time.sleep(wait)
        try:
            return jj.parse_page(jj.fetch_html(self.session, url))
        finally:
            self._last = time.monotonic()

    def close(self):
        self.session.close()


def load_url_list(path):
    """JSON array of strings (search URLs or known offer URLs) prepared by Java."""
    with open(path, encoding="utf-8") as f:
        data = json.load(f)
    if not isinstance(data, list) or not all(isinstance(x, str) for x in data):
        raise ValueError(f"Plik {path} musi zawierać tablicę adresów")
    return data


def load_known_slugs(path):
    return {s for s in (jj.slug_from_offer_url(u) for u in load_url_list(path)) if s}


def discover(fetcher, search_urls, max_pages, deadline, stats):
    """Return {canonical slug: listing entry} across all search URLs."""
    groups, slug_to_group = {}, {}
    for search_url in search_urls:
        page, seen_on_url = 1, set()
        while page <= max_pages:
            if time.monotonic() > deadline:
                raise TimeoutError("Przekroczono limit czasu w fazie wyszukiwania")
            data = fetcher.page(jj.page_url(search_url, page))
            offers, total = jj.parse_listing(data)
            stats["listingPages"] += 1
            new_slugs = {o["slug"] for o in offers} - seen_on_url
            log.info("Lista %s: strona %d, %d ofert (łącznie w wyszukiwaniu: %s)",
                     search_url, page, len(offers), total if total is not None else "?")
            if not offers or not new_slugs:
                break  # past the last page, or the site repeats a page
            seen_on_url |= new_slugs
            for o in offers:
                stats["found"] += 1
                if not jj.is_wanted_level(o["level"]):
                    stats["skippedLevel"] += 1
                    continue
                key = next((slug_to_group[s] for s in o["slugs"] if s in slug_to_group), None)
                if key is None:
                    key = o["slug"]
                    groups[key] = o
                else:
                    stats["duplicatesBetweenLists"] += 1
                for s in o["slugs"]:
                    slug_to_group.setdefault(s, key)
            if total is not None and len(seen_on_url) >= total:
                break
            page += 1
        else:
            log.warning("Osiągnięto limit %d stron dla %s", max_pages, search_url)
    return groups


def fetch_details(fetcher, groups, known_slugs, limit, deadline, stats):
    offers, seen_urls = [], set()
    new_groups = [g for g in groups.values() if not any(s in known_slugs for s in g["slugs"])]
    stats["alreadyInDatabase"] = len(groups) - len(new_groups)
    stats["new"] = len(new_groups)
    log.info("Unikalne oferty: %d, już w bazie: %d, nowe do pobrania: %d",
             len(groups), stats["alreadyInDatabase"], len(new_groups))
    for i, entry in enumerate(new_groups, start=1):
        if limit is not None and stats["detailsOk"] >= limit:
            log.info("Tryb testowy: osiągnięto limit %d ofert", limit)
            break
        if time.monotonic() > deadline:
            log.warning("Przekroczono limit czasu — zapisuję %d pobranych ofert", len(offers))
            stats["timedOut"] = True
            break
        url = jj.offer_url(entry["slug"])
        try:
            stats["detailPagesOpened"] += 1
            offer = jj.parse_offer(fetcher.page(url), slug=entry["slug"], fetched_at_digits=6)
            jj.validate_offer(offer)
        except jj.BlockedError:
            raise
        except jj.NotFoundError as e:
            stats["notFound"] += 1
            log.info("[%d/%d] Pominięto (nieaktualne): %s — %s", i, len(new_groups), url, e)
            continue
        except jj.ScraperError as e:
            stats["errors"] += 1
            log.warning("[%d/%d] Błąd ogłoszenia %s: %s", i, len(new_groups), url, e)
            continue
        if not offer["_inPoland"]:
            stats["skippedAbroad"] += 1
            log.info("[%d/%d] Pominięto (poza Polską): %s", i, len(new_groups), offer["title"])
            continue
        if offer["offerUrl"] in seen_urls or jj.slug_from_offer_url(offer["offerUrl"]) in known_slugs:
            continue
        seen_urls.add(offer["offerUrl"])
        offers.append(jj.to_contract(offer))
        stats["detailsOk"] += 1
        log.info("[%d/%d] OK: %s — %s", i, len(new_groups), offer["title"], offer["company"])
    return offers


def save_atomic(offers, path):
    folder = os.path.dirname(os.path.abspath(path))
    os.makedirs(folder, exist_ok=True)
    fd, tmp = tempfile.mkstemp(prefix=".justjoin_", suffix=".json.tmp", dir=folder)
    try:
        with os.fdopen(fd, "w", encoding="utf-8") as f:
            json.dump(offers, f, ensure_ascii=False, indent=4)
        with open(tmp, encoding="utf-8") as f:
            json.load(f)  # sanity check before replacing the last good file
        os.replace(tmp, path)
    except BaseException:
        if os.path.exists(tmp):
            os.remove(tmp)
        raise


def new_stats():
    return {
        "source": jj.SOURCE, "startedAt": jj.format_fetched_at(jj.now_utc(), 3),
        "listingPages": 0, "found": 0, "duplicatesBetweenLists": 0, "unique": 0, "skippedLevel": 0,
        "alreadyInDatabase": 0, "new": 0, "detailPagesOpened": 0, "detailsOk": 0,
        "notFound": 0, "skippedAbroad": 0, "errors": 0,
        "saved": 0, "timedOut": False, "status": "FAILED", "message": "",
    }


def run(args, fetcher, search_urls, known_slugs):
    started = time.monotonic()
    deadline = started + args.max_minutes * 60
    stats = new_stats()
    exit_code = jj.EXIT_UNEXPECTED
    try:
        groups = discover(fetcher, search_urls, args.max_pages, deadline, stats)
        stats["unique"] = len(groups)
        log.info("Faza A zakończona: %d ofert na listach, %d unikalnych", stats["found"], len(groups))
        offers = fetch_details(fetcher, groups, known_slugs, args.limit, deadline, stats)
        if stats["new"] and not offers and stats["errors"]:
            stats["message"] = "Nie udało się pobrać żadnego ogłoszenia — poprzedni plik pozostaje bez zmian"
            exit_code = jj.EXIT_NETWORK
        else:
            save_atomic(offers, args.output)
            stats["saved"] = len(offers)
            partial = stats["errors"] or stats["timedOut"]
            stats["status"] = "PARTIAL" if partial else "SUCCESS"
            stats["message"] = (f"Nowych ofert: {stats['new']} (z {stats['unique']} unikalnych), "
                                f"zapisano: {len(offers)}")
            exit_code = jj.EXIT_OK
    except jj.BlockedError as e:
        stats["message"] = f"{e}. Poprzedni plik pozostaje bez zmian."
        exit_code = jj.EXIT_BLOCKED
    except jj.ScraperError as e:
        stats["message"] = str(e)
        exit_code = e.exit_code
    except TimeoutError as e:
        stats["message"] = str(e)
        exit_code = jj.EXIT_NETWORK
    stats["durationSec"] = round(time.monotonic() - started, 1)
    log_fn = log.info if exit_code == jj.EXIT_OK else log.error
    log_fn("Koniec [%s]: znaleziono=%d unikalne=%d w bazie=%d szczegóły=%d błędy=%d zapisano=%d czas=%.1fs — %s",
           stats["status"], stats["found"], stats["unique"], stats["alreadyInDatabase"], stats["detailsOk"],
           stats["errors"], stats["saved"], stats["durationSec"], stats["message"])
    return exit_code, stats


def parse_args(argv):
    p = argparse.ArgumentParser(description="Scraper ofert z justjoin.it")
    p.add_argument("--output", default=DEFAULT_OUTPUT)
    p.add_argument("--urls-file", help="plik JSON z adresami wyszukiwania (lista z panelu admina, przygotowuje Java)")
    p.add_argument("--known-urls", help="plik JSON z adresami ofert, które już są w bazie (przygotowuje Java)")
    p.add_argument("--limit", type=int, default=None, help="tryb testowy: maks. liczba pobranych ogłoszeń")
    p.add_argument("--delay", type=float, default=DEFAULT_DELAY, help="odstęp między zapytaniami [s]")
    p.add_argument("--max-pages", type=int, default=DEFAULT_MAX_PAGES)
    p.add_argument("--max-minutes", type=float, default=DEFAULT_MAX_MINUTES)
    p.add_argument("--url", action="append", help="zastępuje listę URL-i wyszukiwania")
    return p.parse_args(argv)


def resolve_inputs(args):
    """(search URLs, known slugs) — raises ValueError for unreadable/invalid input files."""
    if args.url:
        urls = args.url
    elif args.urls_file:
        urls = load_url_list(args.urls_file)
    else:
        log.warning("Brak --urls-file: używam domyślnych adresów wyszukiwania (uruchomienie ręczne)")
        urls = SEARCH_URLS
    urls = [jj.validate_search_url(u) for u in urls]
    if not urls:
        raise ValueError("Lista adresów wyszukiwania jest pusta — dodaj adresy w panelu administratora")
    if args.known_urls:
        known = load_known_slugs(args.known_urls)
    else:
        log.warning("Brak --known-urls: wszystkie znalezione oferty traktowane jako nowe (uruchomienie ręczne)")
        known = set()
    log.info("Adresów wyszukiwania: %d, ofert justjoin.it w bazie: %d", len(urls), len(known))
    return urls, known


def main(argv):
    jj.setup_logging()
    args = parse_args(argv)
    try:
        search_urls, known_slugs = resolve_inputs(args)
    except (OSError, ValueError) as e:
        stats = new_stats()
        stats["message"] = f"Nieprawidłowe dane wejściowe ({e}) — pobieranie przerwane"
        log.error(stats["message"])
        print(json.dumps(stats, ensure_ascii=False))
        return jj.EXIT_USAGE
    fetcher = Fetcher(args.delay)
    try:
        exit_code, stats = run(args, fetcher, search_urls, known_slugs)
    finally:
        fetcher.close()
    print(json.dumps(stats, ensure_ascii=False))
    return exit_code


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
