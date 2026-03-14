package com.tss.repositories;

import com.tss.model.FoodItem;
import com.tss.model.CuisineType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuRepository {
    private static HashMap<CuisineType, List<FoodItem>> menu = new HashMap<>();

    static {
        menu.put(new CuisineType("OTHERS"), new ArrayList<>());
    }

    public static HashMap<CuisineType, List<FoodItem>> getMenuItemList() {
        return menu;
    }

    public static boolean isMenuEmpty() {
        for (CuisineType cuisineType : menu.keySet()) {
            if (!menu.get(cuisineType).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
