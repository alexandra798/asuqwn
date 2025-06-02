package com.forum.mapper;

import com.forum.model.entity.UserAnswerList;
import com.forum.model.admin.vo.AnswerSummaryVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserAnswerListMapper {
    
    @Insert("INSERT INTO user_answer_list (user_id, total_time_used, submit_time, " +
            "register_time, is_reviewed) " +
            "VALUES (#{userId}, #{totalTimeUsed}, #{submitTime}, " +
            "#{registerTime}, #{isReviewed})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertAnswerList(UserAnswerList answerList);

    @Insert("INSERT INTO user_answer (answer_list_id, question_id, answer, " +
            "question_description, score, time_used) " +
            "VALUES (#{answerListId}, #{questionId}, #{answer}, " +
            "#{questionDescription}, #{score}, #{timeUsed})")
    void insertAnswer(@Param("answerListId") Long answerListId,
                     @Param("questionId") Long questionId,
                     @Param("answer") String answer,
                     @Param("questionDescription") String questionDescription,
                     @Param("score") Integer score,
                     @Param("timeUsed") Integer timeUsed);

    @Select("SELECT * FROM user_answer_list WHERE user_id = #{userId}")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "totalTimeUsed", column = "total_time_used"),
        @Result(property = "submitTime", column = "submit_time"),
        @Result(property = "registerTime", column = "register_time"),
        @Result(property = "isReviewed", column = "is_reviewed"),
        @Result(property = "answers", column = "id",
                many = @Many(select = "selectAnswersByListId"))
    })
    UserAnswerList selectByUserId(Long userId);

    @Select("SELECT * FROM user_answer WHERE answer_list_id = #{answerListId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "questionId", column = "question_id"),
        @Result(property = "answer", column = "answer"),
        @Result(property = "questionDescription", column = "question_description"),
        @Result(property = "score", column = "score"),
        @Result(property = "timeUsed", column = "time_used")
    })
    List<UserAnswerList.UserAnswer> selectAnswersByListId(Long answerListId);

    @Update("UPDATE user_answer_list SET is_reviewed = #{isReviewed} " +
            "WHERE user_id = #{userId}")
    void updateReviewStatus(@Param("userId") Long userId,
                           @Param("isReviewed") Boolean isReviewed);

    @Update("UPDATE user_answer SET score = #{score} " +
            "WHERE answer_list_id = #{answerListId} AND question_id = #{questionId}")
    void updateAnswerScore(@Param("answerListId") Long answerListId,
                          @Param("questionId") Long questionId,
                          @Param("score") Integer score);

    @Select("SELECT COUNT(*) FROM user_answer_list WHERE is_reviewed = false")
    Long countUnreviewed();

    @Update("UPDATE user_answer SET score = #{score} WHERE id = #{userAnswerId}")
    void updateScoreByUserAnswerId(@Param("userAnswerId") Long userAnswerId, @Param("score") Integer score);

    @Select("SELECT ual.id as answer_list_id, ual.user_id, u.email, ual.submit_time, ual.is_reviewed " +
            "FROM user_answer_list ual JOIN user u ON ual.user_id = u.id " +
            "WHERE ual.is_reviewed = #{isReviewedStatus} " +
            "ORDER BY ual.submit_time ASC LIMIT #{limit} OFFSET #{offset}")
    List<AnswerSummaryVO> findAnswerSummariesByStatus(@Param("isReviewedStatus") boolean isReviewedStatus, 
                                                 @Param("offset") int offset, 
                                                 @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM user_answer_list WHERE is_reviewed = #{isReviewedStatus}")
    Long countAnswerSummariesByStatus(@Param("isReviewedStatus") boolean isReviewedStatus);
}
