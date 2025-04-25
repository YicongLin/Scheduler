package com.scheduler.scheduler.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scheduler.scheduler.dto.ProfileResponseDto;
import com.scheduler.scheduler.dto.UserResponseDto;
import com.scheduler.scheduler.entity.Profile;
import com.scheduler.scheduler.entity.User;
import com.scheduler.scheduler.repository.ProfileRepository;
import com.scheduler.scheduler.repository.UserRepository;
import com.scheduler.scheduler.util.JwtUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SettingService {
    private final ProfileRepository profileRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ProfileResponseDto getUserProfile(String token) {

        Optional<Profile> optionalProfile = profileRepository.findByUserId(jwtUtil.extractUserId(token));

        if (optionalProfile.isEmpty()) {
            return new ProfileResponseDto(false, "Profile not found", null, null, null);
        }

        Profile profile = optionalProfile.get();

        return new ProfileResponseDto(true, "Profile has founded", profile.getUserName(), profile.getBio(), profile.getAvatarUrl());
    }
   

    
}
