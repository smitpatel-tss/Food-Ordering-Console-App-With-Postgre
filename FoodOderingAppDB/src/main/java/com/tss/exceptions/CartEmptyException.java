package com.tss.exceptions;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException() {
        super("Cart is Empty!");
    }
}
