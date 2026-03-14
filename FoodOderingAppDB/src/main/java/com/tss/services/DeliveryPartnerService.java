package com.tss.services;

import com.tss.exceptions.OrderNotFoundException;
import com.tss.exceptions.UserNotFoundException;
import com.tss.model.Order;
import com.tss.model.OrderStatus;
import com.tss.model.users.DeliveryPartner;
import com.tss.model.users.User;
import com.tss.repositories.OrderRepository;
import com.tss.repositories.UserRepository;
import com.tss.utils.Validate;

import java.util.List;

public class DeliveryPartnerService {
    private DeliveryPartner deliveryPartner;
    private DeliveryPartnerManager deliveryPartnerManager;
    private OrderManager orderManager;
    private OrderRepository orderRepository;
    private UserService userService;
    private NotificationService notificationService;
    private UserRepository userRepository;

    public DeliveryPartnerService(User deliveryPartner) {
        this.deliveryPartner = (DeliveryPartner) deliveryPartner;
        deliveryPartnerManager = DeliveryPartnerManager.getInstance();
        orderManager = OrderManager.getOrderManagerInstance();
        orderRepository = OrderRepository.getInstance();
        userService = UserService.getInstance();
        notificationService = NotificationService.getInstance();
        userRepository = UserRepository.getInstance();
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
        List<Order> orders = orderRepository.ordersFromDeliveryAgentId(deliveryPartner.getId());

        System.out.println("ORDER HISTORY:");
        orderRepository.displayOrders(orders);
    }


    public void confirmDelivery() {
        List<Order> pendingOrders = orderRepository
                .getOrderFromStatus(OrderStatus.OUT_FOR_DELIVERY,
                        orderRepository
                                .ordersFromDeliveryAgentId(deliveryPartner.getId()));

        if (pendingOrders == null || pendingOrders.isEmpty()) {
            System.out.println("Order not found!");
            return;
        }
        System.out.println("CURRENT ORDERS: ");
        orderRepository.displayOrders(pendingOrders);
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
        confirmOrder.ChangeState(OrderStatus.DELIVERED);

        notificationService.sendNotification(
                confirmOrder.getCustomerId(),
                "Your Order Is Delivered... Order id: " + confirmOrder.getOrderId(),
                "DELIVERY PARTNER"
        );
        System.out.println("Order Delivered...");

        deliveryPartnerManager.pushDeliveryPartnerInQueue(deliveryPartner);
    }

    public void displayNotifications() {
        userService.displayUserNotifications(deliveryPartner);
    }

    public void changePassword() {
        userService.changePassword(deliveryPartner);
    }

    public void changePhoneNumber() {
        userService.changeNumber(deliveryPartner);
    }

    public void reportIssue() {
        System.out.println("Write Your issue: ");
        String message = Validate.validateNonEmptyString();
        System.out.print("Do you really want to send?(Y/N): ");
        if (!Validate.validateYesNo()) {
            return;
        }
        notificationService.sendNotification(userRepository.getAdmin().getId(), message, "DELIVERY PARTNER: " + deliveryPartner.getName());
        System.out.println("Message sent...");
    }
}
