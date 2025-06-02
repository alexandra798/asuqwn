package com.forum.mapper;

import com.forum.model.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    
    @Insert("INSERT INTO question (title, description, correct_answer, score, " +
            "time_limit, category, difficulty, created_at, created_by, is_enabled) " +
            "VALUES (#{title}, #{description}, #{correctAnswer}, #{score}, " +
            "#{timeLimit}, #{category}, #{difficulty}, #{createdAt}, #{createdBy}, #{isEnabled})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Question question);

    @Select("SELECT * FROM question WHERE id = #{id}")
    Question selectById(Long id);

    @Select("SELECT * FROM question WHERE is_enabled = true " +
            "AND category = #{category} " +
            "ORDER BY RAND() LIMIT #{count}")
    List<Question> selectRandomByCategory(@Param("category") String category,
                                        @Param("count") Integer count);

    @Select("SELECT * FROM question WHERE is_enabled = true " +
            "AND category = #{category} " +
            "AND difficulty = #{difficulty} " +
            "ORDER BY RAND() LIMIT #{count}")
    List<Question> selectRandomByCategoryAndDifficulty(
            @Param("category") String category,
            @Param("difficulty") Integer difficulty,
            @Param("count") Integer count);

    @Select("SELECT * FROM question WHERE is_enabled = true " +
            "ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Question> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM question WHERE is_enabled = true")
    Long count();

    @Update("UPDATE question SET title = #{title}, " +
            "description = #{description}, " +
            "correct_answer = #{correctAnswer}, " +
            "score = #{score}, " +
            "time_limit = #{timeLimit}, " +
            "category = #{category}, " +
            "difficulty = #{difficulty}, " +
            "is_enabled = #{isEnabled} " +
            "WHERE id = #{id}")
    void update(Question question);

    @Update("UPDATE question SET is_enabled = #{isEnabled} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("isEnabled") Boolean isEnabled);

    @Select("SELECT DISTINCT category FROM question WHERE is_enabled = true")
    List<String> getAllCategories();

    @Select("SELECT * FROM question WHERE is_enabled = true ORDER BY RAND() LIMIT #{count}")
    List<Question> selectRandomEnabled(@Param("count") int count);
}
