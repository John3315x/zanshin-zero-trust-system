package com.nanakusa.zanshingateway.security;

import com.nanakusa.zanshingateway.filter.JwtFilter;
import com.nanakusa.zanshingateway.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtFilter jwtFilter(JwtUtil jwtUtil) {
        return new JwtFilter(jwtUtil);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JwtFilter jwtFilter) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                .authorizeExchange(exchange -> exchange

                        // 🔓 Rutas públicas
                        .pathMatchers(
                                "/api/auth/**",
                                "/api/zanshin/**",

                                // Swagger UI
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",

                                // OpenAPI de los microservicios
                                "/auth/v3/api-docs",
                                "/users/v3/api-docs",

                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()


                        // 🔐 Rutas protegidas
                        .pathMatchers("/api/admin/**").hasRole("ADMIN")
                        .pathMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                        .pathMatchers("/api/support/**").hasAnyRole("SUPPORT", "ADMIN", "AUDITOR")
                        .pathMatchers("/api/auditor/**").hasAnyRole("AUDITOR", "ADMIN")
                        .pathMatchers("/api/test/**").hasAnyRole("USER", "ADMIN", "SUPPORT", "AUDITOR")

                        // Todo lo demás requiere autenticación
                        .anyExchange().authenticated()
                )

                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                .build();
    }
}
