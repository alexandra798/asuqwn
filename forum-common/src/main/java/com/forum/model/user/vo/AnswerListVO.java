package com.forum.model.user.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AnswerListVO {
    @Data
    public static class AnswerItem{
        private String questionId;
        private String description;
        private String type;
        private String options;
        private String answer;
        private Integer score;
    }
    private LocalDateTime submitTime;
    private Integer totalScore;
    private List<AnswerItem> answers;
}
