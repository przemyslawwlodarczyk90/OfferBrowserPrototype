from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import json
import logging
import sys
from datetime import datetime, timezone

# Konfiguracja logowania
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    stream=sys.stderr  # Logi na stderr
)

def scrape_single_offer(offer_url):
    """
    Funkcja do pobierania szczegółów pojedynczej oferty z podanego URL za pomocą Selenium.
    """
    chrome_options = webdriver.ChromeOptions()
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--window-size=1920,1080")
    chrome_options.add_argument("--headless")
    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument("--ignore-certificate-errors")
    chrome_options.add_argument('--disable-dev-shm-usage')

    service = Service(ChromeDriverManager().install())
    driver = webdriver.Chrome(service=service, options=chrome_options)

    try:
        logging.info(f"Ładowanie oferty: {offer_url}")
        driver.get(offer_url)

        wait = WebDriverWait(driver, 30)
        wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))

        # Pobieranie szczegółowych danych
        try:
            title = driver.find_element(By.XPATH, '//h3[contains(@class, "posting-title")]').text or "Brak tytułu"
        except Exception:
            title = "Brak tytułu"

        try:
            description = driver.find_element(By.XPATH, '//nfj-read-more').text
        except Exception:
            description = "Brak opisu"

        try:
            salary = driver.find_element(By.XPATH, '//common-posting-salaries-list/h4').text
        except Exception:
            salary = "Brak wynagrodzenia"

        try:
            location = driver.find_element(By.XPATH, '//common-posting-locations/span/span[1]').text
        except Exception:
            location = "Nieokreślona"

        try:
            level = driver.find_element(By.XPATH, '//li[2]/div/span').text or "Nieokreślony poziom"
        except Exception:
            level = "Nieokreślony poziom"

        try:
            company = driver.find_element(By.XPATH, '//*[@id="postingCompanyUrl"]').text
        except Exception:
            company = "Brak informacji o firmie"

        offer_details = {
            "title": title,
            "description": description,
            "location": location,
            "salaryRange": salary,
            "level": level,
            "applied": True,
            "fetchedAt": datetime.utcnow().strftime("%Y-%m-%dT%H:%M:%S.%f")[:-3] + "Z",
            "offerUrl": offer_url,
            "company": company
        }

        logging.info(f"Pobrano szczegóły oferty: {offer_details}")
        # Wypisanie JSON-u na stdout
        print(json.dumps(offer_details, ensure_ascii=False))

    except Exception as e:
        logging.error(f"Wystąpił błąd: {e}")
        sys.exit(1)
    finally:
        driver.quit()
        logging.info("Zamknięto przeglądarkę.")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Użycie: python scrape_offer_from_url.py <URL oferty>", file=sys.stderr)
        sys.exit(1)

    offer_url = sys.argv[1]
    scrape_single_offer(offer_url)
