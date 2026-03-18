package com.tss.services;

import com.tss.model.Notification;
import com.tss.model.users.UserType;
import com.tss.repositories.NotificationRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepo notificationRepo;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void sendNotificationTest() {
        long userId = 1L;
        String message = "Rahul, your order has been accepted.";
        doNothing().when(notificationRepo).sendNotification(any(Notification.class));

        notificationService.sendNotification(userId, message, UserType.ADMIN, UserType.CUSTOMER);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepo, times(1)).sendNotification(captor.capture());
        Notification captured = captor.getValue();
        assertEquals(userId, captured.getUserId());
        assertEquals(message, captured.getMessage());
        assertEquals(UserType.ADMIN, captured.getSender());
        assertEquals(UserType.CUSTOMER, captured.getReceiver());
    }

    @Test
    void sendNotificationBroadcastTest() {
        String message = "Server maintenance tonight.";
        doNothing().when(notificationRepo).sendNotification(any(Notification.class));

        notificationService.sendNotification(message, UserType.ADMIN, UserType.CUSTOMER);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepo, times(1)).sendNotification(captor.capture());
        Notification captured = captor.getValue();
        assertEquals(message, captured.getMessage());
        assertEquals(UserType.ADMIN, captured.getSender());
    }

    @Test
    void getNotificationsTest() {
        long userId = 1L;
        Notification n1 = new Notification(userId, "Tanmay placed an order!", UserType.ADMIN, UserType.CUSTOMER);
        Notification n2 = new Notification(userId, "Divy delivered your order!", UserType.DELIVERY_PARTNER, UserType.CUSTOMER);
        when(notificationRepo.getAllUnseenNotifications(userId)).thenReturn(Arrays.asList(n1, n2));

        List<Notification> result = notificationService.getNotifications(userId);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getNotificationsEmptyTest() {
        long userId = 99L;
        when(notificationRepo.getAllUnseenNotifications(userId)).thenReturn(Collections.emptyList());
        List<Notification> result = notificationService.getNotifications(userId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void broadcastCustomerNotificationTest() {
        String message = "Special offer today!";
        doNothing().when(notificationRepo).sendNotification(any(Notification.class));

        notificationService.broadcastCustomerNotification(message, UserType.ADMIN);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepo, times(1)).sendNotification(captor.capture());
        Notification captured = captor.getValue();
        assertEquals(message, captured.getMessage());
        assertEquals(UserType.CUSTOMER, captured.getReceiver());
    }

    @Test
    void broadcastDeliveryPartnerNotificationTest() {
        String message = "New order available!";
        doNothing().when(notificationRepo).sendNotification(any(Notification.class));

        notificationService.broadcastDeliveryPartnerNotification(message, UserType.ADMIN);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepo, times(1)).sendNotification(captor.capture());
        Notification captured = captor.getValue();
        assertEquals(message, captured.getMessage());
        assertEquals(UserType.DELIVERY_PARTNER, captured.getReceiver());
    }

    @Test
    void displayNotificationsTest() {
        Notification n = new Notification(1L, "Rahul, test message", UserType.ADMIN, UserType.CUSTOMER);
        List<Notification> notifications = Collections.singletonList(n);
        assertDoesNotThrow(() -> notificationService.displayNotifications(notifications));
    }

    @Test
    void displayNotificationsEmptyTest() {
        List<Notification> emptyList = Collections.emptyList();
        assertDoesNotThrow(() -> notificationService.displayNotifications(emptyList));
    }
}