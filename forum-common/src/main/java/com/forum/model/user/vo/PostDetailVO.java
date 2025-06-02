package com.forum.model.user.vo;

import lombok.Data;
import java.time.LocalDateTime;
// import java.util.List; // If comments are added later
// import com.forum.model.entity.Comment; // Or CommentVO

@Data
public class PostDetailVO {
    private Long postId;
    private String title;
    private String content;
    private String authorName;
    private String categoryName;
    private Integer commentCount; // Represents floorCount
    private Integer viewCount;
    private Boolean isHidden;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // private List<Comment> comments; // Placeholder if you add comment list later
}
