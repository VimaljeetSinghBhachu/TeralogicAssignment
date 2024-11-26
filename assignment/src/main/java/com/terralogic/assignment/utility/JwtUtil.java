package com.terralogic.assignment.utility;

import com.terralogic.assignment.constants.Constants;
import com.terralogic.assignment.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtUtil {

    private static String secretKey = "tIuSF0kKuRRndeef6tR404BQIqJihVpytD9ApIebNKGUfq8urHds+OxJ0/lVJCki";  // Key for signing and verifying the JWT

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    // Generate JWT token
    public String generateToken(User user) {
        if (secretKey == null) {
            throw new IllegalArgumentException("Secret key cannot be null or empty");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("firstName", user.getFirstName());
        claims.put("middleName", user.getMiddleName());
        claims.put("lastName", user.getLastName());
        claims.put("email", user.getEmail());
        claims.put("contact", user.getContact());
        claims.put("expirationDate", user.getExpirationDate());
        claims.put("description", user.getDescription());
        claims.put("timeout", user.getTimeout());
        claims.put("dateTimeFormat", user.getDateTimeFormat());
        claims.put("roleName", user.getRoleName());
        claims.put("scopeName", user.getScopeName());
        claims.put("primaryGroupName", user.getPrimaryGroupName());
        claims.put("secondaryGroupName", user.getSecondaryGroupName());
        claims.put("language", user.getLanguage());
        claims.put("organization", user.getOrganization());
        claims.put("timezone", user.getTimezone());
        claims.put("memo", user.getMemo());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Validate the token
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getSubject();
        } catch (Exception e) {
            System.err.println("Failed to extract user ID from token: " + e.getMessage());
            return null;
        }
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.err.println("Failed to parse token: " + e.getMessage());
            throw e;
        }
    }
}