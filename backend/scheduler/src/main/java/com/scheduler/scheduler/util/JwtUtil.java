package com.scheduler.scheduler.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.util.Map;
import java.util.Set;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtil {
    private Key key;

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expirationMs}")
    private long jwtExpiration;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Long userId, Map<String, Object> claims) {

        JwtBuilder builder = Jwts.builder()
            .setSubject(userId.toString())
            .setIssuer("scheduler")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(key, SignatureAlgorithm.HS256);

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
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return null;
        } catch (MalformedJwtException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return getClaimFromToken(token,  Claims.EXPIRATION, Date.class).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public Long extractUserId(String token) {
        try {
            return Long.parseLong(extractAllClaims(token).getSubject());
        } catch (Exception e) {
            return null;
        }
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
        } catch (ExpiredJwtException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
