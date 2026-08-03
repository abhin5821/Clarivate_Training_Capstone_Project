package org.capstonegrp8.restaurant_management_system.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET ;

    public String generateToken(String email, String role) {
    SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
    return Jwts.builder()
            .setSubject(email)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
}

public String extractRole(String token) {
    return extractAllClaims(token).get("role", String.class);
}

private Claims extractAllClaims(String token) {
    SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
    return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
}
    public String extractUsername(String token) {

        SecretKey key =
                Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
