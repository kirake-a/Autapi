package com.lisoft.autapi.infrastructure.security;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.lisoft.autapi.application.utils.JWTUtils;
import com.lisoft.autapi.domain.exceptions.CannotLoadKeyException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtilsComponent implements JWTUtils {

    @Value("${security.jwt.expiration-time}")
    private String expirationTime;

    @Value("${security.jwt.private-key-path}")
    private Resource privateKeyResource;

    @Value("${security.jwt.public-key-path}")
    private Resource publicKeyResource;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @Override
    public PublicKey getPublicKey4Export() {
        return getPublicKey();
    }

    private PrivateKey getPrivateKey() {
        if (privateKey == null) {
            try (InputStream inputStream = privateKeyResource.getInputStream()) {
                String key = new String(inputStream.readAllBytes())
                        .replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\s", "");
                byte[] keyBytes = Base64.getDecoder().decode(key);
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                privateKey = keyFactory.generatePrivate(spec);
            } catch (Exception e) {
                throw new CannotLoadKeyException("Error loading private key", e);
            }
        }

        return privateKey;
    }

    private PublicKey getPublicKey() {
        if (publicKey == null) {
            try (InputStream inputStream = publicKeyResource.getInputStream()) {
                String key = new String(inputStream.readAllBytes())
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s", "");
                byte[] keyBytes = Base64.getDecoder().decode(key);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                publicKey = keyFactory.generatePublic(spec);
            } catch (Exception e) {
                throw new CannotLoadKeyException("Error loading public key", e);
            }
        }

        return publicKey;
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();

        if (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
            String role = user.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            String userId = "";

            if (user instanceof com.lisoft.autapi.infrastructure.schemas.UserSchema userEntity) {
                userId = userEntity.getId();
            }
            
            claims.put("role", role);
            claims.put("userId", userId);
        }

        return generateToken(claims, user);
    }

    @Override
    public String generateToken(Map<String, Object> extractClaims, UserDetails user) {
        return buildToken(extractClaims, user, getExpirationTime());
    }

    @Override
    public long getExpirationTime() {
        return Long.parseLong(this.expirationTime);
    }

    @Override
    public String buildToken(
            Map<String, Object> extractClaims,
            UserDetails user,
            long expirationTime) {
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    @Override
    public String extractUserId(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

}
