package com.tss.repositories;

import com.tss.model.CuisineType;
import com.tss.model.FoodItem;

import java.util.List;

public interface MenuRepo {
    void addNewFoodItem(FoodItem foodItem);
    void addNewCuisine(String name);
    List<CuisineType> getALlCuisines();
    List<FoodItem> getAllFoodItems(long cuisineId);
    FoodItem getItemFromId(long itemId);
    boolean removeItem(long itemId);
    boolean removeCuisine(long cuisineId);
    void changePrice(long id,double newPrice);
}
