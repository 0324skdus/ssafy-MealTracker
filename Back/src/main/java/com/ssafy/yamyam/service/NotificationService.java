package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dto.Notification;

import java.util.List;

public interface NotificationService {

    void sendLikeNotification(String receiverId, String senderId, String postTitle, int communityId);

    void sendCommentNotification(String receiverId, String senderId, String postTitle, int communityId);

    List<Notification> getNotifications(String receiverId);

    int countUnread(String receiverId);

    void markAsRead(Long notificationId);

    void deleteNotification(Long notificationId);

    void markAllAsRead(String receiverId);

    void deleteAllByUserId(String userId);
}
