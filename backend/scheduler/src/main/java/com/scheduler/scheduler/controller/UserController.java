
package com.scheduler.scheduler.controller;

import com.scheduler.scheduler.dto.UserAuthRequestDto;
import com.scheduler.scheduler.dto.UserResponseDto;
import com.scheduler.scheduler.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserAuthRequestDto request) {
        return userService.register(request);
    }
}
