package com.nanakusa.zanshin.filter;

import com.nanakusa.zanshin.service.IPService;
import com.nanakusa.zanshin.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;
    private final IPService ipService;

    public RateLimitFilter(RateLimiterService rateLimiterService, IPService ipService) {
        this.rateLimiterService = rateLimiterService;
        this.ipService = ipService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String key = "auth:rate_limit:" + ipService.getClientIp(request) + ":" + request.getRequestURI();

        boolean allowed = rateLimiterService.isAllowed(key, 5, 600);

        if (!allowed) {
            response.setStatus(429);
            response.getWriter().write("Too many requests: limit is 5 requests per 10 minutes.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
