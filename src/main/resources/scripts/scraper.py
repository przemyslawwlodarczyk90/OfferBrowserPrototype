from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import json
import logging
from datetime import datetime, timezone
import os

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


def scrape_offers():
    chrome_options = webdriver.ChromeOptions()
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--window-size=1920,1080")
    chrome_options.add_argument("--headless")
    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument("--ignore-certificate-errors")
    chrome_options.add_argument('--disable-dev-shm-usage')

    service = Service(ChromeDriverManager().install())
    driver = webdriver.Chrome(service=service, options=chrome_options)

    offers = []
    seen_urls = set()

    try:
        for url in SORTED_URLS:
            try:
                logging.info(f"Ładowanie strony: {url}")
                driver.get(url)

                wait = WebDriverWait(driver, 30)
                offer_elements = wait.until(
                    EC.presence_of_all_elements_located(
                        (By.XPATH, '//a[contains(@class, "posting-list-item")]')
                    )
                )
                logging.info(f"Liczba znalezionych ofert: {len(offer_elements)}")

                for index in range(len(offer_elements)):
                    try:
                        offer_elements = wait.until(
                            EC.presence_of_all_elements_located(
                                (By.XPATH, '//a[contains(@class, "posting-list-item")]')
                            )
                        )
                        offer = offer_elements[index]

                        title = offer.find_element(
                            By.XPATH, './/h3[contains(@class, "posting-title")]'
                        ).text or "Brak tytułu"
                        offer_url = offer.get_attribute('href')

                        # Pomiń duplikaty
                        if offer_url in seen_urls:
                            logging.info(f"Pominięto duplikat: {title}")
                            continue
                        seen_urls.add(offer_url)

                        driver.get(offer_url)
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

                        offers.append({
                            "title": title,
                            "description": description,
                            "location": location,
                            "salaryRange": salary,
                            "level": level,

                            "fetchedAt": datetime.now(timezone.utc).isoformat(timespec='microseconds').replace('+00:00', 'Z'),
                            "offerUrl": offer_url,
                            "company": company
                        })

                        logging.info(f"Przetworzono ofertę: {title}")

                        driver.back()
                        offer_elements = wait.until(
                            EC.presence_of_all_elements_located(
                                (By.XPATH, '//a[contains(@class, "posting-list-item")]')
                            )
                        )

                    except Exception as e:
                        logging.warning(f"Problem podczas przetwarzania oferty #{index}: {e}")
                        # Wróć na stronę listy jeśli coś poszło nie tak
                        try:
                            driver.get(url)
                            wait.until(EC.presence_of_all_elements_located(
                                (By.XPATH, '//a[contains(@class, "posting-list-item")]')
                            ))
                        except Exception:
                            pass
                        continue

            except Exception as e:
                logging.error(f"Błąd podczas ładowania strony {url}: {e}")
                logging.info(f"Zebrano dotychczas {len(offers)} ofert, kontynuuję z następną stroną...")
                continue  # Przejdź do następnego URL zamiast przerywać całość

    finally:
        driver.quit()
        logging.info("Zakończono działanie funkcji scrape_offers.")

    return offers


def save_offers_to_file(offers, filename="detailed_offers.json"):
    folder_path = os.path.join("data", "offers")
    os.makedirs(folder_path, exist_ok=True)
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