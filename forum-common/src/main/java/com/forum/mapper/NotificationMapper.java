// NotificationMapper.java
package com.forum.mapper;

import com.forum.model.entity.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("INSERT INTO notification (user_id, content, post_id, comment_id, " +
            "type, is_read, created_at) " +
            "VALUES (#{userId}, #{content}, #{postId}, #{commentId}, " +
            "#{type}, #{isRead}, #{createdAt})")
    void insert(Notification notification);

    @Select("SELECT * FROM notification WHERE user_id = #{userId} " +
            "ORDER BY created_at DESC LIMIT 50")
    List<Notification> findByUserId(Long userId);

    @Update("UPDATE notification SET is_read = #{isRead} WHERE id = #{id}")
    void updateReadStatus(@Param("id") Long id, @Param("isRead") Boolean isRead);

    @Select("SELECT user_id FROM post WHERE id = #{postId}")
    Long getPostAuthorId(Long postId);

    @Select("SELECT user_id FROM comment WHERE id = #{commentId}")
    Long getCommentAuthorId(Long commentId);

    @Select("SELECT post_id FROM comment WHERE id = #{commentId}")
    Long getPostIdByCommentId(Long commentId);
}
