package com.ssafy.yamyam.dto;

import java.time.LocalDateTime;

public class Community {

    private int id;
    private String userId;
    private String title;
    private String content;
    private int likeCount;
    private LocalDateTime createdAt;
    private String communityType;

    public String getCommunityType() {
        return communityType;
    }

    public void setCommunityType(String communityType) {
        this.communityType = communityType;
    }

    public Community() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Community(int id, String userId, String title, String content, int likeCount, LocalDateTime createdAt, String communityType) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.communityType = communityType;
    }
}
