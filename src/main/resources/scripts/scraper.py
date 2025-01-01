from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import json
import logging
from datetime import datetime
import os

# Konfiguracja logowania
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# Lista URL z różnymi opcjami sortowania
SORTED_URLS = [
    "https://nofluffjobs.com/pl/Java?sort=default",
    "https://nofluffjobs.com/pl/Java?sort=newest",
    "https://nofluffjobs.com/pl/Java?sort=salary-asc",
    "https://nofluffjobs.com/pl/Java?sort=salary-desc"
]

def scrape_offers():
    """
    Funkcja do pobierania ofert pracy za pomocą Selenium.
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
        offers = []

        for url in SORTED_URLS:
            logging.info(f"Ładowanie strony: {url}")
            driver.get(url)

            wait = WebDriverWait(driver, 30)
            offer_elements = wait.until(EC.presence_of_all_elements_located((By.XPATH, '//a[contains(@class, "posting-list-item")]')))
            logging.info(f"Liczba znalezionych ofert: {len(offer_elements)}")

            for index in range(len(offer_elements)):
                try:
                    # Odświeżenie listy elementów
                    offer_elements = wait.until(EC.presence_of_all_elements_located((By.XPATH, '//a[contains(@class, "posting-list-item")]')))
                    offer = offer_elements[index]

                    title = offer.find_element(By.XPATH, './/h3[contains(@class, "posting-title")]').text or "Brak tytułu"
                    offer_url = offer.get_attribute('href')

                    # Przejście do szczegółów oferty
                    driver.get(offer_url)
                    wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))

                    # Pobranie szczegółowych danych
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
                        # Próba z pierwszym XPath
                        company = driver.find_element(By.XPATH, '//*[@id="postingCompanyUrl"]').text
                    except Exception:
                        try:
                            # Próba z drugim XPath
                            company = driver.find_element(By.XPATH,
                                '/html/body/nfj-root/nfj-layout/nfj-main-content/div/nfj-posting-details/div/'
                                'common-main-loader/div/main/article/div[1]/common-posting-content-wrapper/div[1]/'
                                'section[1]/div/common-posting-header/div/div/a').text
                        except Exception:
                            company = "Brak informacji o firmie"

                    # Dodanie oferty do listy
                    offers.append({
                        "title": title,
                        "description": description,
                        "location": location,
                        "salaryRange": salary,
                        "level": level,
                        "applied": False,
                        "fetchedAt": datetime.utcnow().isoformat(timespec='microseconds') + "Z",
                        "offerUrl": offer_url,
                        "company": company  # Nowe pole
                    })

                    logging.info(f"Przetworzono ofertę: {title}")

                    # Powrót na stronę główną
                    driver.back()
                    offer_elements = wait.until(EC.presence_of_all_elements_located((By.XPATH, '//a[contains(@class, "posting-list-item")]')))

                except Exception as e:
                    logging.warning(f"Problem podczas przetwarzania oferty: {e}")
                    continue  # Kontynuuj iterację dla pozostałych ofert

        return offers

    except Exception as e:
        logging.error(f"Wystąpił błąd podczas pobierania ofert: {e}")
        return []

    finally:
        driver.quit()
        logging.info("Zakończono działanie funkcji scrape_offers.")

def save_offers_to_file(offers, filename="detailed_offers.json"):
    """
    Funkcja zapisuje listę ofert do pliku JSON.
    """
    # Ścieżka do zapisu
    folder_path = os.path.join("data", "offers")
    os.makedirs(folder_path, exist_ok=True)  # Tworzy folder, jeśli nie istnieje
    filepath = os.path.join(folder_path, filename)

    try:
        with open(filepath, "w", encoding="utf-8") as file:
            json.dump(offers, file, ensure_ascii=False, indent=4)
        logging.info(f"Zapisano {len(offers)} ofert do pliku '{filepath}'.")
    except Exception as e:
        logging.error(f"Wystąpił błąd podczas zapisywania do pliku: {e}")

if __name__ == "__main__":
    offers = scrape_offers()
    if offers:
        save_offers_to_file(offers)
    else:
        logging.info("Brak ofert do zapisania.")
