package com.example.offerbrowserprototype.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-time-ms}")
    private long expirationTimeMs;

    @Value("${jwt.issuer}")
    private String issuer;

    private Algorithm getAlgorithm() {
        logger.info("Using secret key: {}", secretKey); // Ensure to remove or hide this log in production!
        try {
            return Algorithm.HMAC256(secretKey);
        } catch (IllegalArgumentException e) {
            logger.error("Error in secret key format: {}", e.getMessage());
            throw e;
        }
    }

    private DecodedJWT getDecodedJWT(String token) {
        logger.debug("Decoding token: {}", token);
        try {
            Algorithm algorithm = getAlgorithm();
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            logger.debug("Token successfully decoded: {}", jwt.getSubject());
            return jwt;
        } catch (Exception e) {
            logger.error("Error while decoding token: {}", e.getMessage());
            throw e;
        }
    }

    public String extractUsername(String token) {
        logger.debug("Extracting username from token: {}", token);
        try {
            DecodedJWT jwt = getDecodedJWT(token);
            String username = jwt.getSubject();
            logger.debug("Extracted username: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("Failed to extract username from token: {}", e.getMessage());
            throw e;
        }
    }

    public String generateToken(UserDetails userDetails) {
        logger.info("Generating token for user: {}", userDetails.getUsername());
        try {
            Algorithm algorithm = getAlgorithm();
            String token = JWT.create()
                    .withSubject(userDetails.getUsername())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expirationTimeMs))
                    .withIssuer(issuer)
                    .sign(algorithm);
            logger.info("Generated token: {}", token);
            return token;
        } catch (Exception e) {
            logger.error("Failed to generate token: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        logger.info("Validating token for user: {}", userDetails.getUsername());
        try {
            DecodedJWT jwt = getDecodedJWT(token);
            String username = jwt.getSubject();
            boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
            logger.info("Token validation result: {}", isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Invalid JWT Token: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = getDecodedJWT(token);
            Date expiration = jwt.getExpiresAt();
            boolean isExpired = expiration.before(new Date());
            logger.debug("Token expiration date: {}, Is expired: {}", expiration, isExpired);
            return isExpired;
        } catch (Exception e) {
            logger.error("Failed to check if token is expired: {}", e.getMessage());
            throw e;
        }
    }
}
