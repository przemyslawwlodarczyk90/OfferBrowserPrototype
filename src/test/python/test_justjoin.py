"""Tests for the justjoin.it scrapers.

Fixtures in fixtures/justjoin/ are real pages from justjoin.it (2026-10-22)
trimmed to the server-components rows the parser reads (listings: the "offers"
array and the search "count"; offers: the offer object, its description text
row and the JSON-LD JobPosting). Variants that did not occur in the sample
(senior, abroad, inactive, changed structure) are derived in the tests below.

Run: python -m unittest discover -s src/test/python -v
"""
import io
import json
import os
import sys
import tempfile
import types
import unittest
from contextlib import redirect_stdout
from datetime import datetime, timezone
from unittest import mock

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.abspath(os.path.join(HERE, "..", "..", "main", "resources", "scripts", "justjoin")))

import justjoin_common as jj  # noqa: E402
import scrape_justjoin as bulk  # noqa: E402
import scrape_justjoin_offer as single  # noqa: E402

FIX = os.path.join(HERE, "fixtures", "justjoin")
FIXED_NOW = datetime(2026, 10, 22, 12, 0, 0, 123456, tzinfo=timezone.utc)

EMAGINE = "emagine-polska-java-developer-warszawa-java-9bd7c937"
IT_FACTORY_WAW = "it-factory-analityk-programista-java-warszawa-java"
IT_FACTORY_LODZ = "it-factory-analityk-programista-java-lodz-java"
ITLT = "itlt-junior-mid-java-developer-nice-to-have-gwt--warszawa-java-19595d7a"
FINANTEQ = "finanteq-junior-backend-developer-java--lublin-java-67b61924"


def raw(name):
    with open(os.path.join(FIX, name), encoding="utf-8") as f:
        return f.read()


def page(name):
    return jj.parse_page(raw(name))


def with_offer_changes(name, **changes):
    """Fixture page with fields of its offer object replaced (derived variants)."""
    p = page(name)
    offer = jj.find_offer(p)
    start = p.flight.find('{"slug":"' + offer["slug"] + '","title":')
    _, end = jj._DECODER.raw_decode(p.flight, start)
    offer.update(changes)
    patched = json.dumps(offer, ensure_ascii=False, separators=(",", ":"))
    return jj.Page(p.flight[:start] + patched + p.flight[end:], p.ld_json)


