"""Tests for the theprotocol.it scrapers.

Fixtures in fixtures/theprotocol/ are real pages from theprotocol.it
(2026-10-11) trimmed to their __NEXT_DATA__ JSON. Variants that did not occur
in the sample (no requirements, changed structure, abroad) are derived from
them in the tests below.

Run: python -m unittest discover -s src/test/python -v
"""
import copy
import json
import os
import sys
import tempfile
import types
import unittest
from datetime import datetime, timezone
from unittest import mock

HERE = os.path.dirname(os.path.abspath(__file__))
SCRIPTS = os.path.join(HERE, "..", "..", "main", "resources", "scripts", "theprotocol")
sys.path.insert(0, os.path.abspath(SCRIPTS))

import theprotocol_common as tp  # noqa: E402
import scrape_theprotocol as bulk  # noqa: E402
import scrape_theprotocol_offer as single  # noqa: E402

FIX = os.path.join(HERE, "fixtures", "theprotocol")
FIXED_NOW = datetime(2026, 10, 11, 12, 0, 0, 123456, tzinfo=timezone.utc)


def fixture_html(name):
    with open(os.path.join(FIX, name), encoding="utf-8") as f:
        return f.read()


def fixture_data(name):
    return tp.extract_next_data(fixture_html(name))


JUNIOR = "offer_junior_salary_remote.html"
MULTI = "offer_multilocation_no_salary_en.html"


