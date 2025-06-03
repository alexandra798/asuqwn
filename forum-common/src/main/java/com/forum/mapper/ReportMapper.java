package com.forum.mapper;

import com.forum.model.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Mapper
public interface ReportMapper {
    
    @Insert("INSERT INTO report (type, target_id, content, reported_by, title, author_id, count, status, reported_at) " +
            "VALUES (#{type}, #{targetId}, #{content}, #{reportedBy}, #{title}, #{authorId}, #{count}, #{status}, #{reportedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Report report);

    @Select("SELECT * FROM report WHERE id = #{id}")
    Report selectById(Long id);

    @Select("SELECT * FROM report WHERE status = #{status} ORDER BY reported_at DESC")
    List<Report> selectByStatus(String status);

    @Select("SELECT * FROM report ORDER BY reported_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Report> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM report")
    Long count();

    @Update("UPDATE report SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    @Update("UPDATE report SET type = #{type}, target_id = #{targetId}, content = #{content}, " +
            "reported_by = #{reportedBy}, title = #{title}, author_id = #{authorId}, " +
            "count = #{count}, status = #{status}, reported_at = #{reportedAt} WHERE id = #{id}")
    void update(Report report);

    @Update("UPDATE report SET count = count + 1 WHERE type = #{type} AND target_id = #{targetId}")
    void incrementCount(@Param("type") String type, @Param("targetId") Long targetId);

    @Select("SELECT * FROM report WHERE type = #{type} AND target_id = #{targetId}")
    Report findByTypeAndTargetId(@Param("type") String type, @Param("targetId") Long targetId);

    @Select("SELECT * FROM report WHERE type = #{type} AND reported_at BETWEEN #{startDate} AND #{endDate} ORDER BY reported_at DESC")
    List<Report> findByTypeAndDateRange(@Param("type") String type, 
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
}
