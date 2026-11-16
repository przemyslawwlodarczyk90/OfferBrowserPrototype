from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from urllib.parse import urlsplit
import argparse
import json
import logging
import os
import sys
import tempfile
import time
from datetime import datetime, timezone

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

SORTED_URLS = [
    "https://nofluffjobs.com/pl/Java?sort=default",
    "https://nofluffjobs.com/pl/Java?sort=newest",
    "https://nofluffjobs.com/pl/Java?sort=salary-asc",
    "https://nofluffjobs.com/pl/Java?criteria=seniority%3Dtrainee,junior",
    "https://nofluffjobs.com/pl/warszawa/Java?criteria=seniority%3Dtrainee,junior",
    "https://nofluffjobs.com/pl/warszawa/Java?criteria=seniority%3Dmid",
    "https://nofluffjobs.com/pl/warszawa/Java?sort=newest",
    "https://nofluffjobs.com/pl/warszawa/Java?sort=salary-asc",
    "https://nofluffjobs.com/pl/lodz/Java?criteria=seniority%3Dtrainee,junior",
    "https://nofluffjobs.com/pl/lodz/Java?criteria=seniority%3Dmid",
    "https://nofluffjobs.com/pl/lodz/Java?sort=newest",
    "https://nofluffjobs.com/pl/lodz/Java?sort=salary-asc"
]

LIST_ITEM_XPATH = '//a[contains(@class, "posting-list-item")]'
WAIT_SECONDS = 30

EXIT_OK = 0
EXIT_UNEXPECTED = 1
EXIT_KNOWN_URLS = 2   # nie da się odczytać listy ofert z bazy / adresów wyszukiwania → nie pobieramy niczego
EXIT_BLOCKED = 3      # serwis odrzucił przeglądarkę (ochrona antybotowa) → przerywamy, bez obchodzenia

# Tytuły stron błędu/ochrony zamiast listy ofert (CloudFront, Cloudflare, WAF)
BLOCK_TITLE_MARKERS = ("the request could not be satisfied", "access denied", "just a moment",
                       "attention required", "cierpliwości")


class BlockedError(Exception):
    pass


def check_not_blocked(driver, url):
    title = (driver.title or "").lower()
    if any(marker in title for marker in BLOCK_TITLE_MARKERS):
        raise BlockedError(f"Serwis zwrócił stronę blokady zamiast wyników ({driver.title!r}) dla {url}")


def normalize_url(url):
    """Kanoniczna postać adresu oferty do porównań: https, host bez www, bez query,
    fragmentu i końcowego '/'. Ścieżka /pl/job/<slug> jednoznacznie identyfikuje ofertę.
    Zwraca None dla adresów spoza nofluffjobs.com."""
    if not url or not isinstance(url, str):
        return None
    parts = urlsplit(url.strip())
    host = (parts.hostname or "").lower()
    if host.startswith("www."):
        host = host[4:]
    if host != "nofluffjobs.com" or not parts.path:
        return None
    path = parts.path.rstrip("/").lower()
    return f"https://{host}{path}"


def load_search_urls(path):
    """Adresy list wyszukiwania z panelu administratora (plik JSON przygotowany przez Javę)."""
    with open(path, encoding="utf-8") as f:
        data = json.load(f)
    if not isinstance(data, list) or not all(isinstance(x, str) and x.strip() for x in data):
        raise ValueError("Plik z adresami wyszukiwania musi zawierać tablicę adresów")
    if not data:
        raise ValueError("Lista adresów wyszukiwania jest pusta — dodaj adresy w panelu administratora")
    return [x.strip() for x in data]


def load_known_urls(path):
    """Adresy ofert, które już są w bazie (plik JSON przygotowany przez Javę).
    Błąd odczytu przerywa scraping — nie zakładamy, że baza jest pusta."""
    with open(path, encoding="utf-8") as f:
        data = json.load(f)
    if not isinstance(data, list):
        raise ValueError("Plik z istniejącymi ofertami musi zawierać tablicę adresów")
    return {u for u in (normalize_url(x) for x in data) if u}


def create_driver():
    chrome_options = webdriver.ChromeOptions()
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--window-size=1920,1080")
    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument("--ignore-certificate-errors")
    chrome_options.add_argument('--disable-dev-shm-usage')

    service = Service(ChromeDriverManager().install())
    return webdriver.Chrome(service=service, options=chrome_options)


