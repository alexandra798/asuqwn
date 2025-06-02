package com.forum.controller;

import com.forum.model.admin.dto.QuestionListDTO;
import com.forum.model.admin.dto.ReviewDTO;
import com.forum.service.ReviewAnswersService;
import com.forum.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users/answers")
public class ReviewAnswersController {

    @Autowired
    private ReviewAnswersService reviewAnswersService;

    @GetMapping
    public Result<?> getAnswersList(
            @RequestParam String status,Integer pageNum, Integer pageSize,
            @RequestHeader("Admin-Token") String adminToken) {
        return Result.success(reviewAnswersService.getAnswersList(status,pageNum,pageSize));
    }
    @GetMapping
    public Result<?> getAnswersByUser(@RequestParam Long userId){
        return Result.success(reviewAnswersService.getAnswersByUser(userId));
    }

    @PostMapping
    public Result<?> addQuestion(QuestionListDTO questionListDTO){
        reviewAnswersService.addQuestion(questionListDTO);
        return Result.success();
    }

    @PostMapping
    public Result<?> submitReviewing(ReviewDTO reviewDTO){
        reviewAnswersService.submitReviewing(reviewDTO);
        return Result.success();
    }
}
