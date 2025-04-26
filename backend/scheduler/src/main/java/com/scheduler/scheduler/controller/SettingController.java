package com.scheduler.scheduler.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.scheduler.scheduler.dto.response.profile.ProfileResponseDto;
import com.scheduler.scheduler.service.SettingService;


@RestController
@RequestMapping("/setting")
public class SettingController {
    
    @Autowired
    private SettingService settingService;

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
    
}
