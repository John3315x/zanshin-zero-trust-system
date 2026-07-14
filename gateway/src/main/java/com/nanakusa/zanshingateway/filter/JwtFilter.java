package com.nanakusa.zanshingateway.filter;

import com.nanakusa.zanshingateway.util.JwtUtil;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

//@Component
//@Order(-1) // Asegura que este filtro se ejecute antes de otros filtros de seguridad
public class JwtFilter implements WebFilter{

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    // Aquí iría la lógica para interceptar las solicitudes, extraer el token JWT, validarlo y establecer el contexto de seguridad.

     // Por ejemplo:
     // 1. Interceptar la solicitud entrante.
     // 2. Extraer el token JWT del encabezado Authorization.
     // 3. Validar el token (verificar firma, expiración, etc.).
     // 4. Si el token es válido, establecer el contexto de seguridad con los detalles del usuario.
     // 5. Continuar con la cadena de filtros. {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        //System.out.println("ENTRA AL FILTRO");

        String path = exchange.getRequest().getURI().getPath();

        System.out.println("JWT Filter -> " + path);

        // 🔓 Rutas públicas
        if (path.startsWith("/api/auth")
                || path.startsWith("/api/zanshin")

                // Swagger
                || path.startsWith("/webjars/")
                || path.startsWith("/swagger-ui/")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/auth/v3/api-docs")
                || path.startsWith("/users/v3/api-docs")

                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.equals("/favicon.ico")) {

            return chain.filter(exchange);
        }

        //System.out.println("REQUIERE JWT");

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                // Valida token y obtiene claims
                String email = jwtUtil.getSubject(token);
                String role = jwtUtil.getRole(token);

                //System.out.println("AQUI: " + email + " | " + role);

                // Crear roles
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

                // Crear Authentication
                var auth = new UsernamePasswordAuthenticationToken(email, null, authorities);

                // Importante en WebFlux: Meterlo en el contexto de seguridad reactivo
                return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

            } catch (Exception e) {
                return unauthorized(exchange);
            }
        }

        return unauthorized(exchange);
    }

    // Corta la ejecución
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
