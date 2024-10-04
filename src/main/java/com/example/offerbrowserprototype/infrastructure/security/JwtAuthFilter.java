package com.example.offerbrowserprototype.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Sprawdzenie, czy nagłówek Authorization istnieje i czy rozpoczyna się od "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Logowanie tokena dla celów debugowania
            System.out.println("Received token: " + token);

            // Weryfikacja struktury tokena
            String[] tokenParts = token.split("\\.");
            if (tokenParts.length != 3) {
                System.err.println("Invalid token format: " + token);
                // Możesz zwrócić błąd odpowiedzi lub przerwać przetwarzanie w inny sposób
                return;
            }

            try {
                // Próba wydobycia nazwy użytkownika z tokena
                String username = jwtService.extractUsername(token);

                // Sprawdzenie, czy użytkownik nie jest już uwierzytelniony
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Sprawdzenie poprawności tokena
                    if (jwtService.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Ustawienie użytkownika jako uwierzytelnionego
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (Exception e) {
                // Obsługa błędów związanych z tokenem JWT
                System.err.println("Invalid JWT Token: " + e.getMessage());
            }
        }

        // Przekazanie żądania do następnego filtra
        filterChain.doFilter(request, response);
    }
}
