package com.tss.repositories;

import com.tss.model.Discount;

import java.util.List;

public interface DiscountRepo {
    void addNewPriceDiscount(Discount discount);
    List<Discount> getAllDiscounts();
    Discount giveMaxPossibleDiscount(double amount);
}
