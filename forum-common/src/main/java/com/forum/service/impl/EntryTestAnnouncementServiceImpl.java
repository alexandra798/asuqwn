// EntryTestAnnouncementServiceImpl.java
package com.forum.service.impl;

import com.forum.mapper.UserMapper;
import com.forum.model.entity.User;
import com.forum.service.EntryTestAnnouncementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntryTestAnnouncementServiceImpl implements EntryTestAnnouncementService {

    private final UserMapper userMapper;
    private final JavaMailSender mailSender;

    @Override
    public void sendAnnouncement(Long userId, boolean isPass) {
        User user = userMapper.findById(userId);
        if (user == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());

        if (isPass) {
            message.setSubject("恭喜！您已通过入站测试");
            message.setText("很遗憾，您未能通过入站测试。您的账号将被注销，欢迎您以后再次尝试。");
        }

        try {
            mailSender.send(message);
            log.info("入站测试结果通知发送成功: userId={}, isPass={}", userId, isPass);
        } catch (Exception e) {
            log.error("邮件发送失败", e);
        }
    }
}
