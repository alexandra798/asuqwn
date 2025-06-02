// LoginServiceImpl.java
package com.forum.service.impl;

import com.forum.exception.BusinessException;
import com.forum.mapper.UserMapper;
import com.forum.model.entity.User;
import com.forum.model.user.dto.LoginDTO;
import com.forum.service.LoginService;
import com.forum.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final StringRedisTemplate redisTemplate;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        // 查询用户
        User user = userMapper.findByEmail(loginDTO.getEmail());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("密码错误");
        }

        // 检查用户状态
        if (!user.getIsActivated()) {
            throw new BusinessException("账号未激活");
        }
        if (user.getIsBanned()) {
            throw new BusinessException("账号已被封禁");
        }

        // 生成token
        String token = jwtUtils.generateJwtToken(user.getEmail());

        // 将token存入Redis
        redisTemplate.opsForValue().set("token:" + user.getId(), token, 24, TimeUnit.HOURS);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("email", user.getEmail());
        result.put("isPremium", user.getIsPremium());

        log.info("用户登录成功: userId={}, email={}", user.getId(), user.getEmail());

        return result;
    }

    @Override
    public void logout(String token) {
        // 从Redis删除token
        String userId = jwtUtils.getUserNameFromJwtToken(token);
        if (userId != null) {
            redisTemplate.delete("token:" + userId);
            log.info("用户登出成功: userId={}", userId);
        }
    }
}