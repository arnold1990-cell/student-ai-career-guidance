package com.edutech.security;

import com.edutech.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
public class JwtService {

    private final JwtProperties properties;
    private final SecretKey secretKey;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        Set<String> roles = user.getRoles().stream().map(role -> role.getName().name()).collect(java.util.stream.Collectors.toSet());
        return generateToken(user.getEmail(), properties.accessTokenExpirationMs(), Map.of("roles", roles, "type", "access"));
    }

    public String generateRefreshToken(User user) {
        return generateToken(user.getEmail(), properties.refreshTokenExpirationMs(), Map.of("type", "refresh"));
    }

    public Claims parseToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public String extractSubject(String token) {
        return parseToken(token).getSubject();
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(parseToken(token).get("type", String.class));
    }

    public long accessTokenExpirationSeconds() {
        return properties.accessTokenExpirationMs() / 1000;
    }

    private String generateToken(String subject, long expirationMs, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(secretKey)
                .compact();
    }
}
