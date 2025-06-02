package com.forum.model.user.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String password;
    private String captchaCode;
}
