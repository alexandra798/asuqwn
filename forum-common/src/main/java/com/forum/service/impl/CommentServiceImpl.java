package com.forum.service.impl;

import com.forum.exception.BusinessException;
import com.forum.mapper.CommentMapper;
import com.forum.mapper.PostMapper;
import com.forum.model.entity.Comment;
import com.forum.model.user.dto.CommentDTO;
import com.forum.model.user.vo.CommentVO;
import com.forum.service.CommentService;
import com.forum.service.NotificationService;
import com.forum.utils.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final PostMapper postMapper;
    private final NotificationService notificationService;

    private static final int REPORT_THRESHOLD = 5;

    @Override
    public PageResult<CommentVO> getCommentsByPost(Long postId, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;

        List<Comment> comments = commentMapper.selectByPostId(postId, offset, pageSize);
        Long total = commentMapper.countByPostId(postId);

        List<CommentVO> voList = comments.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(voList, total, pageNum, pageSize);
    }

    @Override
    @Transactional
    public CommentVO createComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setUserId(commentDTO.getUserId());
        comment.setPostId(commentDTO.getPostId());
        comment.setReplyToFloor(commentDTO.getReplyToFloor());
        comment.setReportCount(0);
        comment.setIsHidden(false);
        comment.setStatus(1);
        comment.setCreatedAt(LocalDateTime.now());

        // 计算楼层号
        Integer maxFloor = commentMapper.getMaxFloorByPostId(commentDTO.getPostId());
        comment.setFloorNumber(maxFloor == null ? 1 : maxFloor + 1);

        commentMapper.insert(comment);

        // 更新帖子的评论数
        postMapper.incrementFloorCount(commentDTO.getPostId());

        // 发送通知
        if (commentDTO.getReplyToFloor() != null) {
            // 回复评论通知
            Comment replyToComment = commentMapper.selectByPostIdAndFloor(
                    commentDTO.getPostId(), commentDTO.getReplyToFloor()
            );
            if (replyToComment != null) {
                notificationService.ReplyToCommentNotification(
                        commentDTO.getUserId(), replyToComment.getId(), comment.getId()
                );
            }
        } else {
            // 回复帖子通知
            notificationService.ReplyToPostNotification(
                    commentDTO.getUserId(), commentDTO.getPostId(), comment.getId()
            );
        }

        return convertToVO(comment);
    }

    @Override
    @Transactional
    public void reportComment(Long postId, Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }

        // 增加举报次数
        comment.setReportCount(comment.getReportCount() + 1);
        log.info("评论 (ID: {}) 举报次数预更新为: {}", commentId, comment.getReportCount());

        // 更新数据库中的举报次数，确保 collapseComment 能读取到最新值
        commentMapper.update(comment);
        log.info("评论 (ID: {}) 举报次数已在数据库中更新为: {}", commentId, comment.getReportCount());


        // 调用 collapseComment 方法，由其内部逻辑判断是否需要折叠
        this.collapseComment(commentId);
    }

    @Override
    // 注意：此方法若仅由 reportComment（已@Transactional）通过 `this.`调用，
    // 它将参与现有事务。若希望它能独立形成事务或有特定传播行为，可添加@Transactional注解。
    public void collapseComment(Long commentId) {
        Comment comment = commentMapper.selectById(commentId);

        if (comment == null) {
            // 如果评论不存在，则记录警告并返回，不执行任何操作
            log.warn("尝试折叠一个不存在的评论: commentId={}", commentId);
            return;
        }

        log.debug("检查评论 (ID: {}) 是否需要折叠。当前举报次数: {}, 是否已隐藏: {}",
                commentId, comment.getReportCount(), comment.getIsHidden());

        // 检查举报次数是否达到阈值，并且评论当前未被隐藏
        if (comment.getReportCount() >= REPORT_THRESHOLD && !comment.getIsHidden()) {
            comment.setIsHidden(true);
            commentMapper.update(comment);
            log.info("评论 (ID: {}) 因举报次数 ({}) 达到阈值 {} 已被折叠。",
                    commentId, comment.getReportCount(), REPORT_THRESHOLD);
        } else if (comment.getIsHidden()) {
            log.info("评论 (ID: {}) 已处于折叠状态，举报次数: {} (阈值: {})。",
                    commentId, comment.getReportCount(), REPORT_THRESHOLD);
        } else { // reportCount < REPORT_THRESHOLD && !comment.getIsHidden()
            log.info("评论 (ID: {}) 举报次数 ({}) 未达到阈值 {}，无需折叠。",
                    commentId, comment.getReportCount(), REPORT_THRESHOLD);
        }
    }


    private CommentVO convertToVO(Comment comment) {
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setContent(comment.getContent());
        vo.setAuthorName(comment.getAuthorName());
        vo.setCreatedAt(comment.getCreatedAt());

        // 查找回复此评论的楼层
        List<Integer> replyFloors = commentMapper.getReplyFloors(
                comment.getPostId(), comment.getFloorNumber()
        );
        vo.setReplyBy(replyFloors);

        return vo;
    }
}