class OfferParsingTest(unittest.TestCase):

    def test_remote_offer_with_hourly_b2b_salary(self):
        o = jj.parse_offer(page("offer_remote_b2b_hourly.html"), fetched_at=FIXED_NOW)
        self.assertEqual(o["title"], "Java Developer")
        self.assertEqual(o["company"], "emagine Polska")
        self.assertEqual(o["level"], "Mid")
        self.assertEqual(o["location"], "Warszawa, praca zdalna")
        self.assertEqual(o["salaryRange"], "160 – 190 PLN netto (+VAT)/godz. (B2B)")
        self.assertEqual(o["offerUrl"], jj.offer_url(EMAGINE))
        self.assertEqual(o["source"], "JustJoin")
        self.assertIn("Spring Boot", o["requirements"])
        self.assertEqual(o["niceToHave"], [])
        self.assertTrue(o["_inPoland"])

    def test_converted_currencies_are_not_listed(self):
        o = jj.parse_offer(page("offer_remote_b2b_hourly.html"))
        for currency in ("USD", "EUR", "CHF", "GBP"):
            self.assertNotIn(currency, o["salaryRange"])

    def test_description_comes_from_text_row_with_list_items(self):
        o = jj.parse_offer(page("offer_remote_b2b_hourly.html"))
        self.assertTrue(o["description"].startswith("We’re seeking to hire 2 backend developers"))
        self.assertIn("\n- Design, build, and maintain scalable microservices", o["description"])
        self.assertIn("Authentication & Wallet Tribe", o["description"])  # entities decoded
        self.assertNotIn("<p>", o["description"])

    def test_uop_and_b2b_salaries_with_junior_level(self):
        o = jj.parse_offer(page("offer_junior_uop_b2b.html"))
        self.assertEqual(o["level"], "Junior")
        self.assertEqual(o["location"], "Warszawa, praca stacjonarna")
        self.assertEqual(o["salaryRange"],
                         "70 – 110 PLN netto (+VAT)/mies. (B2B) | 9 000 – 14 000 PLN brutto/mies. (UoP)")

    def test_multilocation_offer_without_salary(self):
        o = jj.parse_offer(page("offer_multilocation_no_salary.html"))
        self.assertEqual(o["salaryRange"], "Brak wynagrodzenia")
        self.assertEqual(o["location"], "Warszawa, Lublin, Łódź, Olsztyn, Białystok, praca hybrydowa")
        self.assertEqual(o["offerUrl"], jj.offer_url(IT_FACTORY_WAW))  # Warszawa variant is canonical

    def test_any_location_variant_maps_to_same_canonical_url(self):
        p = page("offer_multilocation_no_salary.html")
        self.assertEqual(jj.parse_offer(p, slug=IT_FACTORY_LODZ)["offerUrl"], jj.offer_url(IT_FACTORY_WAW))
        self.assertEqual(jj.choose_canonical_slug(
            [{"slug": "a-lublin", "city": "Lublin"}, {"slug": "a-lodz", "city": "Łódź"}], "a-lublin"), "a-lodz")
        self.assertEqual(jj.choose_canonical_slug(
            [{"slug": "b-lublin", "city": "Lublin"}, {"slug": "a-gdansk", "city": "Gdańsk"}], "b-lublin"), "a-gdansk")

    def test_fetched_at_formats_match_java_patterns(self):
        self.assertEqual(jj.format_fetched_at(FIXED_NOW, 6), "2026-10-22T12:00:00.123456Z")
        self.assertEqual(jj.format_fetched_at(FIXED_NOW, 3), "2026-10-22T12:00:00.123Z")

    def test_nice_to_have_skills_without_duplicates_of_required(self):
        p = with_offer_changes("offer_remote_b2b_hourly.html",
                               niceToHaveSkills=[{"name": "Docker"}, {"name": "java"}, {"name": "Docker"}])
        self.assertEqual(jj.parse_offer(p)["niceToHave"], ["Docker"])

    def test_abroad_offer_is_flagged(self):
        p = with_offer_changes("offer_remote_b2b_hourly.html", countryCode="DE")
        self.assertFalse(jj.parse_offer(p)["_inPoland"])

    def test_inactive_offer_raises_not_found(self):
        p = with_offer_changes("offer_remote_b2b_hourly.html", isOfferActive=False)
        with self.assertRaises(jj.NotFoundError):
            jj.parse_offer(p)

    def test_offer_without_company_is_rejected(self):
        p = with_offer_changes("offer_remote_b2b_hourly.html", companyName="")
        with self.assertRaises(jj.StructureError):
            jj.parse_offer(p)

    def test_description_falls_back_to_json_ld(self):
        p = with_offer_changes("offer_remote_b2b_hourly.html", body="$ffff")
        self.assertIn("seeking to hire", jj.parse_offer(p)["description"])

    def test_page_without_offer_object_raises_structure_error(self):
        p = page("offer_remote_b2b_hourly.html")
        broken = jj.Page(p.flight.replace('"companyName"', '"employerName"'), p.ld_json)
        with self.assertRaises(jj.StructureError):
            jj.parse_offer(broken)

    def test_page_without_payload(self):
        with self.assertRaises(jj.StructureError):
            jj.parse_page("<html><body>nic tu nie ma</body></html>")
        with self.assertRaises(jj.BlockedError):
            jj.parse_page(raw("cloudflare_challenge.html"))


