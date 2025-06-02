package com.forum.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Report {
    private Long id;
    private String type;
    private Long targetId;
    private String content;
    private String reportedBy;
    private String title; // 仅post有

    private Long authorId;
    private Integer Count;
    private String status;
    private LocalDateTime reportedAt;
}
