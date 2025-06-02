package com.forum.model.user.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CommentVO {
    private Long id;
    private String content;
    private String authorName;

    //private Integer replyToFloor; replyToFloor的信息包含在content里面呀

    private List<Integer> replyBy;  // 回复此评论的楼层号列表
    private LocalDateTime createdAt;
    //private Boolean isCollapsed;
}
