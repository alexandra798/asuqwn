// CommentMapper.java
package com.forum.mapper;

import com.forum.model.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO comment (content, user_id, post_id, floor_number, " +
            "reply_to_floor, report_count, is_hidden, status, created_at) " +
            "VALUES (#{content}, #{userId}, #{postId}, #{floorNumber}, " +
            "#{replyToFloor}, #{reportCount}, #{isHidden}, #{status}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Comment comment);

    @Select("SELECT c.*, u.username as author_name " +
            "FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.post_id = #{postId} AND c.status = 1 " +
            "ORDER BY c.floor_number " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<Comment> selectByPostId(@Param("postId") Long postId,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM comment WHERE post_id = #{postId} AND status = 1")
    Long countByPostId(Long postId);

    @Select("SELECT * FROM comment WHERE id = #{id}")
    Comment selectById(Long id);

    @Select("SELECT * FROM comment WHERE post_id = #{postId} AND floor_number = #{floor}")
    Comment selectByPostIdAndFloor(@Param("postId") Long postId, @Param("floor") Integer floor);

    @Select("SELECT MAX(floor_number) FROM comment WHERE post_id = #{postId}")
    Integer getMaxFloorByPostId(Long postId);

    @Select("SELECT floor_number FROM comment " +
            "WHERE post_id = #{postId} AND reply_to_floor = #{floor}")
    List<Integer> getReplyFloors(@Param("postId") Long postId, @Param("floor") Integer floor);

    @Update("UPDATE comment SET report_count = #{reportCount}, " +
            "is_hidden = #{isHidden} WHERE id = #{id}")
    void update(Comment comment);
}
