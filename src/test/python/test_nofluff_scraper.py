"""Tests for the No Fluff Jobs bulk scraper (scraper.py): offers already in the
database and duplicates between search lists must be skipped BEFORE the detail
page is opened. A fake WebDriver counts every navigation; no network, no Chrome.

Run: python -m unittest discover -s src/test/python -v
"""
import json
import os
import sys
import tempfile
import unittest
from unittest import mock

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.abspath(os.path.join(HERE, "..", "..", "main", "resources", "scripts", "nofluff")))

import scraper  # noqa: E402

scraper.WAIT_SECONDS = 0.2  # empty fake lists must not wait 30 s
from selenium.common.exceptions import NoSuchElementException  # noqa: E402


class FakeElement:
    def __init__(self, href=None, title=None, text=""):
        self.href, self.title, self.text = href, title, text

    def get_attribute(self, name):
        return self.href if name == "href" else None

    def find_element(self, by, xpath):
        if "posting-title" in xpath and self.title is not None:
            return FakeElement(text=self.title)
        raise NoSuchElementException(xpath)


class FakeDriver:
    """Search pages return cards; any other URL is treated as an offer detail page."""

    def __init__(self, listings, failing_details=()):
        self.listings = listings            # search url -> list of offer hrefs
        self.failing = set(failing_details)
        self.current = None
        self.title = "Praca Java"
        self.list_gets, self.detail_gets = [], []

    def get(self, url):
        self.current = url
        if url in self.listings:
            self.list_gets.append(url)
        else:
            self.detail_gets.append(url)
            if url in self.failing:
                raise RuntimeError("timeout")

    def find_elements(self, by, xpath):
        if self.current in self.listings and "posting-list-item" in xpath:
            return [FakeElement(href=h, title=f"Java Developer {i}") for i, h in enumerate(self.listings[self.current])]
        return []

    def find_element(self, by, xpath):
        if xpath == "body":
            return FakeElement()
        if "postingCompanyUrl" in xpath:
            return FakeElement(text="ACME")
        raise NoSuchElementException(xpath)


def offer(i):
    return f"https://nofluffjobs.com/pl/job/java-developer-acme-warszawa-{i}"


SEARCH = ["https://nofluffjobs.com/pl/Java?sort=default", "https://nofluffjobs.com/pl/warszawa/Java?sort=newest"]


