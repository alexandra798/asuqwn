// 修复PostController.java
package com.forum.controller;

import com.forum.exception.BusinessException;
import com.forum.model.user.dto.PostDTO;
import com.forum.model.user.query.PostQueryDTO;
import com.forum.model.user.vo.PostDetailVO;
import com.forum.model.user.vo.PostItemVO;
import com.forum.service.PostService;
import com.forum.utils.PageResult;
import com.forum.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts")
    public Result<?> createPost(@RequestBody PostDTO postDTO) {
        try {
            postService.createPost(postDTO);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/api/posts/{id}")
    public Result<PageResult<PostDetailVO>> getPostDetail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            PageResult<PostDetailVO> post = postService.getPostDetail(id, pageNum, pageSize);
            return Result.success(post);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/api/home/{categoryId}")
    public Result<PageResult<PostItemVO>> getPostsByCategory(
            @PathVariable(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            PostQueryDTO postQueryDTO = new PostQueryDTO();
            postQueryDTO.setType(String.valueOf(categoryId));
            postQueryDTO.setPageNo(pageNum);
            postQueryDTO.setPageSize(pageSize);

            PageResult<PostItemVO> posts = postService.getPostsByCategory(categoryId, postQueryDTO);
            return Result.success(posts);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/api/home")
    public Result<PageResult<PostItemVO>> getPosts(
            @RequestParam(defaultValue = "0") Long categoryId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            PostQueryDTO postQueryDTO = new PostQueryDTO();
            postQueryDTO.setType(String.valueOf(categoryId));
            postQueryDTO.setPageNo(pageNum);
            postQueryDTO.setPageSize(pageSize);

            PageResult<PostItemVO> posts = postService.getPostsByCategory(categoryId, postQueryDTO);
            return Result.success(posts);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/api/posts/{id}/report")
    public Result<?> reportPost(@PathVariable Long id) {
        try {
            postService.reportPost(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/api/posts/{postId}/update")
    public Result<PostItemVO> updatePost(
            @PathVariable Long postId,
            @RequestBody PostDTO postDTO) {
        try {
            PostItemVO updated = postService.updatePost(postId, postDTO);
            return Result.success(updated);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}