def discover_offers(driver, urls, stats):
    """Faza 1: tylko strony list. Zbiera adresy i tytuły z kart — bez otwierania szczegółów.
    Zwraca dict[znormalizowany URL → {url, title, foundIn}] (deduplikacja między listami)."""
    discovered = {}
    for url in urls:
        try:
            logging.info(f"Ładowanie strony: {url}")
            driver.get(url)
            stats["listPagesOpened"] += 1
            check_not_blocked(driver, url)

            wait = WebDriverWait(driver, WAIT_SECONDS)
            offer_elements = wait.until(
                EC.presence_of_all_elements_located((By.XPATH, LIST_ITEM_XPATH))
            )
            logging.info(f"Liczba znalezionych ofert: {len(offer_elements)}")

            for offer in offer_elements:
                try:
                    offer_url = offer.get_attribute('href')
                    key = normalize_url(offer_url)
                    if not key:
                        continue
                    stats["found"] += 1
                    if key in discovered:
                        discovered[key]["foundIn"].append(url)
                        stats["duplicatesBetweenLists"] += 1
                        continue
                    try:
                        title = offer.find_element(
                            By.XPATH, './/h3[contains(@class, "posting-title")]'
                        ).text or "Brak tytułu"
                    except Exception:
                        title = "Brak tytułu"
                    discovered[key] = {"url": offer_url, "title": title, "foundIn": [url]}
                except Exception as e:
                    logging.warning(f"Problem z kartą oferty na {url}: {e}")

        except BlockedError:
            raise  # nie odpytujemy kolejnych stron zablokowanego serwisu
        except Exception as e:
            stats["listPageErrors"] += 1
            logging.error(f"Błąd podczas ładowania strony {url}: {e}")
            continue  # Przejdź do następnego URL zamiast przerywać całość

    stats["unique"] = len(discovered)
    return discovered


# Poziomy od najniższego; przy kilku poziomach na ofercie (np. "Mid, Senior") bierzemy najniższy
LEVEL_ORDER = ["Trainee", "Junior", "Mid", "Senior", "Expert"]
SENIORITY_XPATH = '//*[@id="posting-seniority"]//span'


def pick_level(texts):
    """Z tekstów elementu poziomu wybiera najniższy znany poziom; None, gdy brak znanego."""
    found = []
    for text in texts:
        for part in (text or "").replace("/", ",").split(","):
            word = part.strip().lower()
            for level in LEVEL_ORDER:
                if word == level.lower():
                    found.append(level)
    return min(found, key=LEVEL_ORDER.index) if found else None


def read_level(driver, fallback_xpath=None):
    """Poziom z elementu #posting-seniority; stary selektor jako zapas."""
    try:
        level = pick_level([el.text for el in driver.find_elements(By.XPATH, SENIORITY_XPATH)])
        if level:
            return level
    except Exception:
        pass
    if fallback_xpath:
        try:
            return driver.find_element(By.XPATH, fallback_xpath).text or "Nieokreślony poziom"
        except Exception:
            pass
    return "Nieokreślony poziom"


# Lokalizacja: miasta są w (ukrytym) dymku listy lokalizacji, widoczny tekst to często tryb pracy
LOCATION_CITIES_XPATH = '//common-posting-locations//popover-content//li//span'
LOCATION_PIN_XPATH = '//common-posting-locations//*[@data-cy="location_pin"]/span[1]'
WORK_MODES = {"hybrydowo": "praca hybrydowa", "zdalnie": "praca zdalna", "praca zdalna": "praca zdalna",
              "stacjonarnie": "praca stacjonarna", "hybrid": "praca hybrydowa", "remote": "praca zdalna"}


def _text(el):
    # textContent zamiast .text — .text w Selenium zwraca "" dla niewidocznych elementów (dymek)
    return " ".join((el.get_attribute("textContent") or el.text or "").split())


def build_location(cities, mode):
    cities = [c for i, c in enumerate(cities) if c and c not in cities[:i]]
    if cities:
        return ", ".join(cities + ([mode] if mode else []))
    if mode == "praca zdalna":
        return "Zdalnie"
    return None


def read_location(driver, fallback_xpath=None):
    """Miasta z listy lokalizacji + tryb pracy, np. "Warszawa, Kraków, praca hybrydowa"."""
    try:
        cities = [_text(el) for el in driver.find_elements(By.XPATH, LOCATION_CITIES_XPATH)]
        pin = [_text(el) for el in driver.find_elements(By.XPATH, LOCATION_PIN_XPATH)]
        first = pin[0] if pin else ""
        mode = WORK_MODES.get(first.lower())
        if not cities and first and not mode:
            cities = [first]  # pojedyncze miasto widoczne bezpośrednio
        location = build_location([c for c in cities if c.lower() not in WORK_MODES], mode)
        if location:
            return location
    except Exception:
        pass
    if fallback_xpath:
        try:
            return driver.find_element(By.XPATH, fallback_xpath).text or "Nieokreślona"
        except Exception:
            pass
    return "Nieokreślona"