class OfferParsingTest(unittest.TestCase):

    def test_junior_offer_with_salary_and_remote(self):
        o = tp.parse_offer(fixture_data(JUNIOR), fetched_at=FIXED_NOW)
        self.assertEqual(o["title"], "Fullstack Java Developer (React / Angular) (F/M)")
        self.assertEqual(o["company"], "B2B.NET S.A.")
        self.assertEqual(o["level"], "Junior")
        self.assertEqual(o["salaryRange"], "70 – 90 PLN netto (+VAT)/godz. (B2B)")
        self.assertEqual(o["location"], "Warszawa, praca zdalna/praca hybrydowa")
        self.assertEqual(o["source"], "TheProtocol")
        self.assertTrue(o["offerUrl"].startswith("https://theprotocol.it/szczegoly/praca/fullstack-java-developer"))
        self.assertEqual(o["requirements"][:3], ["Java", "Spring Boot", "Spring Framework"])
        self.assertEqual(o["niceToHave"], ["Docker", "Jenkins", "Apache Kafka"])
        self.assertTrue(o["_inPoland"])

    def test_description_contains_job_sections_but_not_company_blurb(self):
        o = tp.parse_offer(fixture_data(JUNIOR))
        d = o["description"]
        self.assertIn("O projekcie\n", d)
        self.assertIn("Zakres obowiązków\n- ", d)
        self.assertIn("Wymagania\n- Minimum 3–4 lata", d)
        self.assertNotIn("B2Bnetwork jest polską firmą", d)  # about-us section skipped

    def test_benefits_are_not_requirements(self):
        o = tp.parse_offer(fixture_data(JUNIOR))
        self.assertNotIn("elastyczny czas pracy", o["requirements"])
        self.assertNotIn("elastyczny czas pracy", o["niceToHave"])

    def test_multilocation_offer_without_salary(self):
        o = tp.parse_offer(fixture_data(MULTI))
        self.assertEqual(o["salaryRange"], "Brak wynagrodzenia")
        self.assertEqual(o["level"], "Mid")  # mid + senior -> lowest wins
        self.assertTrue(o["location"].startswith("Warszawa, Białystok, Bydgoszcz"))
        self.assertIn("Łódź", o["location"])
        self.assertIn("warszawa", o["offerUrl"])  # canonical partition prefers Warsaw
        self.assertLessEqual(len(o["location"]), 255)

    def test_any_location_variant_maps_to_same_canonical_url(self):
        data = fixture_data(MULTI)
        first = tp.parse_offer(data)["offerUrl"]
        shuffled = copy.deepcopy(data)
        shuffled["props"]["pageProps"]["offer"]["partitions"].reverse()
        shuffled["props"]["pageProps"]["offer"]["offerUrlName"] = "something-else,oferta,01000000-0000-0000-0000-000000000001"
        self.assertEqual(tp.parse_offer(shuffled)["offerUrl"], first)

    def test_fetched_at_formats_match_java_patterns(self):
        self.assertEqual(tp.format_fetched_at(FIXED_NOW, 6), "2026-10-11T12:00:00.123456Z")
        self.assertEqual(tp.format_fetched_at(FIXED_NOW, 3), "2026-10-11T12:00:00.123Z")

    def test_offer_without_technologies_has_empty_requirements(self):
        data = fixture_data(JUNIOR)
        data["props"]["pageProps"]["offer"]["technologies"] = {"expected": [], "optional": []}
        o = tp.parse_offer(data)
        self.assertEqual(o["requirements"], [])
        self.assertEqual(o["niceToHave"], [])
        tp.validate_offer(o)

    def test_offer_without_company_is_rejected(self):
        data = fixture_data(JUNIOR)
        data["props"]["pageProps"]["offer"]["attributes"]["employer"] = {"name": ""}
        with self.assertRaises(tp.StructureError):
            tp.parse_offer(data)

    def test_offer_without_location_and_remote_only(self):
        data = fixture_data(JUNIOR)
        off = data["props"]["pageProps"]["offer"]
        off["partitions"] = []
        off["attributes"]["workplaces"] = []
        off["attributes"]["employment"]["detailedWorkModes"] = [{"code": "home-office"}]
        o = tp.parse_offer(data)
        self.assertEqual(o["location"], "Zdalnie")

    def test_abroad_offer_is_flagged(self):
        data = fixture_data(JUNIOR)
        data["props"]["pageProps"]["offer"]["attributes"]["workplaces"][0]["country"] = "Niemcy"
        self.assertFalse(tp.parse_offer(data)["_inPoland"])

    def test_removed_offer_raises_not_found(self):
        with self.assertRaises(tp.NotFoundError):
            tp.parse_offer(fixture_data("offer_missing.html"))

    def test_inactive_offer_raises_not_found(self):
        data = fixture_data(JUNIOR)
        data["props"]["pageProps"]["offer"]["publicationDetails"]["isActive"] = False
        with self.assertRaises(tp.NotFoundError):
            tp.parse_offer(data)

    def test_changed_structure_raises_structure_error(self):
        with self.assertRaises(tp.StructureError):
            tp.extract_next_data("<html><body>nowy wygląd strony</body></html>")
        with self.assertRaises(tp.StructureError):
            tp.parse_offer({"props": {"pageProps": {"somethingElse": 1}}})

    def test_challenge_page_raises_blocked(self):
        with self.assertRaises(tp.BlockedError):
            tp.extract_next_data(fixture_html("cloudflare_challenge.html"))


class SalaryAndLevelTest(unittest.TestCase):

    def test_multiple_contracts_keep_their_own_units(self):
        contracts = [
            {"name": "umowa o pracę", "salary": {"from": 12000, "to": 18000, "currencyCode": "zł",
                                                  "timeUnitId": 0, "kindCode": "brutto"}},
            {"name": "B2B contract", "salary": {"from": 120, "to": 150, "currencyCode": "zł",
                                                "timeUnitId": 1, "kindCode": "net (+ VAT)"}},
        ]
        self.assertEqual(tp.format_salary(contracts),
                         "12 000 – 18 000 PLN brutto/mies. (UoP) | 120 – 150 PLN netto (+VAT)/godz. (B2B)")

    def test_open_ended_salary(self):
        c = [{"name": "kontrakt B2B", "salary": {"from": 15000, "to": None, "currencyCode": "zł",
                                                 "timeUnitId": 0, "kindCode": "netto (+ VAT)"}}]
        self.assertEqual(tp.format_salary(c), "od 15 000 PLN netto (+VAT)/mies. (B2B)")

    def test_levels(self):
        self.assertEqual(tp.map_level(["trainee"]), "Trainee")
        self.assertEqual(tp.map_level(["assistant", "junior"]), "Trainee")
        self.assertEqual(tp.map_level(["junior", "mid"]), "Junior")
        self.assertEqual(tp.map_level(["senior", "expert"]), "Senior")
        self.assertEqual(tp.map_level([]), "Nieokreślony poziom")
        self.assertEqual(tp.map_level(["manager"]), "Manager")


