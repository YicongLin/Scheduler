package com.scheduler.scheduler.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RedeemRequestDto extends AuthRequestDto {
    private String resetToken;
    private String password;
}
