package com.scheduler.scheduler.service;

import com.scheduler.scheduler.dto.UserAuthRequestDto;
import com.scheduler.scheduler.dto.UserResponseDto;
import com.scheduler.scheduler.entity.User;
import com.scheduler.scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public UserResponseDto register(UserAuthRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new UserResponseDto(true, "Email already exists", null, null);
        }

        User user = new User(request.getEmail(), request.getPassword());

        userRepository.save(user);

        return new UserResponseDto(true, "Successfully register with given email", user.getId(), user.getEmail());
    }
}
