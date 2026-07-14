package com.nanakusa.zanshin.filter;

import com.nanakusa.zanshin.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

//@Component
/**
 * Spring Boot hace esto:
 *
 * Detecta tu clase JwtFilter (por @Component)
 * Ve que es un Filter
 * Lo registra automáticamente en la cadena de filtros del servidor
 *
 * 👉 Resultado:
 *
 * Request → JwtFilter → DispatcherServlet → Controller
 *
 * 🔥 Diferencia importante
 * 🟡 Filtro registrado automáticamente (tu caso)
 * Se ejecuta SIEMPRE
 * Antes de Spring Security
 * No tienes control fino
 */
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔓 Rutas públicas
        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        // 🔴 🔥 CASO IMPORTANTE: NO HAY TOKEN
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        try {

            Claims claims = jwtUtil.validateToken(token);

            if (claims.getSubject() != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                String role = claims.get("role", String.class);

                System.out.println("Token válido para usuario: " + claims.getSubject());

                List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                claims.getSubject(),
                                null,
                                authorities
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
