package com.tss.payments;

public interface PaymentMode {
    double getAmount();
    String getDescription();
    PaymentModeType getPaymentModeType();
}
