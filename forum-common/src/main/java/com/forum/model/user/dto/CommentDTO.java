package com.forum.model.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDTO {
    @NotBlank(message = "评论内容不能为空")
    private String content;
    
    private Long postId;  // 所属帖子ID
    
    private Long userId;  // 评论者ID，将由系统设置，不需要前端传入
    
    private Integer replyToFloor;  // 回复的楼层号，如果不是回复则为null
}
