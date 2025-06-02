// 修复CommentController.java
package com.forum.controller;

import com.forum.exception.BusinessException;
import com.forum.model.user.dto.CommentDTO;
import com.forum.model.user.vo.CommentVO;
import com.forum.service.CommentService;
import com.forum.utils.PageResult;
import com.forum.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    public Result<PageResult<CommentVO>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            PageResult<CommentVO> comments = commentService.getCommentsByPost(postId, pageNum, pageSize);
            return Result.success(comments);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/posts/{postId}/comment")
    public Result<CommentVO> createComment(
            @PathVariable Long postId,
            @RequestBody CommentDTO commentDTO) {
        try {
            commentDTO.setPostId(postId);
            CommentVO comment = commentService.createComment(commentDTO);
            return Result.success(comment);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/posts/{postId}/comment-report/{commentId}")
    public Result<?> reportComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        try {
            commentService.reportComment(postId, commentId);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}