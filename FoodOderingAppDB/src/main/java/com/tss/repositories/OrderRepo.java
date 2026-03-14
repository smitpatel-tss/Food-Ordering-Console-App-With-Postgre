package com.tss.repositories;

import com.tss.model.Cart;
import com.tss.model.Order;
import com.tss.model.OrderStatus;

import java.util.List;

public interface OrderRepo {
    Cart cartOfOrder(long orderId);
    void placeNewOrder(Order order);
    List<Order> getAllOrders();
    List<Order> ordersFromDeliveryAgentId(long deliveryPartnerId);
    List<Order> ordersFromCustomerId(long customerId);
    List<Order> getOrderFromStatus(OrderStatus orderStatus);
}
