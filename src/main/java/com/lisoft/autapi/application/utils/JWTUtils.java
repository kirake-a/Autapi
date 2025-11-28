package com.lisoft.autapi.application.utils;

import java.security.PublicKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JWTUtils {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails user);

    String generateToken(Map<String, Object> extractClaims, UserDetails user);

    long getExpirationTime();

    String buildToken(Map<String, Object> extractClaims, UserDetails user, long expirationTime);

    boolean isTokenValid(String token, UserDetails user);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);

    Claims extractAllClaims(String token);

    String extractRole(String token);

    String extractUserId(String token);

    PublicKey getPublicKey4Export();
}
