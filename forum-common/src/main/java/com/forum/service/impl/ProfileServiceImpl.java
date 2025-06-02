// ProfileServiceImpl.java
package com.forum.service.impl;

import com.forum.exception.BusinessException;
import com.forum.mapper.PremiumFeeMapper;
import com.forum.mapper.UserAnswerListMapper;
import com.forum.mapper.UserMapper;
import com.forum.model.entity.PremiumFee;
import com.forum.model.entity.User;
import com.forum.model.entity.UserAnswerList;
import com.forum.model.user.dto.MessageDTO;
import com.forum.model.user.vo.AnswerListVO;
import com.forum.model.user.vo.PremiumFeeVO;
import com.forum.model.user.vo.ProfileVO;
import com.forum.service.ProfileService;
import com.forum.utils.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PremiumFeeMapper premiumFeeMapper;
    private final UserAnswerListMapper userAnswerListMapper;

    @Override
    @Transactional
    public boolean changePassword(String oldPassword, String newPassword, Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new BusinessException("原密码错误");
        }

        String newPasswordHash = passwordEncoder.encode(newPassword);
        userMapper.updatePassword(userId, newPasswordHash, LocalDateTime.now());

        log.info("用户修改密码成功: userId={}", userId);
        return true;
    }

    @Override
    public PageResult<ProfileVO> getProfileByUser(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        ProfileVO vo = new ProfileVO();
        // TODO: Populate ProfileVO from User entity. Example:
        // vo.setUsername(user.getUsername());
        // vo.setEmail(user.getEmail());
        // ... etc.
        // Assuming ProfileVO has necessary setters (Lombok @Data)

        return new PageResult<>(List.of(vo), 1L, 1, 1);
    }

    @Override
    public PremiumFeeVO getPremiumFeeByUser(LocalDateTime time, Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        PremiumFee currentFee = premiumFeeMapper.getCurrentFee(time);

        PremiumFeeVO vo = new PremiumFeeVO();
        if (currentFee != null) {
            vo.setAmount(currentFee.getAmount());
        }
        vo.setPremium(user.getIsPremium());

        return vo;
    }

    @Override
    public void uploadPaymentProof(MessageDTO messageDTO) {
        // 保存支付凭证信息
        // 这里简化处理，实际应该保存到数据库
        log.info("用户上传支付凭证: {}", messageDTO);
    }

    @Override
    public PageResult<AnswerListVO> getAnswersList(Long userId) {
        UserAnswerList userAnswerList = userAnswerListMapper.selectByUserId(userId);
        if (userAnswerList == null || userAnswerList.getAnswers() == null) {
            return new PageResult<>(List.of(), 0L, 1, 10);
        }

        List<AnswerListVO.AnswerItem> answerItems = userAnswerList.getAnswers().stream().map(userAnswer -> {
            AnswerListVO.AnswerItem item = new AnswerListVO.AnswerItem();
            item.setQuestionId(String.valueOf(userAnswer.getQuestionId()));
            item.setDescription(userAnswer.getQuestionDescription());
            item.setAnswer(userAnswer.getAnswer());
            item.setScore(userAnswer.getScore());
            return item;
        }).collect(Collectors.toList());

        AnswerListVO resultVO = new AnswerListVO();
        resultVO.setSubmitTime(userAnswerList.getSubmitTime());
        resultVO.setAnswers(answerItems);

        List<AnswerListVO> voList = List.of(resultVO);
        return new PageResult<>(voList, (long) voList.size(), 1, voList.size());
    }
}