class SalaryLevelLocationTest(unittest.TestCase):

    def test_open_ended_and_same_bounds(self):
        self.assertEqual(jj.format_salary([{"from": 8000, "to": None, "type": "permanent", "unit": "month",
                                            "gross": True, "currency": "pln"}]), "od 8 000 PLN brutto/mies. (UoP)")
        self.assertEqual(jj.format_salary([{"from": 100, "to": 100, "type": "b2b", "unit": "hour",
                                            "gross": False, "currency": "PLN"}]), "100 PLN netto (+VAT)/godz. (B2B)")
        self.assertEqual(jj.format_salary([]), "Brak wynagrodzenia")

    def test_levels(self):
        self.assertEqual(jj.map_level("intern"), "Trainee")
        self.assertEqual(jj.map_level("junior"), "Junior")
        self.assertEqual(jj.map_level("c_level"), "Expert")
        self.assertEqual(jj.map_level(None), jj.UNKNOWN_LEVEL)
        self.assertTrue(jj.is_wanted_level("mid"))
        self.assertFalse(jj.is_wanted_level("senior"))

    def test_city_names_are_unified(self):
        self.assertEqual(jj.normalize_city("Warszawa (Mazowieckie)"), "Warszawa")
        self.assertEqual(jj.normalize_city("warsaw"), "Warszawa")
        self.assertEqual(jj.normalize_city("Lodz"), "Łódź")
        self.assertEqual(jj.build_location(["Warsaw", "Warszawa", "Kraków"], "hybrid"),
                         "Warszawa, Kraków, praca hybrydowa")
        self.assertEqual(jj.build_location([], "remote"), "Zdalnie")


class UrlTest(unittest.TestCase):

    def test_accepts_and_canonicalizes(self):
        self.assertEqual(jj.normalize_offer_url(f"https://www.justjoin.it/job-offer/{EMAGINE}/?utm=x#top"),
                         jj.offer_url(EMAGINE))
        self.assertEqual(jj.normalize_offer_url(f"http://justjoin.it/pl/job-offer/{EMAGINE}"), jj.offer_url(EMAGINE))

    def test_rejects_foreign_or_malformed(self):
        for bad in ["", "ftp://justjoin.it/job-offer/x", "https://justjoin.it.evil.com/job-offer/x",
                    "https://justjoin.it/job-offers/warszawa/java", "https://nofluffjobs.com/pl/job/x"]:
            with self.assertRaises(ValueError, msg=bad):
                jj.normalize_offer_url(bad)

    def test_page_url_keeps_filters_and_replaces_page(self):
        base = "https://justjoin.it/job-offers/warszawa/java?experience-levels=junior,mid"
        self.assertEqual(jj.page_url(base, 1), base)
        self.assertEqual(jj.page_url(base, 3), base + "&page=3")
        self.assertEqual(jj.page_url(base + "&page=2", 4), base + "&page=4")

    def test_search_url_must_be_on_justjoin(self):
        self.assertEqual(jj.validate_search_url(" https://justjoin.it/job-offers/lodz/java "),
                         "https://justjoin.it/job-offers/lodz/java")
        with self.assertRaises(ValueError):
            jj.validate_search_url("https://theprotocol.it/filtry/java;t")


class ListingTest(unittest.TestCase):

    def test_parse_listing_page(self):
        offers, total = jj.parse_listing(page("listing_warszawa_page1.html"))
        self.assertEqual(total, 7)
        self.assertEqual(len(offers), 4)
        self.assertEqual(offers[0]["slug"], EMAGINE)
        self.assertEqual(offers[0]["level"], "mid")
        multi = offers[3]
        self.assertEqual(multi["slug"], IT_FACTORY_WAW)
        self.assertIn(IT_FACTORY_LODZ, multi["slugs"])

    def test_page_past_the_last_one_is_empty(self):
        self.assertEqual(jj.parse_listing(page("listing_past_last_page.html")), ([], 7))

    def test_listing_without_offers_and_count_is_structure_error(self):
        with self.assertRaises(jj.StructureError):
            jj.parse_listing(jj.Page('0:{"P":null}', []))


class FakeFetcher:
    def __init__(self, pages):
        self.pages = pages
        self.calls = []

    def page(self, url):
        self.calls.append(url)
        value = self.pages[url]
        if isinstance(value, Exception):
            raise value
        return value if isinstance(value, jj.Page) else page(value)

    def close(self):
        pass


