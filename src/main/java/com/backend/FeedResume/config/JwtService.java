package com.backend.FeedResume.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "eW91ci0yNTYtYml0LXNlY3JldC15b3VyLTI1Ni1iaXQtc2VjcmV0";

    // ========================= 🔐 Utility ==========================
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ==================== 🚀 Access Token ==========================
    public String generateAccessToken(String username, String provider) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authProvider", provider);
        return buildToken(claims, username, 1000 * 60 * 15); // 15 min
    }

    // ==================== 🔁 Refresh Token ==========================
    public String generateRefreshToken(String username) {
        return buildToken(new HashMap<>(), username, 1000 * 60 * 60 * 24 * 7); // 7 days
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            return extractUsername(token) != null && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // =================== 🔐 Token Builder ===========================
    private String buildToken(Map<String, Object> claims, String subject, long expirationMs) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // =================== 📥 Extract Data ============================
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractAuthProvider(String token) {
        return extractClaim(token, claims -> claims.get("authProvider", String.class));
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
