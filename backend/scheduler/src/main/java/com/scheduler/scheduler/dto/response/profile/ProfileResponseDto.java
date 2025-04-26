package com.scheduler.scheduler.dto.response.profile;

import com.scheduler.scheduler.dto.response.ResponseDto;
import com.scheduler.scheduler.entity.Profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponseDto extends ResponseDto{

    private String userName;
    private String bio;
    private String avatarUrl;

    public ProfileResponseDto(String message, Profile profile) {
        super(profile != null, message);
    
        if (profile != null) {
            this.userName = profile.getUserName();
            this.bio = profile.getBio();
            this.avatarUrl = profile.getAvatarUrl();
        }
    }
  
}
