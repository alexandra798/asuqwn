package com.forum.service;


import com.forum.model.admin.dto.BanUserDTO;
import com.forum.model.admin.dto.MuteUserDTO;

public interface ManageUsersService {

    void banUser(Long userId, BanUserDTO banUserDTO);

    void muteUser(Long userId, MuteUserDTO muteUserDTO);
}
