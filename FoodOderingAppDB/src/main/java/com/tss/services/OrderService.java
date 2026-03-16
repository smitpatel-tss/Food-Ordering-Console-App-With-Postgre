package com.tss.services;

import com.tss.exceptions.CartEmptyException;
import com.tss.exceptions.NoDeliveryPartnerAvailable;
import com.tss.model.*;
import com.tss.model.users.UserType;
import com.tss.payments.PaymentMode;
import com.tss.repositories.*;

import java.util.List;

public class OrderService {
    private DiscountService discountService;
    private NotificationService notificationService;
    private OrderRepo orderRepo;
    private DeliveryPartnerRepo deliveryPartnerRepo;
    private CartRepo cartRepo;
    private DiscountRepo discountRepo;


    public OrderService() {
        discountService = DiscountService.getInstance();
        notificationService = NotificationService.getInstance();
        orderRepo=new OrderRepoImpl();
        deliveryPartnerRepo=new DeliveryPartnerRepoImpl();
        cartRepo=new CartRepoImpl();
        discountRepo=new DiscountRepoImpl();
    }

    private static class InstanceContainer {
        static OrderService obj = new OrderService();
    }

    public static OrderService getInstance() {
        return InstanceContainer.obj;
    }

    public Order placeOrder(PaymentMode paymentMode, long customerId) {
        if(cartRepo.isCartEmpty(customerId)){
            throw new CartEmptyException();
        }
        if(deliveryPartnerRepo.isDeliveryPartnersEmpty()){
            throw new NoDeliveryPartnerAvailable();
        }

        Cart cart=cartRepo.getCart(customerId);
        Order newOrder = new Order(cart, discountRepo.giveMaxPossibleDiscount(paymentMode.getAmount()), paymentMode, customerId);
        orderRepo.placeNewOrder(newOrder);


        notificationService.sendNotification(
                newOrder.getCustomerId(),
                "Your Order Is Accepted... Order id: " + newOrder.getOrderId(),
                UserType.ADMIN,
                UserType.CUSTOMER
        );

        deliveryPartnerRepo.assignOrder();
        cartRepo.clearCart(customerId);

        return newOrder;
    }

    public void displayOrders(List<Order> ordersList) {
        if (ordersList.isEmpty()) {
            System.out.println("No orders Found!");
            return;
        }
        for (Order order : ordersList) {

            System.out.println(order);
        }
    }

}
