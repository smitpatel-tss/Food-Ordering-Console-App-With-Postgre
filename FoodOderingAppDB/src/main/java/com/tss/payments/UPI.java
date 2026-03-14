package com.tss.payments;

public class UPI implements PaymentMode {

    private double amount;

    public UPI(double amount) {
        this.amount = amount;
    }

    @Override
    public PaymentModeType getPaymentModeType() {
        return PaymentModeType.UPI;
    }

    @Override
    public String getDescription() {
        return "UPI";
    }

    @Override
    public double getAmount() {
        return amount;
    }
}