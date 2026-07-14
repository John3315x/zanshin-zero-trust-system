package com.nanakusa.zanshingateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET;
    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // 🔥 valida token y devuelve claims
    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🔥 obtiene email (subject)
    public String getSubject(String token) {
        return validateToken(token).getSubject();
    }

    // 🔥 obtiene rol
    public String getRole(String token) {
        return validateToken(token).get("role", String.class);
    }

    // 🔥 opcional: verificar expiración manual
    public boolean isTokenExpired(String token) {
        return validateToken(token).getExpiration().before(new java.util.Date());
    }
}
