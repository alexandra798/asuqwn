package com.forum.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;


@Data
public class Comment {
    private Long id;
    private String content;
    private Long userId;
    private Long postId;
    private Integer floorNumber;
    private Integer replyToFloor;
    private Integer reportCount;
    private Boolean isHidden;
    private Integer status;
    private LocalDateTime createdAt;
    
    // 非数据库字段
    private String authorName;
    private List<Integer> replyFloors;  // 存储回复此评论的楼层号
    private Boolean isCollapsed;  // 是否被折叠
}
