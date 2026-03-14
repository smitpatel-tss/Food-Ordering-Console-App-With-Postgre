package com.tss.model;

public enum OrderStatus {

    ACCEPTED("Accepted"),
    OUT_FOR_DELIVERY("Out For Delivery"),
    DELIVERED("Delivered");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}