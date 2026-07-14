package com.nanakusa.zanshin.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class IPService {

    public String getClientIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        } else {
            // Si hay múltiples IPs (proxy chain), tomar la primera👁👈👁️
            ip = ip.split(",")[0];
        }

        return ip;
    }
}
