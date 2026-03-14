package com.tss.repositories;

import com.tss.model.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationRepository {

    private Map<Long, List<Notification>> notifications;

    private NotificationRepository() {
        notifications = new HashMap<>();
    }

    private static class InstanceContainer {
        static NotificationRepository obj = new NotificationRepository();
    }

    public static NotificationRepository getInstance() {
        return InstanceContainer.obj;
    }

    public void addNotification(long userId, Notification notification) {
        List<Notification> list = notifications.computeIfAbsent(userId, k -> new ArrayList<>());
        list.add(notification);
    }

    public List<Notification> getUserNotifications(long userId) {
        return notifications.getOrDefault(userId, new ArrayList<>());
    }

    public void clearUserNotifications(long userId) {
        notifications.remove(userId);
    }

    public Map<Long, List<Notification>> getAllNotifications() {
        return notifications;
    }
}