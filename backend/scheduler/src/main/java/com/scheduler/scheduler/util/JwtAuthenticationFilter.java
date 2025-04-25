package com.scheduler.scheduler.util;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.scheduler.scheduler.service.SessionManager;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final SessionManager sessionManager;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, SessionManager sessionManager) {
        this.jwtUtil = jwtUtil;
        this.sessionManager = sessionManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token).toString();
            String sessionId = jwtUtil.getClaimFromToken(token, "sessionId", String.class);

            if (jwtUtil.isTokenExpired(token) || !sessionManager.isSessionValid(userId, sessionId)) {

                sessionManager.removeSessionBySession(userId, sessionId);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
