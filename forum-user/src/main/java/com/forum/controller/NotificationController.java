package com.forum.controller;

import com.forum.model.vo.NotificationVO;
import com.forum.service.NotificationService;
import com.forum.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/api/notifications/{userId}")
    public Result<List<NotificationVO>> getNotifications(@PathVariable Long userId) {
        List<NotificationVO> notifications = notificationService.getNotifications(userId);
        return Result.success(notifications);
    }
}