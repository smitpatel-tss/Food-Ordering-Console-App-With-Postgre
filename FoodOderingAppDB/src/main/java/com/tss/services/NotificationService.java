package com.tss.services;

import com.tss.model.Notification;
import com.tss.model.users.UserType;
import com.tss.repositories.NotificationRepo;
import com.tss.repositories.NotificationRepoImpl;

import java.util.List;

public class NotificationService {

    private NotificationRepo notificationRepo;


    private NotificationService() {
        notificationRepo=new NotificationRepoImpl();
    }

    private static class InstanceContainer {
        static NotificationService obj = new NotificationService();
    }

    public static NotificationService getInstance() {
        return InstanceContainer.obj;
    }

    public void sendNotification(long userId, String message, UserType sender,UserType receiver) {
        Notification notification = new Notification(userId,message,sender, receiver);

        notificationRepo.sendNotification(notification);
    }

    public void sendNotification(String message, UserType sender,UserType receiver) {
        Notification notification = new Notification(message,sender, receiver);

        notificationRepo.sendNotification(notification);
    }

    public List<Notification> getNotifications(long userId) {
        return notificationRepo.getAllUnseenNotifications(userId);
    }

    public void broadcastCustomerNotification(String message, UserType from) {
        notificationRepo.sendNotification(new Notification(message,from,UserType.CUSTOMER));

    }

    public void broadcastDeliveryPartnerNotification(String message, UserType from) {
        notificationRepo.sendNotification(new Notification(message,from,UserType.DELIVERY_PARTNER));
    }

    public void displayNotifications(List<Notification> notificationList) {
        for (Notification notification : notificationList) {
            System.out.println(notification);
        }
    }

}