WAW = "https://justjoin.it/job-offers/warszawa/java?experience-levels=junior,mid"
LODZ = "https://justjoin.it/job-offers/lodz/java?experience-levels=junior,mid"
REMOTE = "https://justjoin.it/job-offers/all-locations/java?experience-levels=junior,mid&remote=yes"
WAW_P1_SLUGS = [EMAGINE, "pko-bp-finat-fullstack-developer-warszawa-java",
                "fabrity-s-a--mid-senior-java-fullstack-developer-k-m--warszawa-java-02c00a0e", IT_FACTORY_WAW]
WAW_P2_SLUGS = ["xtb-java-developer-warszawa-java-bf0dc7bc", "dcg-integration-developer-java-developer-warszawa-java",
                "allegro-junior-test-automation-engineer-warszawa-java"]


class BulkScraperTest(unittest.TestCase):

    def setUp(self):
        self.tmp = tempfile.mkdtemp()
        self.out = os.path.join(self.tmp, "justjoin_offers.json")

    def args(self, **kw):
        base = dict(limit=None, max_pages=20, max_minutes=5, output=self.out)
        base.update(kw)
        return types.SimpleNamespace(**base)

    def pages(self, **overrides):
        pages = {
            WAW: "listing_warszawa_page1.html",
            jj.page_url(WAW, 2): "listing_warszawa_page2.html",
            LODZ: "listing_lodz_multilocation.html",
            REMOTE: "listing_remote.html",
        }
        # detail pages: every discovered offer resolves to one of the three real offer fixtures
        for slug in WAW_P1_SLUGS + WAW_P2_SLUGS + [FINANTEQ]:
            pages[jj.offer_url(slug)] = "offer_junior_uop_b2b.html"
        pages[jj.offer_url(EMAGINE)] = "offer_remote_b2b_hourly.html"
        pages[jj.offer_url(IT_FACTORY_WAW)] = "offer_multilocation_no_salary.html"
        pages[jj.offer_url(IT_FACTORY_LODZ)] = "offer_multilocation_no_salary.html"
        pages.update(overrides)
        return pages

    def test_paginates_and_dedupes_between_lists_before_opening_details(self):
        fake = FakeFetcher(self.pages())
        code, stats = bulk.run(self.args(), fake, [WAW, LODZ, REMOTE], set())
        self.assertEqual(code, 0)
        self.assertEqual(stats["status"], "SUCCESS")
        self.assertEqual(stats["listingPages"], 4)               # count 7 reached on page 2 → no page 3
        self.assertNotIn(jj.page_url(WAW, 3), fake.calls)
        self.assertEqual(stats["found"], 4 + 3 + 1 + 2)
        self.assertEqual(stats["duplicatesBetweenLists"], 2)     # IT Factory (Łódź variant), emagine (remote)
        self.assertEqual(stats["unique"], 8)
        details = [c for c in fake.calls if "/job-offer/" in c]
        self.assertEqual(len(details), 8)                        # each group opened exactly once
        self.assertNotIn(jj.offer_url(IT_FACTORY_LODZ), fake.calls)
        with open(self.out, encoding="utf-8") as f:
            saved = json.load(f)
        self.assertEqual({o["source"] for o in saved}, {"JustJoin"})
        self.assertTrue(all(not k.startswith("_") for o in saved for k in o))

    def test_offers_already_in_database_are_not_opened(self):
        known = {EMAGINE, IT_FACTORY_LODZ}                       # any variant of a group counts
        fake = FakeFetcher(self.pages())
        code, stats = bulk.run(self.args(), fake, [WAW, LODZ], known)
        self.assertEqual((code, stats["alreadyInDatabase"]), (0, 2))
        self.assertNotIn(jj.offer_url(EMAGINE), fake.calls)
        self.assertNotIn(jj.offer_url(IT_FACTORY_WAW), fake.calls)

    def test_senior_offers_are_skipped(self):
        p = page("listing_lodz_multilocation.html")
        senior = jj.Page(p.flight.replace('"experienceLevel":"mid"', '"experienceLevel":"senior"'), [])
        fake = FakeFetcher(self.pages(**{LODZ: senior}))
        code, stats = bulk.run(self.args(), fake, [LODZ], set())
        self.assertEqual((code, stats["skippedLevel"], stats["unique"], stats["saved"]), (0, 1, 0, 0))

    def test_limit_mode(self):
        code, stats = bulk.run(self.args(limit=2), FakeFetcher(self.pages()), [WAW], set())
        self.assertEqual((code, stats["saved"]), (0, 2))

    def test_blocked_run_keeps_previous_file(self):
        with open(self.out, "w", encoding="utf-8") as f:
            json.dump([{"title": "poprzednie"}], f)
        pages = self.pages(**{jj.offer_url(WAW_P1_SLUGS[1]): jj.BlockedError("Cloudflare")})
        code, stats = bulk.run(self.args(), FakeFetcher(pages), [WAW], set())
        self.assertEqual((code, stats["status"]), (jj.EXIT_BLOCKED, "FAILED"))
        with open(self.out, encoding="utf-8") as f:
            self.assertEqual(json.load(f), [{"title": "poprzednie"}])

    def test_single_bad_offer_gives_partial_result(self):
        pages = self.pages(**{jj.offer_url(WAW_P1_SLUGS[1]): jj.StructureError("zmiana"),
                              jj.offer_url(WAW_P1_SLUGS[2]): jj.NotFoundError("usunięta")})
        code, stats = bulk.run(self.args(), FakeFetcher(pages), [WAW], set())
        self.assertEqual((code, stats["status"], stats["errors"], stats["notFound"]), (0, "PARTIAL", 1, 1))

    def test_all_details_failing_does_not_overwrite(self):
        with open(self.out, "w", encoding="utf-8") as f:
            f.write("[]")
        failing = {jj.offer_url(s): jj.NetworkError("timeout") for s in WAW_P1_SLUGS + WAW_P2_SLUGS}
        code, stats = bulk.run(self.args(), FakeFetcher(self.pages(**failing)), [WAW], set())
        self.assertEqual((code, stats["saved"]), (jj.EXIT_NETWORK, 0))

    def test_nothing_new_writes_empty_list(self):
        known = set(WAW_P1_SLUGS + WAW_P2_SLUGS)
        code, stats = bulk.run(self.args(), FakeFetcher(self.pages()), [WAW], known)
        self.assertEqual((code, stats["status"], stats["saved"], stats["new"]), (0, "SUCCESS", 0, 0))
        with open(self.out, encoding="utf-8") as f:
            self.assertEqual(json.load(f), [])

    def test_repeated_page_stops_pagination(self):
        pages = self.pages(**{jj.page_url(WAW, 2): "listing_warszawa_page1.html"})
        p1 = page("listing_warszawa_page1.html")
        pages[WAW] = jj.Page(p1.flight.replace('"count":7', '"count":99'), [])
        pages[jj.page_url(WAW, 2)] = pages[WAW]
        fake = FakeFetcher(pages)
        code, stats = bulk.run(self.args(), fake, [WAW], set())
        self.assertEqual(code, 0)
        self.assertEqual(stats["listingPages"], 2)
        self.assertNotIn(jj.page_url(WAW, 3), fake.calls)

    def test_page_past_end_stops_pagination(self):
        p1 = page("listing_warszawa_page1.html")
        pages = self.pages(**{WAW: jj.Page(p1.flight.replace('"count":7', '"count":99'), []),
                              jj.page_url(WAW, 2): "listing_past_last_page.html"})
        code, stats = bulk.run(self.args(), FakeFetcher(pages), [WAW], set())
        self.assertEqual((code, stats["listingPages"], stats["unique"]), (0, 2, 4))

    def test_deadline_saves_partial(self):
        fake = FakeFetcher(self.pages())
        clock = lambda: 10_000 if sum("/job-offer/" in c for c in fake.calls) >= 2 else 0  # noqa: E731
        with mock.patch.object(bulk.time, "monotonic", side_effect=clock):
            code, stats = bulk.run(self.args(), fake, [WAW], set())
        self.assertEqual(code, 0)
        self.assertTrue(stats["timedOut"])
        self.assertEqual((stats["status"], stats["saved"]), ("PARTIAL", 2))