def parse_offer_details(driver, offer_url, title):
    """Dotychczasowy parser szczegółów (selektory bez zmian). Zakłada, że strona oferty jest otwarta."""
    wait = WebDriverWait(driver, WAIT_SECONDS)
    wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))

    try:
        description = driver.find_element(By.XPATH,
            '/html/body/nfj-root/nfj-layout/nfj-main-content/div/nfj-posting-details/div/'
            'common-main-loader/div/main/article/div[1]/common-posting-content-wrapper/div[1]/'
            'section[2]/nfj-read-more').text
    except Exception:
        description = "Brak opisu"

    try:
        salary = driver.find_element(By.XPATH,
            '/html/body/nfj-root/nfj-layout/nfj-main-content/div/nfj-posting-details/div/'
            'common-main-loader/div/main/article/div[2]/common-apply-box/div[1]/div/'
            'common-posting-salaries-list/div/h4').text
    except Exception:
        salary = "Brak wynagrodzenia"

    location = read_location(driver,
        '/html/body/nfj-root/nfj-layout/nfj-main-content/div/nfj-posting-details/div/'
        'common-main-loader/div/main/article/div[1]/common-posting-content-wrapper/div[1]/'
        'section[1]/div/ul/li[4]/common-posting-locations/div/span/span[1]')

    level = read_level(driver,
        '/html/body/nfj-root/nfj-layout/nfj-main-content/div/nfj-posting-details/div/'
        'common-main-loader/div/main/article/div[1]/common-posting-content-wrapper/div[1]/'
        'section[1]/div/ul/li[2]/div/span')

    try:
        company = driver.find_element(By.XPATH, '//*[@id="postingCompanyUrl"]').text
    except Exception:
        try:
            company = driver.find_element(By.XPATH,
                '/html/body/nfj-root/nfj-layout/nfj-main-content/div/nfj-posting-details/div/'
                'common-main-loader/div/main/article/div[1]/common-posting-content-wrapper/div[1]/'
                'section[1]/div/common-posting-header/div/div/a').text
        except Exception:
            company = "Brak informacji o firmie"

    try:
        requirement_elements = driver.find_elements(By.XPATH,
            '//*[@id="posting-requirements"]//li')
        requirements = [el.text.strip() for el in requirement_elements if el.text.strip()]
    except Exception:
        requirements = []

    return {
        "title": title,
        "description": description,
        "location": location,
        "salaryRange": salary,
        "level": level,
        "fetchedAt": datetime.now(timezone.utc).isoformat(timespec='microseconds').replace('+00:00', 'Z'),
        "offerUrl": offer_url,
        "company": company,
        "requirements": requirements,
        "source": "NoFluff"
    }


def scrape_new_offers(driver, discovered, known_urls, stats):
    """Faza 2: szczegóły otwierane wyłącznie dla ofert, których nie ma w bazie.
    Porównanie z bazą następuje PRZED driver.get(offer_url)."""
    offers = []
    opened = set()
    new_entries = [(key, entry) for key, entry in discovered.items() if key not in known_urls]
    stats["alreadyInDatabase"] = len(discovered) - len(new_entries)
    stats["new"] = len(new_entries)
    logging.info(f"Unikalne oferty: {len(discovered)}, już w bazie: {stats['alreadyInDatabase']}, "
                 f"nowe do pobrania: {len(new_entries)}")

    for index, (key, entry) in enumerate(new_entries, start=1):
        if key in opened or key in known_urls:
            continue
        opened.add(key)
        try:
            driver.get(entry["url"])
            stats["detailPagesOpened"] += 1
            offers.append(parse_offer_details(driver, entry["url"], entry["title"]))
            stats["parsed"] += 1
            logging.info(f"[{index}/{len(new_entries)}] Przetworzono ofertę: {entry['title']}")
        except Exception as e:
            stats["failed"] += 1
            logging.warning(f"[{index}/{len(new_entries)}] Problem podczas przetwarzania oferty {entry['url']}: {e}")
    return offers


