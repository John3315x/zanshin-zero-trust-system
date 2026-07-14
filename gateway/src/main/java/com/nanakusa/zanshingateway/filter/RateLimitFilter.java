package com.nanakusa.zanshingateway.filter;

import com.nanakusa.zanshingateway.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class RateLimitFilter implements GlobalFilter { // GlobalFilter se aplica a todas las rutas, mientras que GatewayFilter se puede aplicar a rutas específicas en la configuración de rutas del gateway.

    @Autowired
    RateLimiterService rateLimiterService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // Obtener la IP del cliente, considerando proxies y balanceadores de carga
        String ip = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"))
                .map(header -> header.split(",")[0])
                .orElseGet(() ->
                        Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                                .map(addr -> addr.getAddress().getHostAddress())
                                .orElse("unknown")
                );

        // Obtener el endpoint solicitado (ruta de la solicitud)
        String path = exchange.getRequest().getURI().getPath();

        // Aplicar rate limit solo a los endpoints de autenticación (ej: /api/auth/login, /api/auth/register), permitiendo un número limitado de solicitudes por IP para estos endpoints críticos.
        /*if (!path.startsWith("/api/auth")) { // [QUEDAMOS AQUI]🚩🚩🚩Termina callendo en el filtro redis del authService
            System.out.println("LA RUTA: " + path);
            return chain.filter(exchange);
        }*/

        // Rate limit por endpoint: Evita que un endpoint específico sea abusado, permitiendo un número limitado de solicitudes por IP para cada endpoint.
        String key = "gateway:rate_limit:" + ip + ":" + path; // Importante el prefijo "gateway:rate_limit:" para diferenciarlo de otros posibles usos de rate limiting en otros microservicios. Esto ayuda a organizar y evitar colisiones en Redis, asegurando que las claves relacionadas con el rate limiting del gateway estén claramente identificadas y separadas de otras funcionalidades que también puedan usar Redis para almacenar datos.

        // Permitir un máximo de 5 solicitudes por IP por endpoint cada 10 minutos (600 segundos)
        boolean allowed = rateLimiterService.isAllowed(key, 5, 600);

        // Si se excede el límite, responder con 429 Too Many Requests
        if (!allowed) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders().add("Content-Type", "application/json");

            String body = "{\"error\": \"Too many requests\", \"status\": 429}";

            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));

            return exchange.getResponse().writeWith(Mono.just(buffer));
        }

        // Si la solicitud está permitida, continuar con el procesamiento normal
        return chain.filter(exchange);
    }
}
