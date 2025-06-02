package com.forum.model.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewDTO {
    @Data
    public static class Item {
        private Long answerId;
        private Long questionId;
        private Integer score;
    }
    private Long userId;

    private Integer totalTimeUsed;  // 单位：秒

    private LocalDateTime submitTime;
    private LocalDateTime registerTime;
    private Boolean isReviewed;

    private List<Item> answers;
}
