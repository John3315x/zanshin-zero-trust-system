package com.nanakusa.zanshin.security;

import com.nanakusa.zanshin.filter.JwtFilter;
import com.nanakusa.zanshin.filter.RateLimitFilter;
import com.nanakusa.zanshin.service.IPService;
import com.nanakusa.zanshin.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    /*@Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }*/

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Nota: Sacamos el JwtFilter porque API Gateway(Zanshin - Gateway) se encargará de validar el JWT y no necesitamos
    // validarlo en este microservicio. Si lo dejamos, se validaría dos veces (en Gateway y en este microservicio) y
    // eso no es eficiente. Además, si el JWT es inválido, el Gateway ya lo rechazará antes de que llegue a este
    // microservicio, por lo que no tiene sentido validar el JWT aquí también.
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            RateLimiterService rateLimiterService,
            IPService ipService) throws Exception {

        RateLimitFilter rateLimitFilter = new RateLimitFilter(rateLimiterService, ipService);

        http
                // API REST
                .csrf(AbstractHttpConfigurer::disable)

                // Sin sesiones
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        // Swagger/OpenAPI
                        .requestMatchers(
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Endpoints públicos (si los quieres listar explícitamente)
                        .requestMatchers("/auth/**").permitAll()

                        // El resto (por ahora)
                        .anyRequest().permitAll()
                )

                // Filtro de Rate Limit
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class);

        // Cuando actives JWT:
        //.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
