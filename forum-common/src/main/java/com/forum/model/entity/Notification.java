package com.forum.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Notification {
    private Long id;
    private Long userId;
    private String content;
    private Long postId;
    private Long commentId;
    private String type;
    private Boolean isRead;
    private LocalDateTime createdAt;
}