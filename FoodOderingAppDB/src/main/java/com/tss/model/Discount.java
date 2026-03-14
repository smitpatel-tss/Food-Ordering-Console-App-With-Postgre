package com.tss.model;

public interface Discount {
    double getDiscount();
    boolean checkDiscount(double amount);
    String getDescription();
    int getId();
    double getMinimumAmount();
}
