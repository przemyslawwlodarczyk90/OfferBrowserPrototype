from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import json
import logging
import sys

# Konfiguracja logowania
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

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
        logging.info(f"Ładowanie strony oferty: {offer_url}")
        driver.get(offer_url)

        wait = WebDriverWait(driver, 30)
        wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))

        # Pobranie szczegółowych danych
        try:
            title = driver.find_element(By.XPATH, './/h3[contains(@class, "posting-title")]').text or "Brak tytułu"
        except Exception:
            title = "Brak tytułu"

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

        try:
            location = driver.find_element(By.XPATH,
                '/html/body/nfj-root/nfj-layout/nfj-main-content/div/nfj-posting-details/div/'
                'common-main-loader/div/main/article/div[1]/common-posting-content-wrapper/div[1]/'
                'section[1]/div/ul/li[4]/common-posting-locations/div/span/span[1]').text
        except Exception:
            location = "Nieokreślona"

        try:
            level = driver.find_element(By.XPATH,
                '/html/body/nfj-root/nfj-layout/nfj-main-content/div/nfj-posting-details/div/'
                'common-main-loader/div/main/article/div[1]/common-posting-content-wrapper/div[1]/'
                'section[1]/div/ul/li[2]/div/span').text or "Nieokreślony poziom"
        except Exception:
            level = "Nieokreślony poziom"

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

        offer_details = {
            "title": title,
            "description": description,
            "location": location,
            "salaryRange": salary,
            "level": level,
            "company": company,
            "offerUrl": offer_url,
        }

        logging.info(f"Pobrano dane oferty: {title}")
        return offer_details

    except Exception as e:
        logging.error(f"Wystąpił błąd podczas pobierania danych oferty: {e}")
        return None

    finally:
        driver.quit()
        logging.info("Zakończono działanie Selenium.")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Użycie: python scrape_offer_from_url.py <URL oferty>")
        sys.exit(1)

    offer_url = sys.argv[1]
    offer_data = scrape_single_offer(offer_url)

    if offer_data:
        print(json.dumps(offer_data, ensure_ascii=False, indent=4))
    else:
        print("Nie udało się pobrać danych oferty.")
