package com.scheduler.scheduler.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.scheduler.scheduler.dto.response.profile.ProfileResponseDto;
import com.scheduler.scheduler.entity.Profile;
import com.scheduler.scheduler.repository.ProfileRepository;
import com.scheduler.scheduler.util.JwtUtil;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class SettingService {
    private final ProfileRepository profileRepository;
    private final JwtUtil jwtUtil;

    @Value("${spring.file.image-dir}") 
    private String uploadDir;

    @Transactional
    public ProfileResponseDto getUserProfile(String token) {


        Profile profile = findValidProfileByUserId(jwtUtil.extractUserId(token));
        if (profile == null) {
            return new ProfileResponseDto("Profile not found",null);
        }

        return new ProfileResponseDto("Profile has founded", profile);
    }
   
 
    @Transactional
    public ProfileResponseDto uploadAvatar(String token, MultipartFile file) {

        // Validate input parameters
        if (file == null || file.isEmpty()) {
            return new ProfileResponseDto("File cannot be empty", null);
        }

        
        try {
            Long userId = jwtUtil.extractUserId(token);
            Profile profile = findValidProfileByUserId(jwtUtil.extractUserId(token));
            if (profile == null) {
                return new ProfileResponseDto("Profile not found", null);
            }

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = userId + "." + fileExtension;
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName).normalize();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            profile.setAvatarUrl(uploadDir + fileName);
            profileRepository.save(profile);

            return new ProfileResponseDto("Successfully upload new avatar", profile);

        } catch (IOException e) {
            return new ProfileResponseDto("Error uploading file", null);
        }
    }
    
    private Profile findValidProfileByUserId(Long userId) {
        Optional<Profile> optionalProfile = profileRepository.findByUserId(userId);

        if (optionalProfile.isEmpty()) {
            return null;
            
        }
        return optionalProfile.get();
    }


    @Transactional
    public ProfileResponseDto uploadBio(String token, String bio) {

        try {
            Profile profile = findValidProfileByUserId(jwtUtil.extractUserId(token));

            if (profile == null) {
                return new ProfileResponseDto("Profile not found", null);
            }

            profile.setBio(bio);
            profileRepository.save(profile);
            return new ProfileResponseDto("Successfully update bio", profile);
        } catch (Exception e) {
            return new ProfileResponseDto("Unable to upate bio", null);
        }
    }
}
