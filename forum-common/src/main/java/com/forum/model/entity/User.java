package com.forum.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String email;

    private Boolean isActivated;
    private Boolean isMuted;
    private Boolean isBanned;
    private Boolean isPremium;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private List<SanctionRecord> sanctionHistory;
    private class SanctionRecord {
        private String type;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }
}