class UrlValidationTest(unittest.TestCase):

    def test_accepts_and_canonicalizes(self):
        u = "http://www.theprotocol.it/szczegoly/praca/java-dev-warszawa,oferta,01000000-183e-46c7-5d79-08df154cbe3d?s=abc#x"
        self.assertEqual(tp.normalize_offer_url(u),
                         "https://theprotocol.it/szczegoly/praca/java-dev-warszawa,oferta,01000000-183e-46c7-5d79-08df154cbe3d")

    def test_rejects_foreign_or_malformed(self):
        for bad in ["https://evil.example/szczegoly/praca/x,oferta,01000000-183e-46c7-5d79-08df154cbe3d",
                    "https://theprotocol.it/filtry/java;t",
                    "ftp://theprotocol.it/szczegoly/praca/x,oferta,01000000-183e-46c7-5d79-08df154cbe3d",
                    "https://theprotocol.it.evil.com/szczegoly/praca/x,oferta,01000000-183e-46c7-5d79-08df154cbe3d",
                    "", "nie-url"]:
            with self.assertRaises(ValueError, msg=bad):
                tp.normalize_offer_url(bad)


class ListingTest(unittest.TestCase):

    def test_parse_listing_page(self):
        offers, number, count, total = tp.parse_listing(fixture_data("listing_page1.html"))
        self.assertEqual((number, count, total), (1, 4, 199))
        self.assertEqual(len(offers), 6)
        self.assertTrue(all(o["groupId"] and o["offerUrlName"] for o in offers))

    def test_listing_without_offers_is_structure_error(self):
        with self.assertRaises(tp.StructureError):
            tp.parse_listing({"props": {"pageProps": {}}})

    def test_level_filter(self):
        self.assertTrue(tp.is_wanted_level(["mid", "senior"]))
        self.assertTrue(tp.is_wanted_level([]))
        self.assertFalse(tp.is_wanted_level(["senior", "expert"]))


# ── Bulk scraper with a fake fetcher ─────────────────────────────────────────

def listing(offers, number, count, total=None):
    return {"props": {"pageProps": {"offersResponse": {
        "offers": offers, "offersCount": total if total is not None else len(offers),
        "page": {"number": number, "size": 50, "count": count}}}}}


def entry(i, group=None, levels=("mid",)):
    return {"id": f"id-{i}", "groupId": group or f"g-{i}",
            "offerUrlName": f"oferta-{i},oferta,01000000-0000-0000-0000-{i:012d}",
            "positionLevels": [{"value": lv} for lv in levels], "workplace": [{"city": "Warszawa"}]}


def detail(i, country="Polska"):
    data = fixture_data(JUNIOR)
    off = data["props"]["pageProps"]["offer"]
    name = f"oferta-{i},oferta,01000000-0000-0000-0000-{i:012d}"
    off["offerUrlName"] = name
    off["partitions"] = [{"id": f"p-{i}", "url": name, "workplaces": [{"location": "Warszawa"}]}]
    off["attributes"]["title"]["value"] = f"Java Developer {i}"
    off["attributes"]["workplaces"][0]["country"] = country
    return data


class FakeFetcher:
    def __init__(self, pages):
        self.pages = pages
        self.calls = []

    def next_data(self, url):
        self.calls.append(url)
        value = self.pages[url]
        if isinstance(value, Exception):
            raise value
        return copy.deepcopy(value)

    def close(self):
        pass


def detail_url(i):
    return tp.offer_url_from_name(f"oferta-{i},oferta,01000000-0000-0000-0000-{i:012d}")


