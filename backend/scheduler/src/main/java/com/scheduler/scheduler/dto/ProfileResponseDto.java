package com.scheduler.scheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponseDto extends ResponseDto{

    private String userName;
    private String bio;
    private String avatarUrl;
    
    public ProfileResponseDto(boolean success, String message, 
            String userName, String bio, String avatarUrl) {

        super(success, message);
        this.userName = userName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
    }
  
    
}