class NoFluffScraperTest(unittest.TestCase):

    def run_scraper(self, driver, known, urls=SEARCH):
        saved = []
        stats = scraper.new_stats()
        code = scraper.run(driver, {scraper.normalize_url(u) for u in known}, stats, urls=urls, save=saved.extend)
        return code, stats, saved

    def test_empty_database_all_offers_are_new(self):
        driver = FakeDriver({SEARCH[0]: [offer(i) for i in range(10)], SEARCH[1]: []})
        code, stats, saved = self.run_scraper(driver, known=[])
        self.assertEqual(code, 0)
        self.assertEqual(len(driver.detail_gets), 10)
        self.assertEqual((stats["new"], stats["detailPagesOpened"], stats["saved"]), (10, 10, 10))

    def test_70_of_100_in_database_opens_only_30(self):
        driver = FakeDriver({SEARCH[0]: [offer(i) for i in range(100)], SEARCH[1]: []})
        code, stats, saved = self.run_scraper(driver, known=[offer(i) for i in range(70)])
        self.assertEqual(len(driver.detail_gets), 30)
        self.assertEqual(set(driver.detail_gets), {offer(i) for i in range(70, 100)})
        self.assertEqual((stats["unique"], stats["alreadyInDatabase"], stats["new"]), (100, 70, 30))
        self.assertEqual(len(saved), 30)

    def test_all_in_database_opens_no_details(self):
        driver = FakeDriver({SEARCH[0]: [offer(i) for i in range(100)], SEARCH[1]: [offer(i) for i in range(50)]})
        code, stats, saved = self.run_scraper(driver, known=[offer(i) for i in range(100)])
        self.assertEqual(code, 0)
        self.assertEqual(driver.detail_gets, [])
        self.assertEqual(stats["status"], "SUCCESS")
        self.assertEqual(saved, [])

    def test_offer_on_five_lists_is_opened_once(self):
        lists = {f"https://nofluffjobs.com/pl/Java?v={v}": [offer(1)] for v in range(5)}
        driver = FakeDriver(lists)
        code, stats, saved = self.run_scraper(driver, known=[], urls=list(lists))
        self.assertEqual(driver.detail_gets, [offer(1)])
        self.assertEqual((stats["found"], stats["unique"], stats["duplicatesBetweenLists"]), (5, 1, 4))

    def test_url_variants_are_matched_with_database(self):
        variants = [offer(1) + "?searchTitle=java#apply", offer(2).replace("https://", "https://www.") + "/",
                    offer(3).upper().replace("HTTPS://NOFLUFFJOBS.COM", "https://nofluffjobs.com")]
        driver = FakeDriver({SEARCH[0]: variants, SEARCH[1]: []})
        self.run_scraper(driver, known=[offer(1), offer(2), offer(3)])
        self.assertEqual(driver.detail_gets, [])

    def test_list_navigation_is_not_repeated_after_each_offer(self):
        driver = FakeDriver({SEARCH[0]: [offer(i) for i in range(5)], SEARCH[1]: [offer(9)]})
        self.run_scraper(driver, known=[])
        self.assertEqual(driver.list_gets, SEARCH)  # each list loaded once, no driver.back() reloads

    def test_failed_detail_is_counted_and_others_continue(self):
        driver = FakeDriver({SEARCH[0]: [offer(1), offer(2), offer(3)], SEARCH[1]: []}, failing_details={offer(2)})
        code, stats, saved = self.run_scraper(driver, known=[])
        self.assertEqual((stats["parsed"], stats["failed"], stats["status"]), (2, 1, "PARTIAL"))
        self.assertEqual([o["offerUrl"] for o in saved], [offer(1), offer(3)])

    def test_output_keeps_the_existing_json_contract(self):
        driver = FakeDriver({SEARCH[0]: [offer(1)], SEARCH[1]: []})
        _, _, saved = self.run_scraper(driver, known=[])
        self.assertEqual(set(saved[0]), {"title", "description", "location", "salaryRange", "level",
                                         "fetchedAt", "offerUrl", "company", "requirements", "source"})
        self.assertEqual(saved[0]["source"], "NoFluff")
        self.assertRegex(saved[0]["fetchedAt"], r"^\d{4}-\d\d-\d\dT\d\d:\d\d:\d\d\.\d{6}Z$")

    def test_all_list_pages_failing_is_an_error_without_saving(self):
        class Broken(FakeDriver):
            def get(self, url):
                raise RuntimeError("no network")
        saved = []
        stats = scraper.new_stats()
        code = scraper.run(Broken({}), set(), stats, urls=SEARCH, save=saved.extend)
        self.assertNotEqual(code, 0)
        self.assertEqual(saved, [])


class BlockedTest(unittest.TestCase):

    def test_block_page_stops_after_first_list_without_saving(self):
        class Blocked(FakeDriver):
            def get(self, url):
                super().get(url)
                self.title = "ERROR: The request could not be satisfied"
        driver = Blocked({SEARCH[0]: [offer(1)], SEARCH[1]: [offer(2)]})
        saved = []
        stats = scraper.new_stats()
        code = scraper.run(driver, set(), stats, urls=SEARCH, save=saved.extend)
        self.assertEqual(code, scraper.EXIT_BLOCKED)
        self.assertEqual(driver.list_gets, [SEARCH[0]])   # kolejne listy nie są odpytywane
        self.assertEqual(driver.detail_gets, [])
        self.assertEqual(saved, [])
        self.assertIn("blokady", stats["message"])


