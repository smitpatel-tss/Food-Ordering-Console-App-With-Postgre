package com.tss.services;

import com.tss.model.Order;
import com.tss.model.users.DeliveryPartner;
import com.tss.repositories.UserRepository;

import java.util.LinkedList;
import java.util.Queue;

public class DeliveryPartnerManager {
    private Queue<DeliveryPartner> deliveryPartners;
    NotificationService notificationService;

    private DeliveryPartnerManager() {
        LinkedList<DeliveryPartner> tempLinkedlist = new LinkedList<>(UserRepository.
                getInstance().getDeliveryPartners());
        deliveryPartners = new LinkedList<>(tempLinkedlist);
        notificationService = NotificationService.getInstance();
    }

    private static class InstanceContainer {
        static DeliveryPartnerManager obj = new DeliveryPartnerManager();
    }

    public static DeliveryPartnerManager getInstance() {
        return InstanceContainer.obj;
    }

    public void pushDeliveryPartnerInQueue(DeliveryPartner deliveryPartner) {
        deliveryPartners.offer(deliveryPartner);
        OrderManager.getOrderManagerInstance().assignDeliveryPartner();
    }

    public void assignOrder(Order order) {
        if (deliveryPartners.isEmpty()) {
            return;
        }
        notificationService.sendNotification(
                deliveryPartners.peek().getId(),
                "New order is Assigned to you... Order id: " + order.getOrderId(),
                "ADMIN"
        );

        order.setDeliveryPartner(deliveryPartners.poll());
    }

    public boolean isDeliveryPartnerAvailable() {
        return !(deliveryPartners.isEmpty());
    }

}
