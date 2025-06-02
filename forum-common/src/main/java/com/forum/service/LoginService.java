package com.forum.service;

import com.forum.model.user.dto.LoginDTO;
import java.util.Map;

public interface LoginService {

    Map<String, Object> login(LoginDTO loginDTO);

    void logout(String token);
}