def save_offers_to_file(offers, filename="detailed_offers.json"):
    """Zapis atomowy (plik tymczasowy + zamiana). Zapisuje także pustą listę — oznacza ona
    poprawny przebieg bez nowych ofert, a nie błąd."""
    folder_path = os.path.join("data", "offers")
    os.makedirs(folder_path, exist_ok=True)
    filepath = os.path.join(folder_path, filename)
    fd, tmp = tempfile.mkstemp(prefix=".nofluff_", suffix=".json.tmp", dir=folder_path)
    try:
        with os.fdopen(fd, "w", encoding="utf-8") as file:
            json.dump(offers, file, ensure_ascii=False, indent=4)
        os.replace(tmp, filepath)
        logging.info(f"Zapisano {len(offers)} ofert do pliku '{filepath}'.")
    except BaseException:
        if os.path.exists(tmp):
            os.remove(tmp)
        raise


def new_stats():
    return {
        "source": "NoFluff", "status": "FAILED", "message": "",
        "listPagesOpened": 0, "listPageErrors": 0, "found": 0, "duplicatesBetweenLists": 0,
        "unique": 0, "alreadyInDatabase": 0, "new": 0,
        "detailPagesOpened": 0, "parsed": 0, "failed": 0, "saved": 0,
    }


def run(driver, known_urls, stats, urls=SORTED_URLS, save=save_offers_to_file):
    started = time.monotonic()
    try:
        discovered = discover_offers(driver, urls, stats)
    except BlockedError as e:
        stats["message"] = f"{e}. Przerwano bez obchodzenia blokady — poprzedni plik bez zmian."
        stats["durationSec"] = round(time.monotonic() - started, 1)
        logging.error(stats["message"])
        return EXIT_BLOCKED
    if stats["listPageErrors"] == len(urls):
        stats["message"] = "Nie udało się załadować żadnej strony wyników — poprzedni plik bez zmian"
        stats["durationSec"] = round(time.monotonic() - started, 1)
        return EXIT_UNEXPECTED
    offers = scrape_new_offers(driver, discovered, known_urls, stats)
    save(offers)
    stats["saved"] = len(offers)
    stats["status"] = "PARTIAL" if stats["failed"] or stats["listPageErrors"] else "SUCCESS"
    stats["message"] = (f"Nowych ofert: {stats['new']} (z {stats['unique']} unikalnych), "
                        f"otwarto szczegółów: {stats['detailPagesOpened']}, zapisano: {len(offers)}")
    stats["durationSec"] = round(time.monotonic() - started, 1)
    return EXIT_OK


def main(argv):
    parser = argparse.ArgumentParser(description="Scraper ofert No Fluff Jobs")
    parser.add_argument("--known-urls", help="plik JSON z adresami ofert, które już są w bazie (przygotowuje Java)")
    parser.add_argument("--urls-file", help="plik JSON z adresami list wyszukiwania (panel admina, przygotowuje Java)")
    args = parser.parse_args(argv)
    stats = new_stats()

    if args.urls_file:
        try:
            search_urls = load_search_urls(args.urls_file)
        except Exception as e:
            stats["message"] = f"Nie można odczytać adresów wyszukiwania ({e}) — pobieranie przerwane"
            logging.error(stats["message"])
            print(json.dumps(stats, ensure_ascii=False))
            return EXIT_KNOWN_URLS
    else:
        logging.warning("Brak --urls-file: używam domyślnych adresów wyszukiwania (uruchomienie ręczne)")
        search_urls = SORTED_URLS
    logging.info(f"Adresów wyszukiwania: {len(search_urls)}")

    if args.known_urls:
        try:
            known_urls = load_known_urls(args.known_urls)
        except Exception as e:
            stats["message"] = f"Nie można odczytać istniejących ofert z bazy ({e}) — pobieranie przerwane"
            logging.error(stats["message"])
            print(json.dumps(stats, ensure_ascii=False))
            return EXIT_KNOWN_URLS
    else:
        logging.warning("Brak --known-urls: wszystkie znalezione oferty traktowane jako nowe (uruchomienie ręczne)")
        known_urls = set()
    logging.info(f"Ofert No Fluff Jobs w bazie: {len(known_urls)}")

    driver = create_driver()
    try:
        exit_code = run(driver, known_urls, stats, urls=search_urls)
    except Exception as e:
        logging.exception("Nieoczekiwany błąd scrapera")
        stats["message"] = str(e)
        exit_code = EXIT_UNEXPECTED
    finally:
        driver.quit()
        logging.info("Zakończono działanie scrapera.")

    logging.info("SCRAPER REPORT: " + ", ".join(f"{k}={stats[k]}" for k in (
        "listPagesOpened", "found", "unique", "duplicatesBetweenLists", "alreadyInDatabase",
        "new", "detailPagesOpened", "parsed", "failed", "saved") if k in stats))
    print(json.dumps(stats, ensure_ascii=False))
    return exit_code


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
