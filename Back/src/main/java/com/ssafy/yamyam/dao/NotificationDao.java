package com.ssafy.yamyam.dao;

import com.ssafy.yamyam.dto.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationDao {

    void insertNotification(Notification notification);

    List<Notification> selectNotificationsByReceiverId(String receiverId);

    int countUnreadNotifications(String receiverId);

    void markAsRead(Long notificationId);

    void deleteNotification(Long notificationId);

    void markAllAsRead(String receiverId);
}
