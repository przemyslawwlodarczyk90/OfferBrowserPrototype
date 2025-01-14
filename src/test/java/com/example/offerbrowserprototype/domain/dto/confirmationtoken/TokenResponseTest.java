//package com.example.offerbrowserprototype.domain.dto.confirmationtoken;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
// class TokenResponseTest {
//
//    @Test
//    public void shouldCreateTokenResponseWithToken() {
//        String testToken = "FERDINAND_TOKEN";
//
//        TokenResponse tokenResponse = new TokenResponse(testToken);
//
//        assertEquals(testToken, tokenResponse.getToken(), "Pole token powinno być poprawnie przypisane.");
//    }
//
//
//    @Test
//    public void shouldUpdateTokenField() {
//
//        TokenResponse tokenResponse = new TokenResponse("INITIAL_TOKEN");
//        String updatedToken = "KIEPSKI_UPDATED_TOKEN";
//
//        tokenResponse.setToken(updatedToken);
//
//        assertEquals(updatedToken, tokenResponse.getToken(), "Pole token powinno być poprawnie zaktualizowane.");
//    }
//
//    @Test
//     void shouldAllowNullToken() {
//
//        TokenResponse tokenResponse = new TokenResponse(null);
//
//        assertNull(tokenResponse.getToken(), "Pole token powinno zwracać null, jeśli nie zostało ustawione.");
//    }
//}
