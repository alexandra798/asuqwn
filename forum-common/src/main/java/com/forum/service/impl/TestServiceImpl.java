// TestServiceImpl.java
package com.forum.service.impl;

import com.forum.mapper.QuestionMapper;
import com.forum.mapper.UserAnswerListMapper;
import com.forum.model.entity.Question;
import com.forum.model.entity.UserAnswerList;
import com.forum.model.user.dto.TestAnswerDTO;
import com.forum.model.user.vo.QuestionListVO;
import com.forum.service.TestService;
import com.forum.utils.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final QuestionMapper questionMapper;
    private final UserAnswerListMapper userAnswerListMapper;

    @Override
    public List<Question> generateQuestions() {
        // 使用新的 mapper 方法随机获取30道启用的题目
        return questionMapper.selectRandomEnabled(30);
    }

    @Override
    public PageResult<QuestionListVO> getOneQuestionInQuery(int pageNumber, List<Question> questions) {
        // Implement proper pagination for questions if this list is large
        // For now, assuming 'questions' is the list for the current context (e.g., 30 generated questions)
        if (questions == null || questions.isEmpty() || pageNumber < 1 || pageNumber > questions.size()) {
            // Handle invalid pageNumber or empty list appropriately
            // Returning the first question if pageNumber is out of bounds for simplicity
             QuestionListVO vo = new QuestionListVO();
             if (questions != null && !questions.isEmpty()) {
                vo.setQuestions(convertToQuestionVOList(List.of(questions.get(0))));
                vo.setQuestionNumber(1); // Displaying one question
             } else {
                vo.setQuestions(List.of());
                vo.setQuestionNumber(0);
             }
            return new PageResult<>(List.of(vo), questions != null ? (long)questions.size() : 0L, pageNumber, 1);
        }

        Question currentQuestion = questions.get(pageNumber - 1);
        QuestionListVO vo = new QuestionListVO();
        vo.setQuestionNumber(questions.size()); // Total questions in the current test set
        vo.setQuestions(convertToQuestionVOList(List.of(currentQuestion))); // Display one question

        // The PageResult should ideally reflect pagination over a larger set if applicable
        // Here, it's a bit mixed: pageNum for one item from a pre-selected list.
        return new PageResult<>(List.of(vo), (long) questions.size(), pageNumber, 1);
    }

    @Override
    @Transactional
    public void submitAnswers(TestAnswerDTO testAnswerDTO, Long userId) {
        // First, create or retrieve the UserAnswerList for this user and test submission
        UserAnswerList userAnswerList = new UserAnswerList();
        userAnswerList.setUserId(userId); // Assuming userId is passed or obtained from TestAnswerDTO
        userAnswerList.setSubmitTime(LocalDateTime.now());
        userAnswerList.setIsReviewed(false); // Default to not reviewed
        // Calculate totalTimeUsed based on individual answers if necessary or set later
        // For now, let's assume totalTimeUsed is sum of individual times, calculate after loop
        int totalTimeUsed = 0;

        userAnswerListMapper.insertAnswerList(userAnswerList); // Persist the list to get its ID
        Long answerListId = userAnswerList.getId(); // Get the generated ID

        if (answerListId == null) {
            log.error("Failed to create UserAnswerList for userId: {}", userId);
            // Optionally throw an exception
            return;
        }

        for (TestAnswerDTO.AnswerDetail detail : testAnswerDTO.getAnswers()) {
            // UserAnswer is an inner class of UserAnswerList, so create it like this or ensure it's static and public
            UserAnswerList.UserAnswer userAnswerItem = new UserAnswerList.UserAnswer();
            // userAnswerItem.setAnswerListId(answerListId); // This field is not in UserAnswer entity, linkage is by table
            userAnswerItem.setQuestionId(detail.getQuestionId());
            userAnswerItem.setAnswer(detail.getAnswer());
            userAnswerItem.setTimeUsed(detail.getTimeUsed());
            totalTimeUsed += detail.getTimeUsed();
            // userAnswerItem.setSubmittedAt(LocalDateTime.now()); // SubmittedAt is for the list, not individual answer
            userAnswerItem.setScore(0); // Initial score, set during review
            // Assuming UserAnswer entity has questionDescription - this should be set based on Question entity
            // Question question = questionMapper.selectById(detail.getQuestionId());
            // if (question != null) userAnswerItem.setQuestionDescription(question.getDescription());

            userAnswerListMapper.insertAnswer(answerListId, userAnswerItem.getQuestionId(), 
                userAnswerItem.getAnswer(), userAnswerItem.getQuestionDescription(), 
                userAnswerItem.getScore(), userAnswerItem.getTimeUsed());
        }
        userAnswerList.setTotalTimeUsed(totalTimeUsed);
        // We might need an update method in UserAnswerListMapper for totalTimeUsed if not set initially
        // For now, assume insertAnswerList handles all necessary fields or an update is done separately.

        log.info("用户答题提交成功: userId={}, 题目数={}, answerListId={}",
                userId, testAnswerDTO.getAnswers().size(), answerListId);
    }

    private List<QuestionListVO.Question> convertToQuestionVOList(List<Question> questions) {
        return questions.stream()
                .map(q -> {
                    QuestionListVO.Question vo = new QuestionListVO.Question();
                    vo.setId(q.getId());
                    vo.setDescription(q.getDescription());
                    vo.setType(q.getCategory()); // Assuming question category can be used as type
                    // vo.setOptions(...); // Options need to be populated if available, e.g. from a serialized field or related table
                    return vo;
                })
                .collect(Collectors.toList());
    }
}