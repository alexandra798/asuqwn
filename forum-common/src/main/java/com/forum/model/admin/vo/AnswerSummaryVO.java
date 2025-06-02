package com.forum.model.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnswerSummaryVO {
    private Long userId;
    private String email; // User's email
    private Long answerListId; // ID of the UserAnswerList
    private LocalDateTime submitTime; // Submission time from UserAnswerList
    private Boolean isReviewed; // Review status from UserAnswerList
    // Add other fields as needed, e.g., total score if calculated by query
}
