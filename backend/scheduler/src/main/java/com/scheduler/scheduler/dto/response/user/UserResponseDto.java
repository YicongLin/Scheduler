package com.scheduler.scheduler.dto.response.user;


import com.scheduler.scheduler.dto.response.ResponseDto;
import com.scheduler.scheduler.entity.Profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto extends ResponseDto {
    private String token;

     public UserResponseDto(String message, String token) {
        super(token != null, message);
        this.token = token;
    }
  
    
}
