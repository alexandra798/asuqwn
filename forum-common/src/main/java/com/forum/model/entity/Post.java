package com.forum.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Post {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Long categoryId;
    private Integer floorCount;
    private Integer reportCount;
    private Integer viewCount;
    private Boolean isHidden;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 非数据库字段，用于显示
    private String authorName;
    private String categoryName;
}
