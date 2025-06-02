package com.forum.service;

import com.forum.model.user.dto.RegisterDTO;

public interface RegisterService {
    void sendVerificationCode(String email);
    boolean verifyCode(String email, String code);
    Long register(RegisterDTO registerDTO);

    boolean getActivationNews (Long userId);
    //让后端程序只返回一个true/false，剩下的页面内容就交给前端了^^
}
