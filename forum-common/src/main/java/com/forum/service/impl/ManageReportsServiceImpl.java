// ManageReportsServiceImpl.java
package com.forum.service.impl;

import com.forum.exception.BusinessException;
import com.forum.mapper.CommentMapper;
import com.forum.mapper.PostMapper;
import com.forum.mapper.ReportMapper;
import com.forum.mapper.UserMapper;
import com.forum.model.entity.Comment;
import com.forum.model.entity.Post;
import com.forum.model.entity.Report;
import com.forum.model.entity.User;
import com.forum.service.ManageReportsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManageReportsServiceImpl implements ManageReportsService {

    private final ReportMapper reportMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    @Override
    public List<Report> getReportedPostsByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return reportMapper.findByTypeAndDateRange("POST", startDate, endDate);
    }

    @Override
    public List<Report> getReportedCommentsByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return reportMapper.findByTypeAndDateRange("COMMENT", startDate, endDate);
    }

    @Override
    @Transactional
    public void hidePost(Long reportId) {
        Report report = reportMapper.selectById(reportId);
        if (report == null) {
            throw new BusinessException("举报记录不存在");
        }
        if (!"POST".equals(report.getType())) {
            throw new BusinessException("该举报不是针对帖子的");
        }

        Post post = postMapper.selectById(report.getTargetId());
        if (post != null) {
            post.setIsHidden(true);
            postMapper.update(post);

            // 更新举报状态
            report.setStatus("PROCESSED");
            reportMapper.update(report);

            log.info("帖子已隐藏: postId={}, reportId={}", post.getId(), reportId);
        }
    }

    @Override
    @Transactional
    public void hideComment(Long reportId) {
        Report report = reportMapper.selectById(reportId);
        if (report == null) {
            throw new BusinessException("举报记录不存在");
        }
        if (!"COMMENT".equals(report.getType())) {
            throw new BusinessException("该举报不是针对评论的");
        }

        Comment comment = commentMapper.selectById(report.getTargetId());
        if (comment != null) {
            comment.setIsHidden(true);
            commentMapper.update(comment);

            // 更新举报状态
            report.setStatus("PROCESSED");
            reportMapper.update(report);

            log.info("评论已隐藏: commentId={}, reportId={}", comment.getId(), reportId);
        }
    }

    @Override
    @Transactional
    public void ignorePost(Long reportId) {
        Report report = reportMapper.selectById(reportId);
        if (report != null && "POST".equals(report.getType())){
            updateReportStatus(reportId, "IGNORED");
        }
    }

    @Override
    @Transactional
    public void ignoreComment(Long reportId) {
        Report report = reportMapper.selectById(reportId);
        if (report != null && "COMMENT".equals(report.getType())){
            updateReportStatus(reportId, "IGNORED");
        }
    }

    @Override
    @Transactional
    public void BanUserByPost(Long postId) {
        Post post = postMapper.selectById(postId);
        if (post != null) {
            banUser(post.getUserId());
        }
    }

    @Override
    @Transactional
    public void BanUserByComment(Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment != null) {
            banUser(comment.getUserId());
        }
    }

    private void updateReportStatus(Long reportId, String status) {
        Report report = reportMapper.selectById(reportId);
        if (report != null) {
            report.setStatus(status);
            reportMapper.update(report);
            log.info("举报状态更新: reportId={}, status={}", reportId, status);
        }
    }

    private void banUser(Long userId) {
        User user = userMapper.findById(userId);
        if (user != null) {
            user.setIsBanned(true);
            userMapper.updateBanStatus(userId, true, LocalDateTime.now());
            log.info("用户已封禁: userId={}", userId);
        }
    }
}
