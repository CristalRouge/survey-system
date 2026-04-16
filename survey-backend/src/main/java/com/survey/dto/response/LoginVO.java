package com.survey.dto.response;

import lombok.Data;

@Data
public class LoginVO {
    private String token;
    private String refreshToken;
    private Long expiresIn;
    private UserVO user;
}
