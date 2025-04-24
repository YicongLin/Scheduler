package com.scheduler.scheduler.dto;

public class UserResponseDto extends ResponseDto {
    private String token;

    public UserResponseDto(boolean success, String message, String token) {
        super(success, message);
        this.token = token;
    }
    
}
