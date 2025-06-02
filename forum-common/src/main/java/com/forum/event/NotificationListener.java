package com.forum.event;

import com.forum.model.entity.Notification;
import com.forum.service.NotificationService;
import com.forum.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;
    private final WebSocketService webSocketService;

    @Async
    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        try {
            // 1. 保存通知到数据库
            Notification notification = new Notification();
            notification.setUserId(event.getUserId());
            notification.setContent(event.getContent());
            notification.setPostId(event.getPostId());
            notification.setCommentId(event.getCommentId());
            notification.setType(event.getType());
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationService.saveNotification(notification);

            // 2. 通过WebSocket推送实时通知
            webSocketService.sendNotificationToUser(event.getUserId(), notification);

            log.info("通知发送成功: userId={}, content={}", event.getUserId(), event.getContent());
        } catch (Exception e) {
            log.error("通知发送失败", e);
        }
    }
}