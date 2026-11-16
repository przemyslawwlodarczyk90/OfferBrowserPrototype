"""Scrape a single theprotocol.it offer.

Usage: python scrape_theprotocol_offer.py "https://theprotocol.it/szczegoly/praca/...,oferta,<id>"

stdout: exactly one JSON object (the offer) on success, nothing otherwise.
stderr: logs.
Exit codes: see theprotocol_common (0 ok, 2 bad URL, 3 blocked, 4 not found,
5 page structure changed, 6 network error, 1 unexpected).
"""
import json
import logging
import sys

import theprotocol_common as tp

log = logging.getLogger("theprotocol.offer")


def scrape(url, session=None):
    canonical_input = tp.normalize_offer_url(url)
    session = session or tp.create_session()
    try:
        log.info("Pobieram ogłoszenie: %s", canonical_input)
        html = tp.fetch_html(session, canonical_input)
        offer = tp.parse_offer(tp.extract_next_data(html), fetched_at_digits=3)
        tp.validate_offer(offer)
        if not offer["_inPoland"]:
            log.warning("Ogłoszenie nie dotyczy pracy w Polsce: %s", offer["location"])
        return tp.to_contract(offer)
    finally:
        session.close()


def main(argv):
    tp.setup_logging()
    if len(argv) != 2:
        print("Użycie: python scrape_theprotocol_offer.py <URL ogłoszenia theprotocol.it>", file=sys.stderr)
        return tp.EXIT_USAGE
    try:
        offer = scrape(argv[1])
    except ValueError as e:
        log.error("Nieprawidłowy URL: %s", e)
        return tp.EXIT_USAGE
    except tp.ScraperError as e:
        log.error("%s", e)
        return e.exit_code
    except Exception:
        log.exception("Nieoczekiwany błąd")
        return tp.EXIT_UNEXPECTED
    log.info("Pobrano: %s — %s (%s)", offer["title"], offer["company"], offer["offerUrl"])
    print(json.dumps(offer, ensure_ascii=False))
    return tp.EXIT_OK


if __name__ == "__main__":
    sys.exit(main(sys.argv))