class BulkScraperTest(unittest.TestCase):

    def setUp(self):
        self.tmp = tempfile.mkdtemp()
        self.out = os.path.join(self.tmp, "theprotocol_offers.json")
        self.s1 = "https://theprotocol.it/filtry/java;t/a;p"
        self.s2 = "https://theprotocol.it/filtry/java;t/a;p/warszawa;wp"

    def args(self, **kw):
        base = dict(output=self.out, limit=None, delay=0, max_pages=10, max_minutes=5, url=[self.s1, self.s2])
        base.update(kw)
        return types.SimpleNamespace(**base)

    def pages(self, **overrides):
        p = {
            self.s1: listing([entry(1), entry(2), entry(3, levels=("senior",))], 1, 2, total=4),
            self.s1 + "?pageNumber=2": listing([entry(4)], 2, 2, total=4),
            # Warsaw search returns another location variant of group g-1 and a new group
            self.s2: listing([entry(11, group="g-1"), entry(5)], 1, 1),
            detail_url(1): detail(1), detail_url(2): detail(2), detail_url(4): detail(4),
            detail_url(5): detail(5, country="Niemcy"),
        }
        p.update(overrides)
        return p

    def test_full_run_paginates_dedupes_filters_and_saves(self):
        fake = FakeFetcher(self.pages())
        code, stats = bulk.run(self.args(), fake)
        self.assertEqual(code, 0)
        self.assertEqual(stats["status"], "SUCCESS")
        self.assertEqual(stats["listingPages"], 3)
        self.assertEqual(stats["unique"], 4)          # g-1, g-2, g-4, g-5 (g-3 senior skipped, g-1 dup)
        self.assertEqual(stats["skippedLevel"], 1)
        self.assertEqual(stats["skippedAbroad"], 1)
        self.assertEqual(stats["saved"], 3)
        self.assertNotIn(detail_url(3), fake.calls)    # senior never opened
        self.assertNotIn(detail_url(11), fake.calls)   # duplicate group never opened
        with open(self.out, encoding="utf-8") as f:
            saved = json.load(f)
        self.assertEqual([o["title"] for o in saved], ["Java Developer 1", "Java Developer 2", "Java Developer 4"])
        self.assertTrue(all(o["source"] == "TheProtocol" and "_inPoland" not in o for o in saved))

    def test_limit_mode(self):
        code, stats = bulk.run(self.args(limit=1), FakeFetcher(self.pages()))
        self.assertEqual((code, stats["saved"]), (0, 1))

    def test_blocked_run_keeps_previous_file(self):
        with open(self.out, "w", encoding="utf-8") as f:
            json.dump([{"title": "poprzednie"}], f)
        pages = self.pages(**{detail_url(2): tp.BlockedError("Cloudflare")})
        code, stats = bulk.run(self.args(), FakeFetcher(pages))
        self.assertEqual(code, tp.EXIT_BLOCKED)
        self.assertEqual(stats["status"], "FAILED")
        with open(self.out, encoding="utf-8") as f:
            self.assertEqual(json.load(f), [{"title": "poprzednie"}])

    def test_single_bad_offer_gives_partial_result(self):
        pages = self.pages(**{detail_url(2): tp.StructureError("zmiana"), detail_url(4): tp.NotFoundError("usunięta")})
        code, stats = bulk.run(self.args(), FakeFetcher(pages))
        self.assertEqual((code, stats["status"], stats["saved"], stats["errors"], stats["notFound"]),
                         (0, "PARTIAL", 1, 1, 1))

    def test_all_details_failing_does_not_overwrite(self):
        with open(self.out, "w", encoding="utf-8") as f:
            f.write("[]")
        failing = {detail_url(i): tp.NetworkError("timeout") for i in (1, 2, 4, 5)}
        code, stats = bulk.run(self.args(), FakeFetcher(self.pages(**failing)))
        self.assertEqual(code, tp.EXIT_NETWORK)
        self.assertEqual(stats["saved"], 0)

    def test_genuinely_empty_search_writes_empty_list(self):
        pages = {self.s1: listing([], 1, 1), self.s2: listing([], 1, 1)}
        code, stats = bulk.run(self.args(), FakeFetcher(pages))
        self.assertEqual((code, stats["status"], stats["saved"]), (0, "SUCCESS", 0))
        with open(self.out, encoding="utf-8") as f:
            self.assertEqual(json.load(f), [])

    def test_repeated_page_stops_pagination(self):
        same = listing([entry(1)], 1, 99)
        pages = {self.s1: same, self.s1 + "?pageNumber=2": same, detail_url(1): detail(1)}
        fake = FakeFetcher(pages)
        code, stats = bulk.run(self.args(url=[self.s1]), fake)
        self.assertEqual(code, 0)
        self.assertNotIn(self.s1 + "?pageNumber=3", fake.calls)

    def test_deadline_saves_partial(self):
        fake = FakeFetcher(self.pages())
        with mock.patch.object(bulk.time, "monotonic", side_effect=[0, 0, 0, 0, 0, 0, 999, 999, 999, 999]):
            code, stats = bulk.run(self.args(max_minutes=1), fake)
        self.assertEqual(code, 0)
        self.assertTrue(stats["timedOut"])
        self.assertEqual(stats["status"], "PARTIAL")


