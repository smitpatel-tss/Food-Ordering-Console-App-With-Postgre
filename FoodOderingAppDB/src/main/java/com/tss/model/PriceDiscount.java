package com.tss.model;

public class PriceDiscount implements Discount {
    private double minimumAmount;
    private double discount;
    private int id;
    private static int discountIdCounter = 1;

    public PriceDiscount(double minimumAmount, double discount) {
        this.minimumAmount = minimumAmount;
        this.discount = discount;
        this.id = discountIdCounter++;
    }

    public PriceDiscount(int id,double minimumAmount, double discount) {
        this.minimumAmount = minimumAmount;
        this.discount = discount;
        this.id = id;
    }

    @Override
    public double getDiscount() {
        return discount;
    }

    public double getMinimumAmount() {
        return minimumAmount;
    }

    @Override
    public boolean checkDiscount(double amount) {
        return amount >= minimumAmount;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return discount * 100 + "% discount on order on or above Rs. " + minimumAmount + ".";
    }
}
