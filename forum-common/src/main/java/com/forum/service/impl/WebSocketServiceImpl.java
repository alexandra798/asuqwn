// WebSocketServiceImpl.java
package com.forum.service.impl;

import com.forum.model.entity.Notification;
import com.forum.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotificationToUser(Long userId, Notification notification) {
        try {
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/queue/notifications",
                    notification
            );
            log.info("WebSocket通知发送成功: userId={}", userId);
        } catch (Exception e) {
            log.error("WebSocket通知发送失败: userId={}", userId, e);
        }
    }

    @Override
    public void sendMessageToUser(Long userId, String message) {
        try {
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/queue/messages",
                    message
            );
        } catch (Exception e) {
            log.error("WebSocket消息发送失败: userId={}", userId, e);
        }
    }
}