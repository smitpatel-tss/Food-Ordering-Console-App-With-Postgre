package com.tss.model;

public class Notification {
    private String message;
    private long userId;
    private String from;
    private long notificationId;
    private static long notificationCount = 0;

    public Notification(String message, long userId, String from) {
        this.message = message;
        this.userId = userId;
        this.from = from;
        this.notificationId = notificationCount++;
    }

    public String getMessage() {
        return message;
    }

    public long getUserId() {
        return userId;
    }

    public String getFrom() {
        return from;
    }

    public long getNotificationId() {
        return notificationId;
    }

    @Override
    public String toString() {
        return "[From " + from + "] \uD83D\uDD14 " + message;
    }
}
