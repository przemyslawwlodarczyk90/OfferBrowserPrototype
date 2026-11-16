"""--urls-file: every bulk scraper takes its search URLs from the admin panel
(database → JSON file prepared by Java) instead of the hard-coded list.

Run: python -m unittest discover -s src/test/python -v
"""
import importlib.util
import io
import json
import os
import sys
import tempfile
import unittest
from contextlib import redirect_stdout
from unittest import mock

HERE = os.path.dirname(os.path.abspath(__file__))
SCRIPTS = os.path.abspath(os.path.join(HERE, "..", "..", "main", "resources", "scripts"))


def load(folder, module):
    """Import a script from its portal folder (same-named helpers stay separate)."""
    path = os.path.join(SCRIPTS, folder)
    if path not in sys.path:
        sys.path.insert(0, path)
    spec = importlib.util.spec_from_file_location(f"{folder}_{module}", os.path.join(path, module + ".py"))
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    return mod


nf = load("nofluff", "scraper")
tp_bulk = load("theprotocol", "scrape_theprotocol")


def write(data):
    fd, path = tempfile.mkstemp(suffix=".json")
    with os.fdopen(fd, "w", encoding="utf-8") as f:
        json.dump(data, f)
    return path


class NoFluffUrlsFileTest(unittest.TestCase):

    def test_admin_urls_are_used_instead_of_defaults(self):
        urls = ["https://nofluffjobs.com/pl/lodz/Java?criteria=seniority%3Djunior"]
        with mock.patch.object(nf, "create_driver"), \
                mock.patch.object(nf, "run", return_value=nf.EXIT_OK) as run, redirect_stdout(io.StringIO()):
            code = nf.main(["--urls-file", write(urls)])
        self.assertEqual(code, nf.EXIT_OK)
        self.assertEqual(run.call_args.kwargs["urls"], urls)

    def test_without_file_defaults_are_used(self):
        with mock.patch.object(nf, "create_driver"), \
                mock.patch.object(nf, "run", return_value=nf.EXIT_OK) as run, redirect_stdout(io.StringIO()):
            nf.main([])
        self.assertEqual(run.call_args.kwargs["urls"], nf.SORTED_URLS)

    def test_empty_or_broken_file_stops_before_opening_the_browser(self):
        for data in ([], {"a": 1}, [""]):
            with mock.patch.object(nf, "create_driver") as driver, redirect_stdout(io.StringIO()) as out:
                code = nf.main(["--urls-file", write(data)])
            self.assertEqual(code, nf.EXIT_KNOWN_URLS, msg=data)
            driver.assert_not_called()
            self.assertEqual(json.loads(out.getvalue())["status"], "FAILED")


class TheProtocolUrlsFileTest(unittest.TestCase):

    def test_admin_urls_are_used(self):
        urls = ["https://theprotocol.it/filtry/java;t/junior;p/lodz;wp"]
        with mock.patch.object(tp_bulk, "Fetcher"), \
                mock.patch.object(tp_bulk, "run", return_value=(0, {})) as run, redirect_stdout(io.StringIO()):
            tp_bulk.main(["--urls-file", write(urls)])
        self.assertEqual(run.call_args.args[0].url, urls)

    def test_empty_file_stops_before_any_request(self):
        with mock.patch.object(tp_bulk, "Fetcher") as fetcher, redirect_stdout(io.StringIO()) as out:
            code = tp_bulk.main(["--urls-file", write([])])
        self.assertEqual(code, tp_bulk.tp.EXIT_USAGE)
        fetcher.assert_not_called()
        self.assertEqual(json.loads(out.getvalue())["status"], "FAILED")


if __name__ == "__main__":
    unittest.main()
