package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.NotificationDao;
import com.ssafy.yamyam.dto.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;

    @Autowired
    public NotificationServiceImpl(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public void sendLikeNotification(String receiverId, String senderId, String postTitle, int communityId) {
        if (receiverId.equals(senderId)) return;

        String content = senderId + " 님이 \"" + postTitle + "\" 글에 좋아요를 눌렀습니다.";

        Notification notification = new Notification();
        notification.setReceiverId(receiverId);
        notification.setSenderId(senderId);
        notification.setContent(content);
        notification.setCommunityId(communityId);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationDao.insertNotification(notification);
    }

    @Override
    public void sendCommentNotification(String receiverId, String senderId, String postTitle, int communityId) {
        if (receiverId.equals(senderId)) return;

        String content = senderId + " 님이 \"" + postTitle + "\" 글에 댓글을 달았습니다.";

        Notification notification = new Notification();
        notification.setReceiverId(receiverId);
        notification.setSenderId(senderId);
        notification.setContent(content);
        notification.setCommunityId(communityId);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationDao.insertNotification(notification);
    }

    @Override
    public List<Notification> getNotifications(String receiverId) {
        return notificationDao.selectNotificationsByReceiverId(receiverId);
    }

    @Override
    public int countUnread(String receiverId) {
        return notificationDao.countUnreadNotifications(receiverId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationDao.markAsRead(notificationId);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationDao.deleteNotification(notificationId);
    }

    @Override
    public void markAllAsRead(String receiverId) {
        notificationDao.markAllAsRead(receiverId);
    }
}
