package com.tss.repositories;

import com.tss.model.Discount;

import java.util.ArrayList;
import java.util.List;

public class DiscountRepository {
    private List<Discount> discounts;

    private DiscountRepository() {
        discounts = new ArrayList<>();
    }

    private static class InstanceContainer {
        static DiscountRepository obj = new DiscountRepository();
    }

    public static DiscountRepository getInstance() {
        return InstanceContainer.obj;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

}
