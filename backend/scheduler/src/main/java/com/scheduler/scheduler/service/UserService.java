package com.scheduler.scheduler.service;

import com.scheduler.scheduler.dto.request.RedeemRequestDto;
import com.scheduler.scheduler.dto.request.UserAuthRequestDto;
import com.scheduler.scheduler.dto.response.user.UserResponseDto;
import com.scheduler.scheduler.entity.Profile;
import com.scheduler.scheduler.entity.User;
import com.scheduler.scheduler.repository.UserRepository;
import com.scheduler.scheduler.util.JwtUtil;

import io.jsonwebtoken.Claims;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    private final EmailService emailService;

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

    @Transactional
    public UserResponseDto sendResetPasswordLink(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return new UserResponseDto(false, "User not found", null);
        }

        User user = optionalUser.get();
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

        user.setResetToken(resetToken);
        user.setResetTokenExpiry(expiry);
        userRepository.save(user);

        String resetLink = "http://localhost:3000/forgotPassword/redeem?token=" + resetToken;
        emailService.sendEmail(email, resetLink);

        return new UserResponseDto(true, "Reset password email sent", null);
    }

    @Transactional
    public UserResponseDto redeemPassword(RedeemRequestDto request) {
        String newPassword = request.getPassword();
        if (newPassword == null || newPassword.length() < 8) {
            return new UserResponseDto(false, "Password must be at least 8 characters", null);
        }

        Optional<User> optionalUser = userRepository.findByResetToken(request.getResetToken());

        if (optionalUser.isEmpty()) {
            return new UserResponseDto(false, "Invalid or expired token", null);
        }

        User user = optionalUser.get();

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return new UserResponseDto(false, "Token has expired", null);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return new UserResponseDto(true, "Password successfully updated", null);
    }
    
}