class FetchRetryTest(unittest.TestCase):

    class Resp:
        def __init__(self, status, text="", headers=None):
            self.status_code, self.text, self.headers, self.encoding = status, text, headers or {}, None

    def session(self, *responses):
        s = mock.Mock()
        s.get.side_effect = list(responses)
        return s

    def test_retries_transient_then_succeeds(self):
        s = self.session(self.Resp(503), self.Resp(200, "ok"))
        self.assertEqual(tp.fetch_html(s, "u", sleep=lambda _: None), "ok")
        self.assertEqual(s.get.call_count, 2)

    def test_gives_up_after_max_attempts(self):
        s = self.session(*[self.Resp(502)] * tp.MAX_ATTEMPTS)
        with self.assertRaises(tp.NetworkError):
            tp.fetch_html(s, "u", sleep=lambda _: None)

    def test_challenge_is_not_retried(self):
        s = self.session(self.Resp(403, "x", {"cf-mitigated": "challenge"}))
        with self.assertRaises(tp.BlockedError):
            tp.fetch_html(s, "u", sleep=lambda _: None)
        self.assertEqual(s.get.call_count, 1)

    def test_respects_retry_after(self):
        waits = []
        s = self.session(self.Resp(429, "", {"Retry-After": "7"}), self.Resp(200, "ok"))
        tp.fetch_html(s, "u", sleep=waits.append)
        self.assertEqual(waits, [7])


class SingleScriptTest(unittest.TestCase):

    def test_invalid_url_exit_code_and_empty_stdout(self):
        with mock.patch("sys.stdout") as out:
            code = single.main(["x", "https://example.com/job/1"])
        self.assertEqual(code, tp.EXIT_USAGE)
        out.write.assert_not_called()

    def test_success_prints_exactly_one_json_line(self):
        printed = []
        with mock.patch.object(tp, "fetch_html", return_value=fixture_html(JUNIOR)), \
                mock.patch("builtins.print", side_effect=lambda *a, **k: printed.append(a[0])):
            code = single.main(["x", "https://theprotocol.it/szczegoly/praca/fullstack-java-developer-react---angular-f-m-warszawa,oferta,01000000-183e-46c7-5d79-08df154cbe3d"])
        self.assertEqual(code, 0)
        self.assertEqual(len(printed), 1)
        o = json.loads(printed[0])
        self.assertNotIn("\n", printed[0])
        self.assertRegex(o["fetchedAt"], r"^\d{4}-\d\d-\d\dT\d\d:\d\d:\d\d\.\d{3}Z$")
        self.assertEqual(set(o), {"title", "description", "location", "salaryRange", "level", "fetchedAt",
                                  "offerUrl", "company", "requirements", "niceToHave", "source"})

    def test_removed_offer_exit_code(self):
        with mock.patch.object(tp, "fetch_html", return_value=fixture_html("offer_missing.html")), \
                mock.patch("builtins.print") as p:
            code = single.main(["x", "https://theprotocol.it/szczegoly/praca/x,oferta,01000000-0000-0000-0000-000000000000"])
        self.assertEqual(code, tp.EXIT_NOT_FOUND)
        p.assert_not_called()


if __name__ == "__main__":
    unittest.main()
