package com.tss.services;

import com.tss.model.FoodItem;
import com.tss.model.Order;

public class InvoiceService {

    public void printInvoice(Order order) {
        System.out.println("\n===================================================");
        System.out.println("                      INVOICE");
        System.out.println("===================================================");
        System.out.printf("Order ID        : %d%n", order.getOrderId());
        System.out.printf("Customer ID     : %d%n", order.getCustomerId());
        System.out.println("---------------------------------------------------");

        System.out.println("Items Ordered:");
        System.out.printf("%-25s %-10s %-5s %-10s%n", "Item", "Price", "Qty", "Total");
        System.out.println("---------------------------------------------------");

        double subtotal = 0;

        for (var entry : order.getCart().getCart().entrySet()) {
            FoodItem item = entry.getKey();
            int qty = entry.getValue();
            double total = item.getPrice() * qty;
            subtotal += total;

            System.out.printf("%-25s ₹%-9.2f %-5d ₹%-9.2f%n",
                    item.getName(), item.getPrice(), qty, total);
        }

        System.out.println("---------------------------------------------------");
        System.out.printf("Subtotal        : ₹%.2f%n", subtotal);

        if (order.getPossibleDiscount() != null) {
            System.out.printf("Discount        : Applied %.0f%%%n",
                    order.getPossibleDiscount().getDiscount() * 100);
        } else {
            System.out.println("Discount        : None");
        }

        System.out.printf("Final Amount    : ₹%.2f%n", order.getFinalAmount());
        System.out.printf("Payment Mode    : %s%n", order.getPaymentMode().getDescription());

        System.out.println("===================================================\n");
    }
}