class InputFilesTest(unittest.TestCase):

    def write(self, data):
        fd, path = tempfile.mkstemp(suffix=".json")
        with os.fdopen(fd, "w", encoding="utf-8") as f:
            json.dump(data, f)
        return path

    def test_urls_file_and_known_urls(self):
        args = bulk.parse_args(["--urls-file", self.write([LODZ]),
                                "--known-urls", self.write([jj.offer_url(EMAGINE), "https://nofluffjobs.com/pl/job/x"])])
        urls, known = bulk.resolve_inputs(args)
        self.assertEqual((urls, known), ([LODZ], {EMAGINE}))

    def test_without_files_defaults_are_used(self):
        urls, known = bulk.resolve_inputs(bulk.parse_args([]))
        self.assertEqual((urls, known), (bulk.SEARCH_URLS, set()))

    def test_invalid_input_stops_before_any_request(self):
        for data in ([], ["https://theprotocol.it/filtry/java;t"], {"a": 1}):
            path = self.write(data)
            with mock.patch.object(bulk, "Fetcher") as fetcher, redirect_stdout(io.StringIO()) as out:
                code = bulk.main(["--urls-file", path])
            self.assertEqual(code, jj.EXIT_USAGE, msg=data)
            fetcher.assert_not_called()
            self.assertEqual(json.loads(out.getvalue())["status"], "FAILED")


