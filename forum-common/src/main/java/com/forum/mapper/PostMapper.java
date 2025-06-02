// PostMapper.java
package com.forum.mapper;

import com.forum.model.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {

    @Insert("INSERT INTO post (title, content, user_id, category_id, floor_count, " +
            "report_count, view_count, is_hidden, status, created_at, updated_at) " +
            "VALUES (#{title}, #{content}, #{userId}, #{categoryId}, #{floorCount}, " +
            "#{reportCount}, #{viewCount}, #{isHidden}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Post post);

    @Select("SELECT p.*, u.username as author_name, c.name as category_name " +
            "FROM post p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "LEFT JOIN category c ON p.category_id = c.id " +
            "WHERE p.id = #{id}")
    Post selectById(Long id);

    @Select("SELECT p.*, u.username as author_name, c.name as category_name " +
            "FROM post p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "LEFT JOIN category c ON p.category_id = c.id " +
            "WHERE p.category_id = #{categoryId} AND p.status = 1 " +
            "ORDER BY p.created_at DESC " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<Post> selectByCategory(@Param("categoryId") Long categoryId,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM post WHERE category_id = #{categoryId} AND status = 1")
    Long countByCategory(Long categoryId);

    @Update("UPDATE post SET title = #{title}, content = #{content}, " +
            "updated_at = #{updatedAt}, report_count = #{reportCount}, " +
            "is_hidden = #{isHidden} WHERE id = #{id}")
    void update(Post post);

    @Update("UPDATE post SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(Long id);

    @Update("UPDATE post SET floor_count = floor_count + 1 WHERE id = #{id}")
    void incrementFloorCount(Long id);
}