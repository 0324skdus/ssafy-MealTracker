package com.ssafy.yamyam.dto;

import java.time.LocalDateTime;

public class Comment {

    private int id;
    private int communityId; // 외래키
    private String userId;
    private String content;
    private LocalDateTime createdAt;

    private boolean secret;

    public boolean isSecret() {
        return secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public Comment() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Comment(int id, int communityId, String userId, String content, LocalDateTime createdAt, boolean secret) {
        this.id = id;
        this.communityId = communityId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.secret = secret;
    }
}
