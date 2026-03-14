package com.tss.model;

import java.util.HashMap;
import java.util.List;

public class Menu {
    private HashMap<CuisineType,List<FoodItem>> menu;

    public Menu(HashMap<CuisineType,List<FoodItem>> menu) {
        this.menu = menu;
    }

    public HashMap<CuisineType,List<FoodItem>> getMenu() {
        return menu;
    }
}
