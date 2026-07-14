package com.nanakusa.zanshingateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    // 👉 conexión con Redis [-guarda datos en memoria, -rápido, -ideal para rate limiting]
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     *
     * @param key (identifica al usuario/IP/etc. que hace la solicitud)
     * @param maxRequests (cuántas solicitudes permitimos en el período de tiempo)
     * @param windowSeconds (ventana de tiempo en segundos para contar las solicitudes)
     * @return ({@return true} si la solicitud está permitida, {@return false} si se excedió el límite)
     */
    public boolean isAllowed(String key, int maxRequests, int windowSeconds) {

        // Incrementa el contador para esta clave (ej: IP) y devuelve el nuevo valor
        Long count = redisTemplate.opsForValue().increment(key);

        // Si es la primera vez que se incrementa (count == 1), establece el tiempo de expiración para esta clave
        if (count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(windowSeconds));
        }

        // Si el contador es menor o igual al máximo permitido, la solicitud está permitida; de lo contrario, se bloquea
        return count <= maxRequests;
    }

}
