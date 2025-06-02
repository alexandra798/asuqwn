package com.forum.mapper;

import com.forum.model.entity.PremiumFee;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PremiumFeeMapper {
    
    @Insert("INSERT INTO premium_fee (start_time, end_time, amount) " +
            "VALUES (#{startTime}, #{endTime}, #{amount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(PremiumFee premiumFee);

    @Select("SELECT * FROM premium_fee WHERE id = #{id}")
    PremiumFee selectById(Long id);

    @Select("SELECT * FROM premium_fee WHERE " +
            "#{currentTime} BETWEEN start_time AND end_time")
    PremiumFee getCurrentFee(@Param("currentTime") java.time.LocalDateTime currentTime);

    @Select("SELECT * FROM premium_fee ORDER BY start_time DESC")
    List<PremiumFee> selectAll();

    @Update("UPDATE premium_fee SET start_time = #{startTime}, " +
            "end_time = #{endTime}, amount = #{amount} WHERE id = #{id}")
    void update(PremiumFee premiumFee);

    @Delete("DELETE FROM premium_fee WHERE id = #{id}")
    void deleteById(Long id);
}