class KnownUrlsFileTest(unittest.TestCase):

    def test_unreadable_known_urls_aborts_before_opening_a_browser(self):
        with mock.patch.object(scraper, "create_driver") as create, mock.patch("builtins.print") as out:
            code = scraper.main(["--known-urls", os.path.join(tempfile.gettempdir(), "nie-ma-takiego-pliku.json")])
        self.assertEqual(code, scraper.EXIT_KNOWN_URLS)
        create.assert_not_called()
        self.assertIn("pobieranie przerwane", json.loads(out.call_args[0][0])["message"])

    def test_corrupted_known_urls_aborts(self):
        with tempfile.NamedTemporaryFile("w", suffix=".json", delete=False, encoding="utf-8") as f:
            f.write("{nie json")
        with mock.patch.object(scraper, "create_driver") as create, mock.patch("builtins.print"):
            code = scraper.main(["--known-urls", f.name])
        os.remove(f.name)
        self.assertEqual(code, scraper.EXIT_KNOWN_URLS)
        create.assert_not_called()

    def test_load_known_urls_normalizes(self):
        with tempfile.NamedTemporaryFile("w", suffix=".json", delete=False, encoding="utf-8") as f:
            json.dump([offer(1) + "?x=1", "https://example.com/job/1", None], f)
        known = scraper.load_known_urls(f.name)
        os.remove(f.name)
        self.assertEqual(known, {offer(1)})


class NormalizeUrlTest(unittest.TestCase):

    def test_normalization(self):
        n = scraper.normalize_url
        self.assertEqual(n("https://nofluffjobs.com/pl/job/abc-1"), "https://nofluffjobs.com/pl/job/abc-1")
        self.assertEqual(n("http://www.NoFluffJobs.com/pl/job/ABC-1/?utm_source=x#top"), "https://nofluffjobs.com/pl/job/abc-1")
        self.assertIsNone(n("https://theprotocol.it/szczegoly/praca/x"))
        self.assertIsNone(n("https://nofluffjobs.com.evil.com/pl/job/x"))
        self.assertIsNone(n(""))
        self.assertIsNone(n(None))

    def test_different_offers_with_same_title_stay_distinct(self):
        self.assertNotEqual(scraper.normalize_url(offer(1)), scraper.normalize_url(offer(2)))


if __name__ == "__main__":
    unittest.main()


class LevelTest(unittest.TestCase):

    def test_pick_level_takes_lowest_known(self):
        self.assertEqual(scraper.pick_level([" Senior "]), "Senior")
        self.assertEqual(scraper.pick_level(["Mid", "Senior"]), "Mid")
        self.assertEqual(scraper.pick_level(["Junior / Mid"]), "Junior")
        self.assertIsNone(scraper.pick_level(["", "coś innego"]))

    def test_read_level_from_posting_seniority(self):
        class D:
            def find_elements(self, by, xpath):
                return [FakeElement(text=" Mid "), FakeElement(text="Senior")] if "posting-seniority" in xpath else []
            def find_element(self, by, xpath):
                raise NoSuchElementException(xpath)
        self.assertEqual(scraper.read_level(D(), "//old"), "Mid")

    def test_read_level_falls_back_to_unknown(self):
        class D:
            def find_elements(self, by, xpath):
                return []
            def find_element(self, by, xpath):
                raise NoSuchElementException(xpath)
        self.assertEqual(scraper.read_level(D(), "//old"), "Nieokreślony poziom")


class LocationTest(unittest.TestCase):

    class D:
        def __init__(self, cities, pin):
            self.cities, self.pin = cities, pin

        def find_elements(self, by, xpath):
            if "popover-content" in xpath:
                return [FakeTextContent(c) for c in self.cities]
            if "location_pin" in xpath:
                return [FakeTextContent(p) for p in self.pin]
            return []

        def find_element(self, by, xpath):
            raise NoSuchElementException(xpath)

    def test_cities_from_hidden_popover_plus_work_mode(self):
        d = self.D([" Warszawa ", "Kraków"], ["Hybrydowo"])
        self.assertEqual(scraper.read_location(d, "//old"), "Warszawa, Kraków, praca hybrydowa")

    def test_single_city_shown_directly(self):
        self.assertEqual(scraper.read_location(self.D([], ["Łódź"]), "//old"), "Łódź")

    def test_remote_without_cities(self):
        self.assertEqual(scraper.read_location(self.D([], ["Zdalnie"]), "//old"), "Zdalnie")

    def test_nothing_found_is_unknown(self):
        self.assertEqual(scraper.read_location(self.D([], []), "//old"), "Nieokreślona")


class FakeTextContent(FakeElement):
    """Hidden element: Selenium .text is empty, textContent has the value."""

    def __init__(self, value):
        super().__init__(text="")
        self.value = value

    def get_attribute(self, name):
        return self.value if name == "textContent" else None
