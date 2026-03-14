package com.tss.model;

import java.util.HashMap;

public class Cart {
    private HashMap<FoodItem,Integer> cart;
    private double totalCartPrice;

    public Cart(){
        cart=new HashMap<>();
    }

    public Cart(Cart otherCart) {
        this.cart = new HashMap<>(otherCart.cart);
        this.totalCartPrice = otherCart.totalCartPrice;
    }

    public Cart(HashMap <FoodItem,Integer> items,double totalCartPrice){
        this.cart=items;
        this.totalCartPrice=totalCartPrice;
    }

    public Cart(HashMap <FoodItem,Integer> items){
        this.cart=items;
        this.totalCartPrice=totalCartPrice;
    }

    public void setTotalCartPrice(double totalCartPrice) {
        this.totalCartPrice = totalCartPrice;
    }

    public void addItemToCart(FoodItem item, int quantity){

        cart.put(item,cart.getOrDefault(item,0)+quantity);
        totalCartPrice+=(item.getPrice()*quantity);
    }

    public void removeItemFromCart(FoodItem item, int quantity){
        if(cart.containsKey(item)){
            cart.put(item,cart.get(item)-quantity);
        }
        if(cart.get(item)<=0){
            cart.remove(item);
        }
        totalCartPrice-=(item.getPrice()*quantity);
    }

    public void displayCart() {

        System.out.println("\n======================== YOUR CART ========================");

        if (cart.isEmpty()) {
            System.out.println("NONE!");
            System.out.println("===========================================================\n");
            return;
        }

        System.out.printf("%-4s %-25s %-10s %-5s %-10s%n",
                "Id.", "Item", "Price", "Qty", "Total");
        System.out.println("-----------------------------------------------------------");

        double total = 0;

        for (var entry : cart.entrySet()) {
            FoodItem item = entry.getKey();
            int qty = entry.getValue();
            double itemTotal = item.getPrice() * qty;
            total += itemTotal;

            System.out.printf("%-4d %-25s Rs.%-9.2f %-5d Rs.%-9.2f%n",
                    item.getId(), item.getName(), item.getPrice(), qty, itemTotal);
        }

        System.out.println("-----------------------------------------------------------");
        System.out.printf("Current Total: Rs. %.2f%n", total);
        System.out.println("===========================================================\n");
    }

    public HashMap<FoodItem, Integer> getCart() {
        return cart;
    }

    public double getTotalCartPrice() {
        return totalCartPrice;
    }

    public void emptyTheCart(){
        cart.clear();
        totalCartPrice=0;
    }
}
