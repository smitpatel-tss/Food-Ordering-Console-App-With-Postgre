package com.tss.services;

import com.tss.model.FoodItem;
import com.tss.model.Menu;
import com.tss.model.CuisineType;
import com.tss.repositories.MenuRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MenuService {
    private Menu menu;

    private MenuService() {
        menu = new Menu(MenuRepository.getMenuItemList());
    }

    private static class InstanceContainer {
        static MenuService obj = new MenuService();
    }

    public static MenuService getInstance() {
        return InstanceContainer.obj;
    }

    public void displayMenu() {

        System.out.println("\n======================== MENU ========================");

        for (CuisineType cuisine : menu.getMenu().keySet()) {

            List<FoodItem> foodItems = menu.getMenu().get(cuisine);
            if (foodItems == null || foodItems.isEmpty()) continue;

            System.out.println("\n[" + cuisine.getName().toUpperCase() + "]");
            System.out.println("------------------------------------------------------");

            System.out.printf("%-6s %-25s %-10s%n", "ID", "Name", "Price");
            System.out.println("------------------------------------------------------");

            for (FoodItem item : foodItems) {
                System.out.printf("%-6d %-25s ₹%-10.2f%n",
                        item.getId(),
                        item.getName(),
                        item.getPrice());
            }
        }

        System.out.println("\n======================================================\n");
    }

    public FoodItem getItemFromId(long id) {
        for (CuisineType cuisine : menu.getMenu().keySet()) {
            List<FoodItem> foodItems = menu.getMenu().get(cuisine);

            for (FoodItem items : foodItems) {
                if (items.getId() == id) {
                    return items;
                }
            }
        }
        return null;
    }

    public boolean removeItem(long id) {

        for (List<FoodItem> foodItems : menu.getMenu().values()) {
            Iterator<FoodItem> iterator = foodItems.iterator();
            while (iterator.hasNext()) {
                FoodItem item = iterator.next();

                if (item.getId() == id) {
                    iterator.remove();
                    return true;
                }
            }
        }

        return false;
    }

    public boolean removeCuisine(long id) {

        List<CuisineType> cuisines = new ArrayList<>(menu.getMenu().keySet());
        for (CuisineType cuisine : cuisines) {
            if (cuisine.getId() == id) {
                menu.getMenu().remove(cuisine);
                return true;
            }
        }
        return false;
    }

}
