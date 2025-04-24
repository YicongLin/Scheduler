package com.scheduler.scheduler.dto;

public class ProfileResponseDto extends ResponseDto {
    private Long id;
    private String email;

    public ProfileResponseDto(boolean success, String message, Long id, String email) {
        super(success, message);
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    
}
