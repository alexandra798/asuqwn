package com.forum.controller;

import com.forum.model.admin.dto.BanUserDTO;
import com.forum.model.admin.dto.MuteUserDTO;
import com.forum.service.ManageUsersService;
import com.forum.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-users")
@RequiredArgsConstructor
public class ManageUsersController {

    private final ManageUsersService manageUsersService;


    @PostMapping("/users/{userId}/ban")
    public Result<?> banUser(
            @PathVariable Long userId,
            @RequestBody BanUserDTO banDTO,
            @RequestHeader("Admin-Token") String adminToken) {
        manageUsersService.banUser(userId, banDTO);
        return Result.success();
    }

    @PostMapping("/users/{userId}/mute")
    public Result<?> muteUser(
            @PathVariable Long userId,
            @RequestBody MuteUserDTO muteDTO,
            @RequestHeader("Admin-Token") String adminToken) {
        manageUsersService.muteUser(userId, muteDTO);
        return Result.success();
    }
}