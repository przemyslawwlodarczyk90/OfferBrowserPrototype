//package com.example.offerbrowserprototype.domain.statistics;
//
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class CityDistributionTest {
//
//    @Test
//    void shouldCreateCityDistributionUsingNoArgsConstructor() {
//        // Użycie konstruktora bezargumentowego
//        CityDistribution cityDistribution = new CityDistribution();
//
//        // Weryfikacja początkowych wartości
//        assertThat(cityDistribution.getId()).isNull();
//        assertThat(cityDistribution.getCount()).isEqualTo(0);
//    }
//
//    @Test
//    void shouldCreateCityDistributionUsingAllArgsConstructor() {
//        // Użycie konstruktora z argumentami
//        CityDistribution cityDistribution = new CityDistribution("city1", 100);
//
//        // Weryfikacja wartości
//        assertThat(cityDistribution.getId()).isEqualTo("city1");
//        assertThat(cityDistribution.getCount()).isEqualTo(100);
//    }
//
//    @Test
//    void shouldSetAndGetValues() {
//        // Tworzenie obiektu
//        CityDistribution cityDistribution = new CityDistribution();
//
//        // Ustawianie wartości
//        cityDistribution.setId("city2");
//        cityDistribution.setCount(200);
//
//        // Weryfikacja getterów
//        assertThat(cityDistribution.getId()).isEqualTo("city2");
//        assertThat(cityDistribution.getCount()).isEqualTo(200);
//    }
//
//    @Test
//    void shouldTestEquality() {
//        // Tworzenie dwóch identycznych obiektów
//        CityDistribution city1 = new CityDistribution("city1", 100);
//        CityDistribution city2 = new CityDistribution("city1", 100);
//
//        // Weryfikacja metody equals
//        assertThat(city1).isEqualTo(city2);
//        assertThat(city1.hashCode()).isEqualTo(city2.hashCode());
//    }
//
//    @Test
//    void shouldTestInequality() {
//        // Tworzenie różnych obiektów
//        CityDistribution city1 = new CityDistribution("city1", 100);
//        CityDistribution city2 = new CityDistribution("city2", 200);
//
//        // Weryfikacja nierówności
//        assertThat(city1).isNotEqualTo(city2);
//    }
//
//    @Test
//    void shouldConvertToString() {
//        // Tworzenie obiektu
//        CityDistribution cityDistribution = new CityDistribution("city1", 100);
//
//        // Weryfikacja metody toString
//        assertThat(cityDistribution.toString()).isEqualTo("CityDistribution(id=city1, count=100)");
//    }
//}
