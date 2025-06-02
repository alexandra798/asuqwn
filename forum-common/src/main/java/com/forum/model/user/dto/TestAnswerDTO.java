package com.forum.model.user.dto;

import lombok.Data;
import java.util.List;

@Data
public class TestAnswerDTO {
    private Long userId;                  // 用户ID
    private List<AnswerDetail> answers;   // 答题详情列表
    
    @Data
    public static class AnswerDetail {
        private String answer;            // 答案内容
        private Long questionId;          // 题目ID
        private Integer timeUsed;         // 用时（秒）
        private String type;              // 题目类型：choice/text
    }
}
