package com.scheduler.scheduler.service;

import com.scheduler.scheduler.dto.request.UserAuthRequestDto;
import com.scheduler.scheduler.dto.response.user.UserResponseDto;
import com.scheduler.scheduler.entity.Profile;
import com.scheduler.scheduler.entity.User;
import com.scheduler.scheduler.repository.UserRepository;
import com.scheduler.scheduler.util.JwtUtil;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;

import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SessionManager sessionManager;

    @Value("${spring.file.image-dir}")
    private String defaultImagePath;

    private String generateTokenWithSession(User user, UserAuthRequestDto request) {
        Long userId = user.getId();

        String sessionId = sessionManager.createSession(userId.toString(), request.getDeviceId());

        if (sessionId == null) {
            return null;
        }

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("sessionId", sessionId);

        return jwtUtil.generateToken(userId, request.toClaims(extraClaims));
    }


    /*
     * Frontend redirects users from a registration 
     * page to the main page after successful registration
     */

    @Transactional
    public UserResponseDto register(UserAuthRequestDto request) {
        String email = request.getEmail();
        String username = request.getUsername();

        if (userRepository.existsByEmail(email)) {
            return new UserResponseDto(false, "Email already exists", null);
        }
        
        Profile profile = Profile.builder()
            .userName(username)
            .avatarUrl(defaultImagePath + "/default.jpg")
            .bio("")
            .build();

        User user = User.builder()
            .username(username)
            .email(email)
            .password(passwordEncoder.encode(request.getPassword()))
            .profile(profile)
            .build();

        userRepository.save(user);
        
        return new UserResponseDto(true, "Successfully register with given email", generateTokenWithSession(user, request));
    }

    public UserResponseDto login(UserAuthRequestDto request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return new UserResponseDto(false, "User not found", null);
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new UserResponseDto(false, "Incorrect password", null);
        }

        return new UserResponseDto(true, "Login successfully", generateTokenWithSession(user, request));
    }

    public UserResponseDto logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new UserResponseDto(false, "Invalid authentication header", authHeader);
        }

        String token = authHeader.substring(7);
        try {
            Long userId = jwtUtil.extractUserId(token);
            String sessionId = jwtUtil.getClaimFromToken(token, "sessionId", String.class);
            
            if (sessionId != null) {
                sessionManager.removeAllSessions(userId.toString());
            }
        } catch (Exception e) {
            return new UserResponseDto(false, "Invalid token", token);
        }
        return new UserResponseDto(true, "Logout successfully", null);
    }

    @Transactional
    public UserResponseDto resetPassword(String token, String olderPassword, String newPassword) {
        if (olderPassword == null || newPassword == null 
            || newPassword.length() < 8
            || newPassword.equals(olderPassword)) {
            return new UserResponseDto(false, "Invalid password is given", null);
        }

        Long userId = jwtUtil.extractUserId(token);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return new UserResponseDto(false, "User not found", null);
        }

        User user = optionalUser.get();
        String currPassword = user.getPassword();

        if (!passwordEncoder.matches(olderPassword, currPassword)) {
            return new UserResponseDto(false, "Incorrect password", null);
        }

        user.setPassword(newPassword);
        userRepository.save(user);

        // Invalidate all other valid sessions for given user (except current one)
        sessionManager.removeAllSessionsExceptDevice(userId.toString(), jwtUtil.getClaimFromToken(token, "deviceId", String.class));

        return new UserResponseDto(true, "Successfully update password", null);
    }
    
}
