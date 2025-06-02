// WebSocketService.java
package com.forum.service;

import com.forum.model.entity.Notification;

public interface WebSocketService {
    void sendNotificationToUser(Long userId, Notification notification);
    void sendMessageToUser(Long userId, String message);
}
