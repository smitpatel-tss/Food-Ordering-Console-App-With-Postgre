package com.tss.payments;

public class CashOnDelivery implements PaymentMode{
    private double amount;

    public CashOnDelivery(double amount) {
        this.amount = amount;
    }

    @Override
    public PaymentModeType getPaymentModeType() {
        return PaymentModeType.COD;
    }

    public CashOnDelivery() {
    }

    @Override
    public String getDescription() {
        return "Cash On Delivery";
    }

    @Override
    public double getAmount() {
        return amount;
    }
}
