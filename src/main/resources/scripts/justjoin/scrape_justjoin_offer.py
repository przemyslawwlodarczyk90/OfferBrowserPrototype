"""Scrape a single justjoin.it offer.

Usage: python scrape_justjoin_offer.py "https://justjoin.it/job-offer/<nazwa-oferty>"

stdout: exactly one JSON object (the offer) on success, nothing otherwise.
stderr: logs.
Exit codes: see justjoin_common (0 ok, 2 bad URL, 3 blocked, 4 not found,
5 page structure changed, 6 network error, 1 unexpected).
"""
import json
import logging
import sys

import justjoin_common as jj

log = logging.getLogger("justjoin.offer")


def scrape(url, session=None):
    canonical_input = jj.normalize_offer_url(url)
    session = session or jj.create_session()
    try:
        log.info("Pobieram ogłoszenie: %s", canonical_input)
        page = jj.parse_page(jj.fetch_html(session, canonical_input))
        offer = jj.parse_offer(page, slug=jj.slug_from_offer_url(canonical_input), fetched_at_digits=3)
        jj.validate_offer(offer)
        if not offer["_inPoland"]:
            log.warning("Ogłoszenie nie dotyczy pracy w Polsce: %s", offer["location"])
        return jj.to_contract(offer)
    finally:
        session.close()


def main(argv):
    jj.setup_logging()
    if len(argv) != 2:
        print("Użycie: python scrape_justjoin_offer.py <URL ogłoszenia justjoin.it>", file=sys.stderr)
        return jj.EXIT_USAGE
    try:
        offer = scrape(argv[1])
    except ValueError as e:
        log.error("Nieprawidłowy URL: %s", e)
        return jj.EXIT_USAGE
    except jj.ScraperError as e:
        log.error("%s", e)
        return e.exit_code
    except Exception:
        log.exception("Nieoczekiwany błąd")
        return jj.EXIT_UNEXPECTED
    log.info("Pobrano: %s — %s (%s)", offer["title"], offer["company"], offer["offerUrl"])
    print(json.dumps(offer, ensure_ascii=False))
    return jj.EXIT_OK


if __name__ == "__main__":
    sys.exit(main(sys.argv))
