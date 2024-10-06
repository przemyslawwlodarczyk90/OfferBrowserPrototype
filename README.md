# Offer Browser Prototype

## Opis projektu

Offer Browser Prototype to aplikacja webowa, która umożliwia zarządzanie ofertami pracy, rejestrację użytkowników oraz uwierzytelnianie przy użyciu tokenów JWT. Aplikacja pozwala na przeglądanie ofert pracy z wewnętrznej bazy danych, a także z zewnętrznych źródeł poprzez integracje API, zarówno tworzenie i wystawianie ogłoszeń na zewnętrznych portalach ogłoszeniowych. Dodatkowo zawiera funkcje związane z rejestracją, logowaniem i zarządzaniem profilem użytkownika.

## Funkcjonalności

-   **Rejestracja i logowanie użytkowników**: Tworzenie konta, potwierdzenie rejestracji oraz uwierzytelnianie przy użyciu tokenów JWT.
-   **Zarządzanie ofertami pracy**: Dodawanie, edycja, usuwanie oraz przeglądanie ofert pracy.
-   **Pobieranie zewnętrznych ofert pracy**: Integracja z zewnętrznymi dostawcami ofert pracy oraz ich automatyczne pobieranie.
-  **Tworzenie i wypychanie ogłoszenia na zewnętrzny provider.**
-   **Cache i harmonogramowanie**: Wykorzystanie Redis do cachowania ofert oraz zaplanowane zadania (Scheduler) do okresowego pobierania ofert.

## Technologie

-   **Język programowania**: Java 17
-   **Framework**: Spring Boot 3.2.5
    -   **Spring Web**: Obsługa REST API.
    -   **Spring Security**: Uwierzytelnianie przy użyciu JWT.
    -   **Spring Data MongoDB**: Zarządzanie danymi w bazie MongoDB.
    -   **Spring Data Redis**: Cachowanie ofert przy użyciu Redis.
    -   **Spring Retry**: Obsługa retry dla zewnętrznych integracji API.
-   **Baza danych**: MongoDB (przechowywanie użytkowników oraz ofert pracy).
-   **Cache**: Redis (do przechowywania tymczasowych ofert pracy).
-   **Kontrola wersji**: Git
-   **Integracja z zewnętrznymi API**: Pobieranie ofert pracy z zewnętrznych dostawców za pomocą REST API.
-   **Obsługa harmonogramowania**: Automatyczne pobieranie ofert zewnętrznych w oparciu o harmonogram (Spring Scheduler).
-   **Swagger/OpenAPI**: Dokumentacja API oraz testowanie endpointów.
-   **Thymeleaf**: Generowanie szablonów e-mail dla potwierdzenia rejestracji.


## Bezpieczeństwo

Projekt korzysta z **Spring Security** oraz **JWT** (JSON Web Tokens) do uwierzytelniania użytkowników. Token JWT jest generowany przy logowaniu i wykorzystywany do autoryzacji użytkownika w kolejnych żądaniach.

## Integracje zewnętrzne

Projekt pobiera oferty pracy z zewnętrznych dostawców poprzez ich API. Za integrację odpowiada fabryka `JobOfferProviderFactory`, która może obsłużyć wielu dostawców.

## Testowanie

Aktualnie projekt jest w trakcie dodawania testów jednostkowych oraz integracyjnych. Wszystkie główne funkcjonalności zostały przetestowane manualnie, a aplikacja działa zgodnie z oczekiwaniami. Planuje się dodanie:

-   **Testów jednostkowych**: Weryfikacja poprawności pojedynczych komponentów aplikacji.
-   **Testów integracyjnych**: Sprawdzenie poprawności współdziałania pomiędzy różnymi modułami aplikacji.

> Na obecnym etapie **nie są planowane testy end-to-end (E2E)**, a testowanie aplikacji odbywa się manualnie.

## Konfiguracja

Aplikacja korzysta z pliku `application.properties`, w którym można dostosować następujące ustawienia:

-   **Baza danych MongoDB**: Konfiguracja połączenia do bazy danych.
-   **Cache Redis**: Włączanie/wyłączanie cache oraz ustawienie czasu wygaśnięcia.
-   **JWT**: Klucz tajny, czas wygaśnięcia tokena oraz issuer.
-   **Scheduler**: Ustawienia harmonogramu do automatycznego pobierania ofert.
-   **SMTP**: Konfiguracja serwera pocztowego do wysyłania e-maili potwierdzających rejestrację.

## Uruchomienie projektu

### Wymagania

-   Java 17
-   Maven
-   MongoDB
-   Redis (jeśli chcesz korzystać z cache)

### Komendy

1.  **Klonowanie repozytorium**:
    
    
    
    Skopiuj kod
    
    `git clone <repo-url>
    cd OfferBrowserPrototype` 
    
2.  **Budowanie projektu**:
    
   
    
    Skopiuj kod
    
    `mvn clean install` 
    
3.  **Uruchomienie aplikacji**:
    
    
    
    Skopiuj kod
    
    `mvn spring-boot:run` 
    
4.  **Swagger/OpenAPI**: Dokumentacja API dostępna pod adresem:
    
 
    
    Skopiuj kod
    
    `http://localhost:8080/swagger-ui.html` 
    

## Docker

Projekt zawiera plik `docker-compose.yml` do szybkiego uruchomienia MongoDB w celu pracy na lokalnym środowisku.


## Licencja

Projekt jest otwartoźródłowy i udostępniany na licencji **MIT**. Możesz dowolnie kopiować, modyfikować i rozpowszechniać kod projektu pod warunkiem zachowania informacji o licencji i autorach.
