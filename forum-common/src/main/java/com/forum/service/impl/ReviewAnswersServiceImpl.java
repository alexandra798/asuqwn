// ReviewAnswersServiceImpl.java
package com.forum.service.impl;

import com.forum.exception.BusinessException;
import com.forum.mapper.QuestionMapper;
import com.forum.mapper.UserAnswerListMapper;
import com.forum.mapper.UserMapper;
import com.forum.model.admin.dto.QuestionListDTO;
import com.forum.model.admin.dto.ReviewDTO;
import com.forum.model.admin.vo.AnswerSummaryVO;
import com.forum.model.entity.Question;
import com.forum.model.entity.UserAnswerList;
import com.forum.service.EntryTestAnnouncementService;
import com.forum.service.ReviewAnswersService;
import com.forum.utils.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewAnswersServiceImpl implements ReviewAnswersService {

    private final QuestionMapper questionMapper;
    private final UserAnswerListMapper userAnswerListMapper;
    private final UserMapper userMapper;
    private final EntryTestAnnouncementService announcementService;

    @Override
    @Transactional
    public void addQuestion(QuestionListDTO questionListDTO) {
        if (questionListDTO == null || questionListDTO.getQuestions() == null) {
            log.warn("尝试添加题目，但列表为空或为null");
            return; // Or throw exception
        }
        int successfullyAddedCount = 0;
        for (QuestionListDTO.QuestionItem item : questionListDTO.getQuestions()) {
            Question question = new Question();
            question.setDescription(item.getDescription());
            question.setCategory(item.getType()); // Assuming DTO type maps to Question category
            try {
                questionMapper.insert(question); // 确保这个方法如果失败会处理或你能捕获
                successfullyAddedCount++;
            } catch (Exception e) {
                log.error("添加题目失败: " + item.getDescription(), e);
                // 根据你的错误处理策略，你可能想继续添加其他题目，或者在这里中断并抛出异常
            }
        }
        log.info("题目添加成功: count={}", successfullyAddedCount);
    }

    @Override
    public PageResult<AnswerSummaryVO> getAnswersList(String status, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        boolean isReviewedStatus = "reviewed".equalsIgnoreCase(status); // Example mapping status string to boolean
        
        List<AnswerSummaryVO> summaries = userAnswerListMapper.findAnswerSummariesByStatus(isReviewedStatus, offset, pageSize);
        Long total = userAnswerListMapper.countAnswerSummariesByStatus(isReviewedStatus);

        return new PageResult<>(summaries, total, pageNum, pageSize);
    }

    @Override
    public PageResult<UserAnswerList> getAnswersByUser(Long userId) {
        UserAnswerList userAnswerList = userAnswerListMapper.selectByUserId(userId);
        if (userAnswerList == null) {
            return new PageResult<>(List.of(), 0L, 1, 10);
        }
        return new PageResult<>(List.of(userAnswerList), 1L, 1, 1);
    }

    @Override
    @Transactional
    public void submitReviewing(ReviewDTO reviewDTO) {
        if (reviewDTO == null || reviewDTO.getAnswers() == null) {
            throw new BusinessException("审核数据为空");
        }

        int totalScore = 0;
        for (ReviewDTO.Item item : reviewDTO.getAnswers()) {
            totalScore += item.getScore();
            userAnswerListMapper.updateScoreByUserAnswerId(item.getAnswerId(), item.getScore());
        }

        // 判断是否通过（38分及以上）
        boolean isPassed = totalScore >= 38;

        // 更新用户激活状态
        if (isPassed) {
            userMapper.updateActivationStatus(reviewDTO.getUserId(), true);
            log.info("用户通过测试: userId={}, totalScore={}", reviewDTO.getUserId(), totalScore);
        } else {
            // 不通过的用户标记为已审核但未激活
            log.info("用户未通过测试: userId={}, totalScore={}", reviewDTO.getUserId(), totalScore);
        }
        userAnswerListMapper.updateReviewStatus(reviewDTO.getUserId(), true);

        // 发送通知
        announcementService.sendAnnouncement(reviewDTO.getUserId(), isPassed);
    }
}
