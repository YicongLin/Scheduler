package com.scheduler.scheduler.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.scheduler.scheduler.dto.response.profile.ProfileResponseDto;
import com.scheduler.scheduler.dto.response.user.UserResponseDto;
import com.scheduler.scheduler.service.SettingService;
import com.scheduler.scheduler.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/setting")
@RequiredArgsConstructor
public class SettingController {
    
    private final SettingService settingService;
    private final UserService userService;

    @GetMapping("/profile")
    public ProfileResponseDto viewProfile(@RequestHeader("Authorization") String authorizationHeader) {
        return settingService.getUserProfile(authorizationHeader.substring(7));
    }

    @PostMapping("/upload-avatar")
    public ProfileResponseDto uploadAvatar(@RequestHeader("Authorization") String authorizationHeader,
                                                @RequestParam("image") MultipartFile file) {
        String token = authorizationHeader.substring(7);
        return settingService.uploadAvatar(token, file);
    }

    @PostMapping("/upload-bio")
    public ProfileResponseDto uploadBio(@RequestHeader("Authorization") String authorizationHeader,
                                                @RequestParam("bio") String bio) {
        String token = authorizationHeader.substring(7);
        return settingService.uploadBio(token, bio);
    }

    @PostMapping("/upload-userName")
    public ProfileResponseDto uploadUserName(@RequestHeader("Authorization") String authorizationHeader,
                                                @RequestParam("userName") String userName) {
        String token = authorizationHeader.substring(7);
        return settingService.uploadUserName(token, userName);
    }

    @PostMapping("/reset-password")
    public UserResponseDto resetPassword(@RequestHeader("Authorization") String authorizationHeader,
                                                @RequestParam("old-password") String oldPassword,
                                                @RequestParam("new-password") String newPassword) {
        String token = authorizationHeader.substring(7);
        return userService.resetPassword(token, oldPassword, newPassword);
    }
    
}
