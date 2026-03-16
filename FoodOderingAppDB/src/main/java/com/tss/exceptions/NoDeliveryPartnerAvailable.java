package com.tss.exceptions;

public class NoDeliveryPartnerAvailable extends RuntimeException {
    public NoDeliveryPartnerAvailable() {
        super("No Delivery Partners Currently Available");
    }
}
