package com.tss.repositories;

import com.tss.model.Order;
import com.tss.model.OrderStatus;
import com.tss.model.users.Customer;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private List<Order> allOrders;

    private OrderRepository() {
        allOrders = new ArrayList<>();
    }

    public List<Order> getAllOrders() {
        return allOrders;
    }

    private static class RepoContainer {
        static OrderRepository obj = new OrderRepository();
    }

    public static OrderRepository getInstance() {
        return OrderRepository.RepoContainer.obj;
    }

    public List<Order> ordersFromDeliveryAgentId(long id) {
        List<Order> matchingOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.getDeliveryPartner() != null && order.getDeliveryPartner().getId() == id) {
                matchingOrders.add(order);
            }
        }
        return matchingOrders;
    }

    public Order ordersFromOrderId(long id) {
        List<Order> matchingOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.getOrderId() == id) {
                return order;
            }
        }
        return null;
    }

    public List<Order> ordersFromCustomerId(long id) {
        List<Order> matchingOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.getCustomerId() == id) {
                matchingOrders.add(order);
            }
        }
        return matchingOrders;
    }

    public void displayOrders(List<Order> ordersList) { //ORDER SERVICE
        if (ordersList.isEmpty()) {
            System.out.println("No orders Found!");
            return;
        }
        for (Order order : ordersList) {

            System.out.println(order);
        }
    }

    public List<Order> getOrderFromStatus(OrderStatus orderStatus, List<Order> ordersList) {
        return ordersList.stream()
                .filter(order -> order.getStatus() == orderStatus)
                .toList();
    }

    public void displayAllOrders() {
        displayOrders(allOrders);
    }

    public List<Order> getPendingOrders() {
        List<Order> matchingOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.getStatus() != OrderStatus.DELIVERED) {
                matchingOrders.add(order);
            }
        }
        return matchingOrders;
    }

}
