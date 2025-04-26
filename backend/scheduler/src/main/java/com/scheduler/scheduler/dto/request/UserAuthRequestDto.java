package com.scheduler.scheduler.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthRequestDto extends AuthRequestDto {
    private String email;
    private String password;
    private String deviceId;

}
