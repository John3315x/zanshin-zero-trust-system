package com.nanakusa.zanshin.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtUtilTest {

    @Test
    void deberia_lanzar_exepcion_token_invalido(){
        // Arrangen

        String SECRET = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc3NTIyNjYyMywiZXhwIjoxNzc1MjI4NDIzfQ.PeD5N2MiVf7KQwulyYzkTO0yIh6KHsvqPU9X5-3YpPE";

        String expiredToken = Jwts.builder()
                .setSubject("koneko@test.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 10)) // hace 10 min
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 5)) // expirado
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))// OJO!!! usar la misma clave con la que se valida
                .compact();

        // Act

        // Assert
        assertThrows(
                ExpiredJwtException.class,
                () -> JwtUtil.validateTokenNotTryCach(expiredToken)
        );
    }
}