class FetchRetryTest(unittest.TestCase):

    class Resp:
        def __init__(self, status, text="ok", headers=None):
            self.status_code, self.text, self.headers, self.encoding = status, text, headers or {}, None

    def session(self, *responses):
        s = mock.Mock()
        s.get.side_effect = list(responses)
        return s

    def test_retries_transient_then_succeeds(self):
        s = self.session(self.Resp(503), self.Resp(200, "<html>"))
        self.assertEqual(jj.fetch_html(s, "u", sleep=lambda _: None), "<html>")

    def test_gives_up_after_max_attempts(self):
        s = self.session(*[self.Resp(502)] * jj.MAX_ATTEMPTS)
        with self.assertRaises(jj.NetworkError):
            jj.fetch_html(s, "u", sleep=lambda _: None)

    def test_not_found_and_challenge_are_not_retried(self):
        with self.assertRaises(jj.NotFoundError):
            jj.fetch_html(self.session(self.Resp(404)), "u", sleep=lambda _: None)
        s = self.session(self.Resp(403, raw("cloudflare_challenge.html")))
        with self.assertRaises(jj.BlockedError):
            jj.fetch_html(s, "u", sleep=lambda _: None)
        self.assertEqual(s.get.call_count, 1)


class SingleScriptTest(unittest.TestCase):

    def test_invalid_url_exit_code_and_empty_stdout(self):
        with redirect_stdout(io.StringIO()) as out:
            code = single.main(["x", "https://theprotocol.it/szczegoly/praca/x,oferta,1"])
        self.assertEqual((code, out.getvalue()), (jj.EXIT_USAGE, ""))

    def test_success_prints_exactly_one_json_line(self):
        session = mock.Mock()
        session.get.return_value = FetchRetryTest.Resp(200, raw("offer_multilocation_no_salary.html"))
        with mock.patch.object(jj, "create_session", return_value=session), redirect_stdout(io.StringIO()) as out:
            code = single.main(["x", f"https://justjoin.it/job-offer/{IT_FACTORY_LODZ}"])
        self.assertEqual(code, 0)
        lines = out.getvalue().strip().splitlines()
        self.assertEqual(len(lines), 1)
        offer = json.loads(lines[0])
        self.assertEqual(offer["offerUrl"], jj.offer_url(IT_FACTORY_WAW))
        self.assertRegex(offer["fetchedAt"], r"^\d{4}-\d\d-\d\dT\d\d:\d\d:\d\d\.\d{3}Z$")
        self.assertFalse(any(k.startswith("_") for k in offer))

    def test_removed_offer_exit_code(self):
        session = mock.Mock()
        session.get.return_value = FetchRetryTest.Resp(404, "<html>")
        with mock.patch.object(jj, "create_session", return_value=session), redirect_stdout(io.StringIO()) as out:
            code = single.main(["x", f"https://justjoin.it/job-offer/{EMAGINE}"])
        self.assertEqual((code, out.getvalue()), (jj.EXIT_NOT_FOUND, ""))


if __name__ == "__main__":
    unittest.main()
