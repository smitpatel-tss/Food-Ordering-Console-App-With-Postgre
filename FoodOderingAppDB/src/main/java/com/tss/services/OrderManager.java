package com.tss.services;

import com.tss.model.Order;
import com.tss.model.OrderStatus;

import java.util.LinkedList;
import java.util.Queue;

public class OrderManager {
    private Queue<Order> orders;
    private DeliveryPartnerManager deliveryPartnerManager;
    private NotificationService notificationService;

    private OrderManager() {
        orders = new LinkedList<>();
        deliveryPartnerManager = DeliveryPartnerManager.getInstance();
        notificationService = NotificationService.getInstance();
    }

    private static class InstanceContainer {
        static OrderManager obj = new OrderManager();
    }

    public static OrderManager getOrderManagerInstance() {
        return OrderManager.InstanceContainer.obj;
    }

    public void addOrderToQueue(Order order) {
        orders.offer(order);
        assignDeliveryPartner();
    }

    public void assignDeliveryPartner() {
        if (!orders.isEmpty()) {
            if (deliveryPartnerManager.isDeliveryPartnerAvailable()) {
                Order currentOrder = orders.poll();
                currentOrder.ChangeState(OrderStatus.OUT_FOR_DELIVERY);
                notificationService.sendNotification(
                        currentOrder.getCustomerId(),
                        "Your Order Is Out For Delivery... Order id: " + currentOrder.getOrderId(),
                        "RESTAURANT"
                );
                deliveryPartnerManager.assignOrder(currentOrder);
            }
        }
    }
}
