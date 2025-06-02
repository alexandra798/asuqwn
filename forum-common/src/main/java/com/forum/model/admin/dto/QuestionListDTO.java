package com.forum.model.admin.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionListDTO {
    @Data
    public static class QuestionItem {
        private Long questionId; // This seems to be an existing question ID for update, or not used if creating new
        private String description;
        private String type;
        private String options;
        // For new questions, you'd likely have more fields like correct_answer, score etc.
        // Matching the Question entity might be better here for adding new questions.
    }
    private List<QuestionItem> questions;

    //这是录入问题使用的表单
}
