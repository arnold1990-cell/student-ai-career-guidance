package com.edutech.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int LIMIT_PER_MINUTE = 120;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        long currentMinute = Instant.now().getEpochSecond() / 60;

        Bucket bucket = buckets.computeIfAbsent(clientIp, ip -> new Bucket(currentMinute, new AtomicInteger(0)));
        synchronized (bucket) {
            if (bucket.minute != currentMinute) {
                bucket.minute = currentMinute;
                bucket.counter.set(0);
            }
            if (bucket.counter.incrementAndGet() > LIMIT_PER_MINUTE) {
                response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Too many requests");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private static class Bucket {
        private volatile long minute;
        private final AtomicInteger counter;

        private Bucket(long minute, AtomicInteger counter) {
            this.minute = minute;
            this.counter = counter;
        }
    }
}
