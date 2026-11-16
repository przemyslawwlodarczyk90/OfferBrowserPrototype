"""Bulk scraper for theprotocol.it Java offers (trainee / junior / mid).

Phase A (discovery): walks every search URL page by page and collects unique
offer groups (one group = one posting, possibly published in many cities).
Phase B (details): opens each unique offer once and maps it to the backend
JSON contract.

Result: data/offers/theprotocol_offers.json (written atomically; the previous
file is kept when the run fails). stdout: one JSON run summary; stderr: logs.

Usage:
  python scrape_theprotocol.py [--urls-file PATH] [--limit N] [--delay S] [--max-minutes M]
                               [--max-pages P] [--output PATH] [--url URL ...]

The search URLs come from the database (admin panel) via --urls-file; SEARCH_URLS
below are used only when the script is run by hand.
"""
import argparse
import json
import logging
import os
import sys
import tempfile
import time

import theprotocol_common as tp

log = logging.getLogger("theprotocol.bulk")

LEVELS = "trainee,assistant,junior,mid;p"
SEARCH_URLS = [
    f"{tp.BASE_URL}/filtry/java;t/{LEVELS}",                      # Java – cała Polska
    f"{tp.BASE_URL}/filtry/java;t/{LEVELS}/warszawa;wp",          # Java – Warszawa
    f"{tp.BASE_URL}/filtry/java;t/{LEVELS}/lodz;wp",              # Java – Łódź
    f"{tp.BASE_URL}/filtry/java;t/trainee,assistant,junior;p",    # Java – junior / młodszy programista
    f"{tp.BASE_URL}/filtry/java;t/mid;p",                         # Java – mid
    f"{tp.BASE_URL}/filtry/java;t/{LEVELS}/zdalna;rw",            # Java – zdalnie
]
DEFAULT_OUTPUT = os.path.join("data", "offers", "theprotocol_offers.json")
DEFAULT_DELAY = 1.5
DEFAULT_MAX_PAGES = 30
DEFAULT_MAX_MINUTES = 40


class Fetcher:
    """HTTP access with polite pacing; replaced by a fake in tests."""

    def __init__(self, delay, session=None):
        self.delay = delay
        self.session = session or tp.create_session()
        self._last = 0.0

    def next_data(self, url):
        wait = self.delay - (time.monotonic() - self._last)
        if wait > 0:
            time.sleep(wait)
        try:
            return tp.extract_next_data(tp.fetch_html(self.session, url))
        finally:
            self._last = time.monotonic()

    def close(self):
        self.session.close()


def page_url(search_url, page):
    return search_url if page == 1 else f"{search_url}?pageNumber={page}"


def discover(fetcher, search_urls, max_pages, deadline, stats):
    """Return {groupId: listing entry} across all search URLs."""
    groups = {}
    for search_url in search_urls:
        page, seen_on_url = 1, set()
        while page <= max_pages:
            if time.monotonic() > deadline:
                raise TimeoutError("Przekroczono limit czasu w fazie wyszukiwania")
            url = page_url(search_url, page)
            data = fetcher.next_data(url)
            offers, number, page_count, total = tp.parse_listing(data)
            stats["listingPages"] += 1
            new_ids = {o["groupId"] for o in offers} - seen_on_url
            log.info("Lista %s: strona %d/%d, %d ofert (łącznie w filtrze: %d)",
                     search_url, number, page_count, len(offers), total)
            if not offers or not new_ids:
                break  # past the last page, or the site repeats a page
            seen_on_url |= new_ids
            for o in offers:
                stats["found"] += 1
                if not tp.is_wanted_level(o["levels"]):
                    stats["skippedLevel"] += 1
                    continue
                groups.setdefault(o["groupId"], o)
            if page >= page_count:
                break
            page += 1
        else:
            log.warning("Osiągnięto limit %d stron dla %s", max_pages, search_url)
    return groups


def fetch_details(fetcher, groups, limit, deadline, stats):
    offers, seen_urls = [], set()
    for i, entry in enumerate(groups.values(), start=1):
        if limit is not None and stats["detailsOk"] >= limit:
            log.info("Tryb testowy: osiągnięto limit %d ofert", limit)
            break
        if time.monotonic() > deadline:
            log.warning("Przekroczono limit czasu — zapisuję %d pobranych ofert", len(offers))
            stats["timedOut"] = True
            break
        url = tp.offer_url_from_name(entry["offerUrlName"])
        try:
            offer = tp.parse_offer(fetcher.next_data(url), fetched_at_digits=6)
            tp.validate_offer(offer)
        except tp.BlockedError:
            raise
        except tp.NotFoundError as e:
            stats["notFound"] += 1
            log.info("[%d/%d] Pominięto (nieaktualne): %s — %s", i, len(groups), url, e)
            continue
        except tp.ScraperError as e:
            stats["errors"] += 1
            log.warning("[%d/%d] Błąd ogłoszenia %s: %s", i, len(groups), url, e)
            continue
        if not offer["_inPoland"]:
            stats["skippedAbroad"] += 1
            log.info("[%d/%d] Pominięto (poza Polską): %s", i, len(groups), offer["title"])
            continue
        if offer["offerUrl"] in seen_urls:
            continue
        seen_urls.add(offer["offerUrl"])
        offers.append(tp.to_contract(offer))
        stats["detailsOk"] += 1
        log.info("[%d/%d] OK: %s — %s", i, len(groups), offer["title"], offer["company"])
    return offers


