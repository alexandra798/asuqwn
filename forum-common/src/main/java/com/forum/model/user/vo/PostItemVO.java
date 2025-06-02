package com.forum.model.user.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostItemVO {

    private Long postId;

    private String title;

    private String authorName;

    private String categoryName;

    private Integer commentCount;

    private Integer viewCount;

    private Boolean isHidden;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}

