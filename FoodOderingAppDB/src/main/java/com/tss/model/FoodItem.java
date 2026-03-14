package com.tss.model;

public class FoodItem {
    private long id;
    private String name;
    private double price;
    private CuisineType cuisine;
    private boolean available;

    public FoodItem(long id, String name, double price, CuisineType cuisine) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cuisine = cuisine;
        this.available = true;
    }

    public FoodItem(String name, double price, String cuisineName) {
        this.name = name;
        this.price = price;
        this.cuisine = new CuisineType(cuisineName);
        this.available = true;
    }

    public FoodItem(long id,String name, double price, String cuisineName,boolean available,long cuisine_id) {
        this.name = name;
        this.price = price;
        this.cuisine = new CuisineType(cuisine_id,cuisineName);
        this.available = available;
    }

    public CuisineType getCuisine() {
        return cuisine;
    }

    public void setCuisine(CuisineType cuisine) {
        this.cuisine = cuisine;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodItem)) return false;

        FoodItem foodItem = (FoodItem) o;
        return name.equals(foodItem.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
