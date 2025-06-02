package com.forum.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Category {
    private Long id;
    private String name;
    private String guidelines;
    private Integer postCount;
    private LocalDateTime createdAt;
    private Long createdBy;
}
