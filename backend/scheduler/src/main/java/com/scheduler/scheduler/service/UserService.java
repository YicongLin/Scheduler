package com.scheduler.scheduler.service;

import com.scheduler.scheduler.dto.UserAuthRequestDto;
import com.scheduler.scheduler.dto.UserResponseDto;
import com.scheduler.scheduler.entity.Profile;
import com.scheduler.scheduler.entity.User;
import com.scheduler.scheduler.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto register(UserAuthRequestDto request) {
        String email = request.getEmail();

        if (userRepository.existsByEmail(email)) {
            return new UserResponseDto(false, "Email already exists", null, null);
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

        return new UserResponseDto(true, "Successfully register with given email", user.getId(), user.getEmail());
    }

    
}
