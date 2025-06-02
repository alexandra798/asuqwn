package com.forum.service;


import com.forum.model.entity.Report;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

import java.util.List;

public interface ManageReportsService {
    List<Report> getReportedPostsByDate(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate);
    List<Report> getReportedCommentsByDate(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate);

    void hidePost(Long reportId);
    void hideComment(Long reportId);

    void ignorePost(Long reportId);
    void ignoreComment(Long reportId);

    void BanUserByPost(Long postId);
    void BanUserByComment(Long commentId);
}
//commentId应当是绝对id还是相对单个post的相对id？
//ignore的实现：忽略一条举报信息，而不是忽略针对一个帖子/评论的所有举报
//举报计数还是挺难实现的
