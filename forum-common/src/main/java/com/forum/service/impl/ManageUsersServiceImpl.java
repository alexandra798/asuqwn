// ManageUsersServiceImpl.java
package com.forum.service.impl;

import com.forum.exception.BusinessException;
import com.forum.mapper.UserMapper;
import com.forum.model.admin.dto.BanUserDTO;
import com.forum.model.admin.dto.MuteUserDTO;
import com.forum.model.entity.User;
import com.forum.service.ManageUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManageUsersServiceImpl implements ManageUsersService {

    private final UserMapper userMapper;

    @Override
    @Transactional
    public void banUser(Long userId, BanUserDTO banUserDTO) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        user.setIsBanned(true);
        userMapper.updateBanStatus(userId, true, LocalDateTime.now());

        // 记录封禁时长
        userMapper.insertSanctionRecord(userId, "BAN",
                LocalDateTime.now(), LocalDateTime.now().plusDays(banUserDTO.getDays()));

        log.info("用户封禁成功: userId={}, days={}", userId, banUserDTO.getDays());
    }

    @Override
    @Transactional
    public void muteUser(Long userId, MuteUserDTO muteUserDTO) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        user.setIsMuted(true);
        userMapper.updateMuteStatus(userId, true, LocalDateTime.now());

        // 记录禁言时长
        userMapper.insertSanctionRecord(userId, "MUTE",
                LocalDateTime.now(), LocalDateTime.now().plusDays(muteUserDTO.getDays()));

        log.info("用户禁言成功: userId={}, days={}", userId, muteUserDTO.getDays());
    }
}