package com.forum.model.user.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationVO {
    private Long id;
    private String content;
    private Long postId;
    private Long commentId;
    private LocalDateTime createdAt;
    private Boolean isRead;
}