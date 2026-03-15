package com.tss.repositories;

import com.tss.model.Notification;

import java.util.List;

public interface NotificationRepo {
    void sendNotification(Notification notification);
    List<Notification> getAllUnseenNotifications(long userId);
    List<Notification> getAllNotifications(long userId);
}
