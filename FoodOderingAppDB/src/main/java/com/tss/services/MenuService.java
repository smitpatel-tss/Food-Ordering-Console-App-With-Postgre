package com.tss.services;

import com.tss.model.FoodItem;
import com.tss.model.CuisineType;
import com.tss.repositories.MenuRepo;
import com.tss.repositories.MenuRepoImpl;

import java.util.HashMap;
import java.util.List;

public class MenuService {
    private MenuRepo menuRepo;

    private MenuService() {
        menuRepo=new MenuRepoImpl();
    }

    private static class InstanceContainer {
        static MenuService obj = new MenuService();
    }

    public static MenuService getInstance() {
        return InstanceContainer.obj;
    }

    public void displayMenu() {
        HashMap<CuisineType, List<FoodItem>> menu=menuRepo.getMenu();

        System.out.println("\n======================== MENU ========================");

        for (CuisineType cuisine : menu.keySet()) {

            List<FoodItem> foodItems = menu.get(cuisine);
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
        FoodItem item=menuRepo.getItemFromId(id);
        return item;
    }

    public boolean removeItem(long id) {

        return menuRepo.removeItem(id);
    }

    public boolean removeCuisine(long id) {

        return menuRepo.removeCuisine(id);
    }

    public void addNewCuisine(String cuisine){
        menuRepo.addNewCuisine(cuisine);
    }

    public void addNewFoodItem(FoodItem foodItem){
        menuRepo.addNewFoodItem(foodItem);
    }

    public List<CuisineType> getAllCuisines(){
        return menuRepo.getALlCuisines();
    }

    public boolean isEmpty(){
        return menuRepo.isMenuEmpty();
    }

    public void changePrice(long id, double newPrice){
        menuRepo.changePrice(id,newPrice);
    }

}
