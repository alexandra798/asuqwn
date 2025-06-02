// CategoryMapper.java
package com.forum.mapper;

import com.forum.model.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("SELECT * FROM category ORDER BY id")
    List<Category> findAll();

    @Select("SELECT * FROM category WHERE id = #{id}")
    Category findById(Long id);

    @Insert("INSERT INTO category (name, guidelines, post_count, created_at, created_by) " +
            "VALUES (#{name}, #{guidelines}, 0, #{createdAt}, #{createdBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Category category);

    @Update("UPDATE category SET name = #{name}, guidelines = #{guidelines} WHERE id = #{id}")
    void update(Category category);

    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteById(Long id);
}