package com.tss.services;

import com.tss.exceptions.OrderNotFoundException;
import com.tss.model.Order;
import com.tss.model.users.DeliveryPartner;
import com.tss.model.users.User;
import com.tss.model.users.UserType;
import com.tss.repositories.*;
import com.tss.utils.Validate;

import java.util.List;

public class DeliveryPartnerService {
    private DeliveryPartner deliveryPartner;
    private UserService userService;
    private NotificationService notificationService;
    private DeliveryPartnerRepo deliveryPartnerRepo;
    private OrderService orderService;
    private OrderRepo orderRepo;

    public DeliveryPartnerService(User deliveryPartner) {
        this.deliveryPartner = (DeliveryPartner) deliveryPartner;
        userService = UserService.getInstance();
        notificationService = NotificationService.getInstance();
        deliveryPartnerRepo=new DeliveryPartnerRepoImpl();
        orderService=OrderService.getInstance();
        orderRepo=new OrderRepoImpl();
    }

    public DeliveryPartner getDeliveryPartner() {
        return deliveryPartner;
    }

    public void setDeliveryPartner(DeliveryPartner deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
    }

    public void welcomeDisplay() {
        System.out.println("\nHii, " + deliveryPartner.getName() + "\nWelcome!");
    }

    public void printOrdersHistory() {
        List<Order> orders = orderRepo.ordersFromDeliveryAgentId(deliveryPartner.getId());

        System.out.println("ORDER HISTORY:");
        orderService.displayOrders(orders);

    }


    public void confirmDelivery() {
        List<Order> pendingOrders = deliveryPartnerRepo.pendingOrders(deliveryPartner.getId());

        if (pendingOrders == null || pendingOrders.isEmpty()) {
            System.out.println("Order not found!");
            return;
        }
        System.out.println("CURRENT ORDERS: ");
        orderService.displayOrders(pendingOrders);
        System.out.print("\nType Order Id to Confirm Delivery: ");
        long orderId = Validate.validatePositiveLong();

        Order confirmOrder = null;
        for (Order order : pendingOrders) {
            if (order.getOrderId() == orderId) {
                confirmOrder = order;
            }
        }

        if (confirmOrder == null) {
            throw new OrderNotFoundException("No Order Found with id " + orderId + "!");
        }
        try{
            deliveryPartnerRepo.completeOrder(deliveryPartner.getId(),orderId);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }
        deliveryPartnerRepo.assignOrder();

        notificationService.sendNotification(
                confirmOrder.getCustomerId(),
                "Your Order Is Delivered... Order id: " + orderId,
                UserType.DELIVERY_PARTNER,
                UserType.CUSTOMER
        );
        System.out.println("Order Delivered...");
    }

    public void displayNotifications() {
        userService.displayUserNotifications(deliveryPartner);
    }

    public void changePassword() {
        userService.changePassword(deliveryPartner, UserType.DELIVERY_PARTNER);
    }

    public void changePhoneNumber() {
        userService.changeNumber(deliveryPartner, UserType.DELIVERY_PARTNER);
    }

    public void reportIssue() {
        System.out.println("Write Your issue: ");
        String message = Validate.validateNonEmptyString();
        System.out.print("Do you really want to send?(Y/N): ");
        if (!Validate.validateYesNo()) {
            return;
        }
        notificationService.sendNotification(message,  UserType.DELIVERY_PARTNER, UserType.ADMIN);
        System.out.println("Message sent...");
    }
}
