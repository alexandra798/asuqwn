package com.forum.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {
    private final Long userId;
    private final String content;
    private final Long postId;
    private final Long commentId;
    private final String type; // REPLY_POST, REPLY_COMMENT

    public NotificationEvent(Object source, Long userId, String content,
                             Long postId, Long commentId, String type) {
        super(source);
        this.userId = userId;
        this.content = content;
        this.postId = postId;
        this.commentId = commentId;
        this.type = type;
    }
}
