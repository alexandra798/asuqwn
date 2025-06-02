package com.forum.controller;

import com.forum.exception.BusinessException;
import com.forum.model.user.dto.TestAnswerDTO;
import com.forum.service.TestService;
import com.forum.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/submit")
    public Result<?> submitAnswers(@RequestBody TestAnswerDTO testAnswerDTO, Long questionId) {
        try {
            testService.submitAnswers(testAnswerDTO, questionId);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}

