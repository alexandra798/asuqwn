// RegisterServiceImpl.java
package com.forum.service.impl;

import com.forum.exception.BusinessException;
import com.forum.mapper.UserMapper;
import com.forum.model.entity.User;
import com.forum.model.user.dto.RegisterDTO;
import com.forum.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void sendVerificationCode(String email) {
        // 检查邮箱是否已注册
        User existingUser = userMapper.findByEmail(email);
        if (existingUser != null) {
            throw new BusinessException("该邮箱已被注册");
        }

        // 生成验证码
        String code = String.format("%06d", new Random().nextInt(999999));

        // 存入Redis，5分钟有效
        redisTemplate.opsForValue().set("verify_code:" + email, code, 5, TimeUnit.MINUTES);

        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("论坛注册验证码");
        message.setText("您的验证码是：" + code + "，5分钟内有效。");

        try {
            mailSender.send(message);
            log.info("验证码发送成功: email={}", email);
        } catch (Exception e) {
            log.error("验证码发送失败", e);
            throw new BusinessException("验证码发送失败，请稍后重试");
        }
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String savedCode = redisTemplate.opsForValue().get("verify_code:" + email);
        if (savedCode == null) {
            throw new BusinessException("验证码已过期");
        }
        return savedCode.equals(code);
    }

    @Override
    @Transactional
    public Long register(RegisterDTO registerDTO) {
        // 验证验证码
        if (!verifyCode(registerDTO.getEmail(), registerDTO.getCaptchaCode())) {
            throw new BusinessException("验证码错误");
        }

        // 创建用户
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getEmail().split("@")[0]); // 默认用户名
        user.setPasswordHash(passwordEncoder.encode(registerDTO.getPassword()));
        user.setIsActivated(false); // 需要通过入站测试后激活
        user.setIsMuted(false);
        user.setIsBanned(false);
        user.setIsPremium(false);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);

        // 删除验证码
        redisTemplate.delete("verify_code:" + registerDTO.getEmail());

        log.info("用户注册成功: userId={}, email={}", user.getId(), user.getEmail());

        return user.getId();
    }

    @Override
    public boolean getActivationNews(Long userId) {
        User user = userMapper.findById(userId);
        return user != null && user.getIsActivated();
    }
}