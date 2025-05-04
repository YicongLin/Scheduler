package com.scheduler.scheduler.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserAuthRequestDto extends AuthRequestDto {
    private String email;
    private String password;
    private String deviceId;

}
