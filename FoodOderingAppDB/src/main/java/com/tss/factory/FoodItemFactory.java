package com.tss.factory;

import com.tss.model.CuisineType;
import com.tss.model.FoodItem;

public class FoodItemFactory {
    static long count=1;

    public static FoodItem getFoodItemInstance(String name, Double price, CuisineType cuisine){
        return new FoodItem(count++,name,price,cuisine);
    }
}
