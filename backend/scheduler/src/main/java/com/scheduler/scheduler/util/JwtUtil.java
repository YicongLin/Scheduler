package com.scheduler.scheduler.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Map;
import java.util.Set;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${security.jwt.expirationMs}")
    private long jwtExpiration;

    public String generateToken(Long userId, Map<String, Object> claims) {

        JwtBuilder builder = Jwts.builder()
            .setSubject(userId.toString())
            .setIssuer("scheduler")  
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(key);

            if (claims != null) {
                claims.forEach((key, value) -> {
                    if (!isReservedClaim(key)) {
                        builder.claim(key, value);
                    }
                });
            }
        
            return builder.compact();
    }

    private boolean isReservedClaim(String claimName) {
        // Standard JWT registered claim names
        final Set<String> reservedClaims = Set.of(
            "iss", "sub", "aud", "exp", "nbf", "iat", "jti"
        );
        return reservedClaims.contains(claimName);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !isTokenExpired(claims);
        } catch (Exception e) {
            return false; // Invalid signature
        }
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token).getSubject());
    }

    /**
     * e.g. 
     *  1: Date expiration = jwtUtil.getClaimFromToken(token, Claims.EXPIRATION, Date.class);
     *  2: Int role = jwtUtil.getClaimFromToken(token, "role", Integer.class); (in future iteration)
     */
    public <T> T getClaimFromToken(String token, String claimName, Class<T> type) {
        try {
            final Claims claims = extractAllClaims(token);
            return claims.get(claimName, type);
        } catch (Exception e) {
            return null;
        }
    }
}
