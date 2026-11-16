package com.example.offerbrowserprototype.domain.scraperurl;

import com.example.offerbrowserprototype.domain.offer.OfferSource;

import java.util.List;
import java.util.Map;

/**
 * Built-in search URLs per portal — the lists the scrapers used before they became
 * editable. Seeded into the database for a portal that has none, and offered by
 * "Przywróć domyślne" in the admin panel.
 */
public final class DefaultSearchUrls {

    public record Entry(String url, String label) {}

    private static final String TP_LEVELS = "trainee,assistant,junior,mid;p";
    private static final String JJ_LEVELS = "experience-levels=junior,mid";

    private static final Map<OfferSource, List<Entry>> DEFAULTS = Map.of(
            OfferSource.NOFLUFF, List.of(
                    new Entry("https://nofluffjobs.com/pl/Java?sort=default", "Java – cała Polska, domyślne sortowanie"),
                    new Entry("https://nofluffjobs.com/pl/Java?sort=newest", "Java – cała Polska, najnowsze"),
                    new Entry("https://nofluffjobs.com/pl/Java?sort=salary-asc", "Java – cała Polska, wynagrodzenie rosnąco"),
                    new Entry("https://nofluffjobs.com/pl/Java?criteria=seniority%3Dtrainee,junior", "Java – cała Polska, trainee/junior"),
                    new Entry("https://nofluffjobs.com/pl/warszawa/Java?criteria=seniority%3Dtrainee,junior", "Java – Warszawa, trainee/junior"),
                    new Entry("https://nofluffjobs.com/pl/warszawa/Java?criteria=seniority%3Dmid", "Java – Warszawa, mid"),
                    new Entry("https://nofluffjobs.com/pl/warszawa/Java?sort=newest", "Java – Warszawa, najnowsze"),
                    new Entry("https://nofluffjobs.com/pl/warszawa/Java?sort=salary-asc", "Java – Warszawa, wynagrodzenie rosnąco"),
                    new Entry("https://nofluffjobs.com/pl/lodz/Java?criteria=seniority%3Dtrainee,junior", "Java – Łódź, trainee/junior"),
                    new Entry("https://nofluffjobs.com/pl/lodz/Java?criteria=seniority%3Dmid", "Java – Łódź, mid"),
                    new Entry("https://nofluffjobs.com/pl/lodz/Java?sort=newest", "Java – Łódź, najnowsze"),
                    new Entry("https://nofluffjobs.com/pl/lodz/Java?sort=salary-asc", "Java – Łódź, wynagrodzenie rosnąco")),
            OfferSource.THEPROTOCOL, List.of(
                    new Entry("https://theprotocol.it/filtry/java;t/" + TP_LEVELS, "Java – cała Polska, trainee–mid"),
                    new Entry("https://theprotocol.it/filtry/java;t/" + TP_LEVELS + "/warszawa;wp", "Java – Warszawa, trainee–mid"),
                    new Entry("https://theprotocol.it/filtry/java;t/" + TP_LEVELS + "/lodz;wp", "Java – Łódź, trainee–mid"),
                    new Entry("https://theprotocol.it/filtry/java;t/trainee,assistant,junior;p", "Java – junior / młodszy programista"),
                    new Entry("https://theprotocol.it/filtry/java;t/mid;p", "Java – mid"),
                    new Entry("https://theprotocol.it/filtry/java;t/" + TP_LEVELS + "/zdalna;rw", "Java – praca zdalna, trainee–mid")),
            OfferSource.JUSTJOIN, List.of(
                    new Entry("https://justjoin.it/job-offers/warszawa/java?" + JJ_LEVELS, "Java – Warszawa, junior/mid"),
                    new Entry("https://justjoin.it/job-offers/lodz/java?" + JJ_LEVELS, "Java – Łódź, junior/mid"),
                    new Entry("https://justjoin.it/job-offers/all-locations/java?" + JJ_LEVELS + "&remote=yes",
                            "Java – zdalnie, cała Polska, junior/mid"))
    );

    private DefaultSearchUrls() {
    }

    public static List<Entry> of(OfferSource source) {
        return DEFAULTS.getOrDefault(source, List.of());
    }
}
