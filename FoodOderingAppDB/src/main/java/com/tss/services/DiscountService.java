package com.tss.services;

import com.tss.model.Discount;
import com.tss.model.PriceDiscount;
import com.tss.repositories.DiscountRepository;

public class DiscountService {
    private DiscountRepository discountRepository;

    private DiscountService() {
        discountRepository = DiscountRepository.getInstance();
    }

    private static class InstanceContainer {
        static DiscountService obj = new DiscountService();
    }

    public static DiscountService getInstance() {
        return InstanceContainer.obj;
    }

    public void displayDiscounts() {
        if (discountRepository.getDiscounts().isEmpty()) {
            System.out.println("No Discounts Currently Available!");
            return;
        }
        for (Discount discount : discountRepository.getDiscounts()) {
            System.out.println(discount.getDescription());
        }
    }

    public void addNewDiscount(double minimumAmount, double discount) {
        discountRepository.getDiscounts().add(new PriceDiscount(minimumAmount, discount));
    }

    public Discount giveMaxPossibleDiscount(double amount) {
        double maxPossibleDiscount = 0;
        Discount possibleDiscount = null;
        for (Discount discount : discountRepository.getDiscounts()) {
            if (discount.checkDiscount(amount)) {
                if (maxPossibleDiscount < discount.getDiscount()) {
                    maxPossibleDiscount = discount.getDiscount();
                    possibleDiscount = discount;
                }
            }
        }
        return possibleDiscount;
    }
}
