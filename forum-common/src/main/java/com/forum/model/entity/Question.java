package com.forum.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Question {
    private Long id;
    private String description;
    private String correctAnswer;
    private Integer score;  // 这道题的分值
    private Integer timeLimit;  // 答题时限（秒）
    private String category;  // 题目类别，如：Java基础、Spring等
    private Integer difficulty;  // 难度等级：1-简单，2-中等，3-困难
    private LocalDateTime createdAt;
    private Long createdBy;
    private Boolean isEnabled;  // 是否启用
}
