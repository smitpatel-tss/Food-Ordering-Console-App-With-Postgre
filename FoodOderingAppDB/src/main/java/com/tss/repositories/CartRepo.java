package com.tss.repositories;

import com.tss.model.Cart;

public interface CartRepo {
    Long getCartIdByUserId(long userId);
    long createCart(long userId);
    void addItemToCart(long userId, long foodItemId, int quantity);
    void removeItemFromCart(long userId, long foodItemId, int quantity);
    void clearCart(long userId);
    Cart getCart(long userId);

    double calculateCartTotal(long userId);

    boolean isCartEmpty(long userId);
}
