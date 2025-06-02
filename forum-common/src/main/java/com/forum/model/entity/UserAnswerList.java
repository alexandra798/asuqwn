package com.forum.model.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserAnswerList {
    private Long id;

    @Data
    public static class UserAnswer {
        private Long id;
        private Long questionId;
        private String answer;
        private String questionDescription;
        private Integer score;
        private Integer timeUsed;
    }

    private Long userId;

    private Integer totalTimeUsed;  // 单位：秒

    private LocalDateTime submitTime;
    private LocalDateTime registerTime;
    private Boolean isReviewed;

    private List<UserAnswer> answers;
}