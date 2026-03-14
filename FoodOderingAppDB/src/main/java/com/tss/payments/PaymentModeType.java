package com.tss.payments;

public enum PaymentModeType {
    UPI{
        public PaymentMode create(double amount){
            return new UPI(amount);
        }
    }
    ,
    COD{
        public PaymentMode create(double amount){
            return new CashOnDelivery(amount);
        }
    };

    public abstract PaymentMode create(double amount);
}
