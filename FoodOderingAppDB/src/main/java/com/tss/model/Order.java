package com.tss.model;

import com.tss.model.users.DeliveryPartner;
import com.tss.payments.PaymentMode;

public class Order {
    private long orderId;
    private static long count=1;
    private Cart cart;
    private DeliveryPartner deliveryPartner;
    private Discount possibleDiscount;
    private PaymentMode paymentMode;
    private long customerId;
    private OrderStatus status;
    private double finalAmount;

    public Order(Cart cart, Discount possibleDiscount, PaymentMode paymentMode, long customerId) {
        this.orderId=count++;
        this.cart = new Cart(cart);
        this.possibleDiscount = possibleDiscount;
        this.paymentMode = paymentMode;
        this.customerId = customerId;
        this.status = OrderStatus.ACCEPTED;
        if(possibleDiscount!=null){
            this.finalAmount=(paymentMode.getAmount()-(paymentMode.getAmount()*possibleDiscount.getDiscount()));
        }else{
            this.finalAmount=paymentMode.getAmount();
        }
    }

    public Order(long orderId,Discount possibleDiscount, Cart cart, PaymentMode paymentMode, long customerId,OrderStatus status) {
        this.orderId=orderId;
        this.cart = cart;
        this.possibleDiscount = possibleDiscount;
        this.paymentMode = paymentMode;
        this.customerId = customerId;
        this.status = status;
        if(possibleDiscount!=null){
            this.finalAmount=(paymentMode.getAmount()-(paymentMode.getAmount()*possibleDiscount.getDiscount()));
        }else{
            this.finalAmount=paymentMode.getAmount();
        }
    }

    public Order(long orderId, Discount possibleDiscount, Cart cart, PaymentMode paymentMode,
                 long customerId, OrderStatus status, Long deliveryPartnerId, String deliveryPartnerName) {

        this.orderId = orderId;
        this.cart = cart;
        this.possibleDiscount = possibleDiscount;
        this.paymentMode = paymentMode;
        this.customerId = customerId;
        this.status = status;

        if (possibleDiscount != null) {
            this.finalAmount = paymentMode.getAmount() - (paymentMode.getAmount() * possibleDiscount.getDiscount());
        } else {
            this.finalAmount = paymentMode.getAmount();
        }

        if (deliveryPartnerId != null && deliveryPartnerName != null) {
            this.deliveryPartner = new DeliveryPartner(deliveryPartnerId, deliveryPartnerName);
        } else {
            this.deliveryPartner = null;
        }
    }

    public void ChangeState(OrderStatus orderstatus){
        status=orderstatus;
    }

    public long getOrderId() {
        return orderId;
    }

    public Cart getCart() {
        return cart;
    }

    public DeliveryPartner getDeliveryPartner() {
        return deliveryPartner;
    }

    public Discount getPossibleDiscount() {
        return possibleDiscount;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public long getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public double getFinalAmount() {
        return finalAmount;
    }



    public void setDeliveryPartner(DeliveryPartner deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
    }

    @Override
    public String toString() {
        return String.format(
                "Order #%d | Customer: %d | Status: %s | Payment: %s | Amount: ₹%.2f | Delivery Partner: %s ",
                orderId,
                customerId,
                status,
                paymentMode.getDescription(),
                finalAmount,
                (deliveryPartner != null ? deliveryPartner.getName() : "NOT ASSIGNED")

        );
    }
}
