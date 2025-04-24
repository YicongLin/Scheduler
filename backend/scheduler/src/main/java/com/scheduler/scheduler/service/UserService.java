package com.scheduler.scheduler.service;

import com.scheduler.scheduler.dto.UserAuthRequestDto;
import com.scheduler.scheduler.dto.UserResponseDto;
import com.scheduler.scheduler.entity.Profile;
import com.scheduler.scheduler.entity.User;
import com.scheduler.scheduler.repository.UserRepository;
import com.scheduler.scheduler.util.JwtUtil;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserResponseDto register(UserAuthRequestDto request) {
        String email = request.getEmail();

        if (userRepository.existsByEmail(email)) {
            return new UserResponseDto(false, "Email already exists", null);
        }
        
        Profile profile = Profile.builder()
            .userName(email)
            .avatarUrl("/image/default.jpg")
            .bio("")
            .build();

        User user = User.builder()
            .email(email)
            .password(passwordEncoder.encode(request.getPassword()))
            .profile(profile)
            .build();

        userRepository.save(user);

        return new UserResponseDto(true, "Successfully register with given email", jwtUtil.generateToken(user.getId(), user.getEmail()));
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

        return new UserResponseDto(true, "Login successful", jwtUtil.generateToken(user.getId(), user.getEmail()));
    }
    
}
