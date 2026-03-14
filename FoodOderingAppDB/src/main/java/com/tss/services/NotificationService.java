package com.tss.services;

import com.tss.exceptions.UserNotFoundException;
import com.tss.model.Notification;
import com.tss.model.users.User;
import com.tss.repositories.NotificationRepository;
import com.tss.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private NotificationRepository notificationRepository;
    private UserRepository userRepository;


    private NotificationService() {
        notificationRepository = NotificationRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    private static class InstanceContainer {
        static NotificationService obj = new NotificationService();
    }

    public static NotificationService getInstance() {
        return InstanceContainer.obj;
    }

    public void sendNotification(long userId, String message, String from) {
        Notification notification = new Notification(message, userId, from);
        notificationRepository.addNotification(userId, notification);
    }

    public List<Notification> getNotifications(long userId) {
        return notificationRepository.getUserNotifications(userId);
    }

    public void clearNotifications(long userId) {
        notificationRepository.clearUserNotifications(userId);
    }

    public void broadcastCustomerNotification(String message, String from) {
        List<Long> customerIds = new ArrayList<>();
        for (User user : userRepository.getCustomers()) {
            customerIds.add(user.getId());
        }
        if (customerIds.isEmpty()) {
            throw new UserNotFoundException("No Customers Found!");
        }

        for (Long userId : customerIds) {
            sendNotification(userId, message, from);
        }
    }

    public void broadcastDeliveryPartnerNotification(String message, String from) {
        List<Long> customerIds = new ArrayList<>();
        for (User user : userRepository.getDeliveryPartners()) {
            customerIds.add(user.getId());
        }
        if (customerIds.isEmpty()) {
            throw new UserNotFoundException("No Delivery Partners Found!");
        }

        for (Long userId : customerIds) {
            sendNotification(userId, message, from);
        }
    }

    public void displayNotifications(List<Notification> notificationList) {
        for (Notification notification : notificationList) {
            System.out.println(notification);
        }
    }

}