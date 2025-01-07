# Offer Browser Prototype

## 📖 Wprowadzenie

**Offer Browser Prototype** to zaawansowana aplikacja webowa stworzona z myślą o usprawnieniu procesu zarządzania ofertami pracy. Jest to kompleksowe narzędzie, które umożliwia:

- **Pobieranie ofert pracy** z różnych źródeł:
    - Integracja z API dostawców ofert pracy.
    - Dynamiczne pozyskiwanie danych z serwisów internetowych.

- **Przechowywanie i porządkowanie ofert** w centralnej bazie danych:
    - Oferty oznaczane jako „zaaplikowane” lub „niezaaplikowane”.
    - Historia zmian i aktualizacji danych.

- **Tworzenie i przechowywanie notatek aplikacyjnych**:
    - Automatyczne zapisywanie szczegółów aplikacji (data, czas, firma, URL oferty).
    - Możliwość dodawania własnych notatek do każdej oferty.

- **Generowanie statystyk i analiz**:
    - Rozkład ofert według miast i poziomów doświadczenia.
    - Podsumowania liczby ofert w bazie i ich statusów.

Aplikacja oferuje pełną kontrolę nad procesem rekrutacyjnym, jednocześnie zapewniając użytkownikowi intuicyjne i elastyczne narzędzia do zarządzania.

---
## 🛠 Architektura aplikacji

Aplikacja została zaprojektowana w sposób modułowy, co umożliwia łatwe rozszerzanie funkcjonalności oraz integrację z różnorodnymi dostawcami ofert pracy.

### 📦 Moduły pobierania ofert
- **Integracje API** – Aplikacja obsługuje integracje z zewnętrznymi systemami API, co pozwala na automatyczne importowanie ofert pracy od wielu dostawców.
- **Dynamiczne pozyskiwanie danych** – Mechanizm umożliwiający pozyskiwanie ofert ze stron internetowych bez potrzeby integracji API, pozwalając użytkownikowi na dostęp do stale aktualnych danych.

### 💾 Przechowywanie danych
- **MongoDB** – Centralna baza danych służąca do przechowywania ofert pracy, statystyk i notatek aplikacyjnych. Gwarantuje trwałość danych i skalowalność.
- **Redis** – System cache'owania, który znacząco przyspiesza dostęp do często używanych informacji, takich jak wyniki statystyk czy ostatnio przeglądane oferty.

### 🗂 Zarządzanie ofertami
Aplikacja dostarcza narzędzia umożliwiające pełną kontrolę nad ofertami pracy:
- **Filtrowanie i sortowanie** – Możliwość przeglądania ofert według statusu, daty pobrania, lokalizacji lub poziomu doświadczenia.
- **Aktualizacja danych** – Prosta edycja szczegółów ofert, takich jak tytuł, lokalizacja, wynagrodzenie czy opis stanowiska.
- **Monitorowanie aplikacji** – Automatyczne oznaczanie ofert, na które użytkownik zaaplikował, co pozwala zachować porządek w procesie rekrutacyjnym.

### 📊 Statystyki
Funkcjonalność statystyk w aplikacji pozwala na:
- analizę rozkładu ofert według miast, co pomaga w identyfikacji lokalnych trendów na rynku pracy,
- ocenę wymagań doświadczenia w ofertach, ułatwiając wybór najlepiej dopasowanych ogłoszeń,
- podsumowanie statusu ofert, takich jak liczba zaaplikowanych czy wciąż otwartych ogłoszeń.

### 📝 Notatki aplikacyjne
Aplikacja działa również jako kompleksowy notatnik rekrutacyjny:
- **Automatyczne notatki** – Zapis szczegółów aplikacji, takich jak data aplikacji, nazwa firmy czy URL oferty.
- **Ręczne komentarze** – Użytkownik może dodawać własne notatki do ofert, co wspiera zarządzanie procesem rekrutacyjnym.
- **Historia aplikacji** – Przegląd zapisanych notatek, pozwalający na szybkie przypomnienie szczegółów aplikacji i dalsze planowanie działań.

### 🌟 Kluczowe cechy
- Elastyczność i skalowalność dzięki modularnej architekturze.
- Integracja z MongoDB i Redis dla szybkiego i trwałego przechowywania danych.
- Możliwość łatwego rozszerzania funkcjonalności, w tym dodawania nowych dostawców ofert pracy.
- Pełna kontrola nad procesem rekrutacyjnym dzięki notatkom aplikacyjnym i statystykom.

## ✅ Testowanie

Aplikacja została gruntownie przetestowana:
- **Jednostkowe testy handlerów** – weryfikacja logiki biznesowej.
- **Integracyjne testy repozytoriów** – testy zapisu i odczytu z bazy MongoDB.

### Przykładowe obszary testów:
- Pobieranie i przechowywanie danych w bazie.
- Generowanie statystyk i przetwarzanie danych.
- Obsługa notatek aplikacyjnych.

---

## 🌟 Podsumowanie

**Offer Browser Prototype** to wszechstronne narzędzie, które wspiera proces rekrutacyjny na każdym etapie:
- Od pobierania ofert z różnych źródeł.
- Przez zarządzanie i oznaczanie aplikowanych ofert.
- Po zaawansowane statystyki i przechowywanie notatek aplikacyjnych.

Dzięki elastycznej architekturze i integracjom aplikacja idealnie nadaje się zarówno dla osób indywidualnych, jak i zespołów HR. 🎯
