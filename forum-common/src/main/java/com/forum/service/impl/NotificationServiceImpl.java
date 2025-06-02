package com.forum.service.impl;

import com.forum.event.NotificationEvent;
import com.forum.mapper.NotificationMapper;
import com.forum.model.entity.Notification;
import com.forum.model.user.vo.NotificationVO;
import com.forum.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void ReplyToPostNotification(Long userId, Long postId, Long replyId) {
        // 获取帖子作者ID
        Long authorId = notificationMapper.getPostAuthorId(postId);
        if (authorId.equals(userId)) {
            return; // 不通知自己
        }

        String content = "有人回复了你的帖子";

        // 发布通知事件
        NotificationEvent event = new NotificationEvent(
                this, authorId, content, postId, replyId, "REPLY_POST"
        );
        eventPublisher.publishEvent(event);
    }

    @Override
    @Transactional
    public void ReplyToCommentNotification(Long userId, Long commentId, Long replyId) {
        // 获取被回复评论的作者ID
        Long authorId = notificationMapper.getCommentAuthorId(commentId);
        if (authorId.equals(userId)) {
            return; // 不通知自己
        }

        String content = "有人回复了你的评论";
        Long postId = notificationMapper.getPostIdByCommentId(commentId);

        // 发布通知事件
        NotificationEvent event = new NotificationEvent(
                this, authorId, content, postId, replyId, "REPLY_COMMENT"
        );
        eventPublisher.publishEvent(event);
    }

    @Override
    public List<NotificationVO> getNotifications(Long userId) {
        List<Notification> notifications = notificationMapper.findByUserId(userId);
        return notifications.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public void saveNotification(Notification notification) {
        notificationMapper.insert(notification);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationMapper.updateReadStatus(notificationId, true);
    }

    private NotificationVO convertToVO(Notification notification) {
        NotificationVO vo = new NotificationVO();
        vo.setId(notification.getId());
        vo.setContent(notification.getContent());
        vo.setPostId(notification.getPostId());
        vo.setCommentId(notification.getCommentId());
        vo.setCreatedAt(notification.getCreatedAt());
        vo.setIsRead(notification.getIsRead());
        return vo;
    }
}
