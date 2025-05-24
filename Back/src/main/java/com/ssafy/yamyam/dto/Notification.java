package com.ssafy.yamyam.dto;

import java.time.LocalDateTime;

public class Notification {
    private Long id;
    private String receiverId;
    private String senderId;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Integer communityId;

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public Notification() {}

    public Notification(Long id, String receiverId, String senderId, String content, boolean isRead, LocalDateTime createdAt, Integer communityId) {
        this.id = id;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.communityId = communityId;
    }

    public Long getId() {
        return id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
