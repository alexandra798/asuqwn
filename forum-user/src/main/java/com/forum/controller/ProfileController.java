package com.forum.controller;


import com.forum.exception.BusinessException;
import com.forum.model.user.dto.MessageDTO;
import com.forum.model.user.dto.PasswordChangeDTO;
import com.forum.model.user.vo.AnswerListVO;
import com.forum.model.user.vo.PremiumFeeVO;
import com.forum.model.user.vo.ProfileVO;
import com.forum.service.ProfileService;
import com.forum.utils.PageResult;
import com.forum.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PutMapping("/password")
    public Result<?> changePassword(
            @RequestBody PasswordChangeDTO passwordDTO,
            @RequestParam Long userId) {
        try {
            profileService.changePassword(
                    passwordDTO.getOldPassword(),
                    passwordDTO.getNewPassword(),
                    userId);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public Result<PageResult<ProfileVO>> getProfile(@PathVariable Long userId) {
        try {
            return Result.success(profileService.getProfileByUser(userId));
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/premium-fee/{userId}")
    public Result<PremiumFeeVO> getPremiumFee(
            @PathVariable Long userId,
            @RequestParam LocalDateTime time) {
        try {
            return Result.success(profileService.getPremiumFeeByUser(time, userId));
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/payment-proof")
    public Result<?> uploadPaymentProof(@RequestBody MessageDTO messageDTO) {
        try {
            profileService.uploadPaymentProof(messageDTO);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/answers/{userId}")
    public Result<PageResult<AnswerListVO>> getAnswersList(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            return Result.success(profileService.getAnswersList(userId));
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}
