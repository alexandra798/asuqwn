// UserMapper.java
package com.forum.mapper;

import com.forum.model.entity.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Insert("INSERT INTO user (username, password_hash, email, is_activated, " +
            "is_muted, is_banned, is_premium, create_time, update_time) " +
            "VALUES (#{username}, #{passwordHash}, #{email}, #{isActivated}, " +
            "#{isMuted}, #{isBanned}, #{isPremium}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Update("UPDATE user SET password_hash = #{passwordHash}, " +
            "update_time = #{updateTime} WHERE id = #{id}")
    void updatePassword(@Param("id") Long id,
                        @Param("passwordHash") String passwordHash,
                        @Param("updateTime") java.time.LocalDateTime updateTime);

    @Update("UPDATE user SET is_activated = #{isActivated} WHERE id = #{id}")
    void updateActivationStatus(@Param("id") Long id, @Param("isActivated") Boolean isActivated);

    @Update("UPDATE user SET is_banned = #{isBanned}, update_time = #{updateTime} WHERE id = #{id}")
    void updateBanStatus(@Param("id") Long id, @Param("isBanned") boolean isBanned, @Param("updateTime") LocalDateTime updateTime);

    @Update("UPDATE user SET is_muted = #{isMuted}, update_time = #{updateTime} WHERE id = #{id}")
    void updateMuteStatus(@Param("id") Long id, @Param("isMuted") boolean isMuted, @Param("updateTime") LocalDateTime updateTime);

    @Insert("INSERT INTO sanction_record (user_id, type, start_time, end_time) " +
            "VALUES (#{userId}, #{type}, #{startTime}, #{endTime})")
    void insertSanctionRecord(@Param("userId") Long userId, @Param("type") String type, 
                              @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