def save_atomic(offers, path):
    folder = os.path.dirname(os.path.abspath(path))
    os.makedirs(folder, exist_ok=True)
    fd, tmp = tempfile.mkstemp(prefix=".theprotocol_", suffix=".json.tmp", dir=folder)
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


def run(args, fetcher):
    started = time.monotonic()
    deadline = started + args.max_minutes * 60
    stats = {
        "source": tp.SOURCE, "startedAt": tp.format_fetched_at(tp.now_utc(), 3),
        "listingPages": 0, "found": 0, "unique": 0, "skippedLevel": 0,
        "detailsOk": 0, "notFound": 0, "skippedAbroad": 0, "errors": 0,
        "saved": 0, "timedOut": False, "status": "FAILED", "message": "",
    }
    exit_code = tp.EXIT_UNEXPECTED
    try:
        groups = discover(fetcher, args.url or SEARCH_URLS, args.max_pages, deadline, stats)
        stats["unique"] = len(groups)
        log.info("Faza A zakończona: %d ofert na listach, %d unikalnych", stats["found"], len(groups))
        offers = fetch_details(fetcher, groups, args.limit, deadline, stats)
        if groups and not offers:
            stats["message"] = "Nie udało się pobrać żadnego ogłoszenia — poprzedni plik pozostaje bez zmian"
            exit_code = tp.EXIT_NETWORK
        else:
            save_atomic(offers, args.output)
            stats["saved"] = len(offers)
            partial = stats["errors"] or stats["timedOut"]
            stats["status"] = "PARTIAL" if partial else "SUCCESS"
            stats["message"] = f"Zapisano {len(offers)} ofert do {args.output}"
            exit_code = tp.EXIT_OK
    except tp.BlockedError as e:
        stats["message"] = f"{e}. Poprzedni plik pozostaje bez zmian."
        exit_code = tp.EXIT_BLOCKED
    except tp.ScraperError as e:
        stats["message"] = str(e)
        exit_code = e.exit_code
    except TimeoutError as e:
        stats["message"] = str(e)
        exit_code = tp.EXIT_NETWORK
    stats["durationSec"] = round(time.monotonic() - started, 1)
    log_fn = log.info if exit_code == tp.EXIT_OK else log.error
    log_fn("Koniec [%s]: znaleziono=%d unikalne=%d szczegóły=%d błędy=%d zapisano=%d czas=%.1fs — %s",
           stats["status"], stats["found"], stats["unique"], stats["detailsOk"],
           stats["errors"], stats["saved"], stats["durationSec"], stats["message"])
    return exit_code, stats


def parse_args(argv):
    p = argparse.ArgumentParser(description="Scraper ofert Java z theprotocol.it")
    p.add_argument("--output", default=DEFAULT_OUTPUT)
    p.add_argument("--limit", type=int, default=None, help="tryb testowy: maks. liczba pobranych ogłoszeń")
    p.add_argument("--delay", type=float, default=DEFAULT_DELAY, help="odstęp między zapytaniami [s]")
    p.add_argument("--max-pages", type=int, default=DEFAULT_MAX_PAGES)
    p.add_argument("--max-minutes", type=float, default=DEFAULT_MAX_MINUTES)
    p.add_argument("--url", action="append", help="zastępuje domyślną listę URL-i wyszukiwania")
    p.add_argument("--urls-file", help="plik JSON z adresami wyszukiwania (panel admina, przygotowuje Java)")
    return p.parse_args(argv)


def load_search_urls(path):
    with open(path, encoding="utf-8") as f:
        data = json.load(f)
    if not isinstance(data, list) or not all(isinstance(x, str) and x.strip() for x in data):
        raise ValueError("Plik z adresami wyszukiwania musi zawierać tablicę adresów")
    if not data:
        raise ValueError("Lista adresów wyszukiwania jest pusta — dodaj adresy w panelu administratora")
    return [x.strip() for x in data]


def main(argv):
    tp.setup_logging()
    args = parse_args(argv)
    if args.urls_file and not args.url:
        try:
            args.url = load_search_urls(args.urls_file)
        except (OSError, ValueError) as e:
            message = f"Nie można odczytać adresów wyszukiwania ({e}) — pobieranie przerwane"
            log.error(message)
            print(json.dumps({"source": tp.SOURCE, "status": "FAILED", "message": message}, ensure_ascii=False))
            return tp.EXIT_USAGE
    fetcher = Fetcher(args.delay)
    try:
        exit_code, stats = run(args, fetcher)
    finally:
        fetcher.close()
    print(json.dumps(stats, ensure_ascii=False))
    return exit_code


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
