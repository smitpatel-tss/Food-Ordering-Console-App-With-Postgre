package com.tss.model;

import com.tss.model.users.UserType;

public class Notification {
    private String message;
    private Long userId;
    private UserType sender;
    private long notificationId;
    private UserType receiver;
    private static long notificationCount = 0;

    public Notification(String message, long userId, UserType sender) {
        this.message = message;
        this.userId = userId;
        this.sender = sender;
        this.notificationId = notificationCount++;
    }

    public Notification(Long userId, String message, UserType sender, UserType receiver) {
        this.message = message;
        this.userId = userId;
        this.sender = sender;
        this.receiver=receiver;
    }

    public Notification(String message, UserType sender, UserType receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver=receiver;
    }

    public String getMessage() {
        return message;
    }

    public Long getUserId() {
        return userId;
    }

    public UserType getSender() {
        return sender;
    }

    public long getNotificationId() {
        return notificationId;
    }

    @Override
    public String toString() {
        return "[From " + sender + "] \uD83D\uDD14 " + message;
    }

    public UserType getReceiver() {
        return receiver;
    }

    public void setReceiver(UserType receiver) {
        this.receiver = receiver;
    }
}
