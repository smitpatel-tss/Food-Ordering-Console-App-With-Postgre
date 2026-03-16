package com.tss.repositories;

import com.tss.config.DBConnection;
import com.tss.model.CuisineType;
import com.tss.model.FoodItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuRepoImpl implements MenuRepo{

    private Connection connection;
    public MenuRepoImpl(){
        connection= DBConnection.connect();
    }


    @Override
    public void addNewFoodItem(FoodItem foodItem) {

        try{
            String sql="INSERT INTO food_item(cuisine_id,name,price) VALUES(?,?,?)";
            PreparedStatement statement=connection.prepareStatement(sql);

            statement.setLong(1,foodItem.getCuisine().getId());
            statement.setString(2,foodItem.getName());
            statement.setDouble(3,foodItem.getPrice());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addNewCuisine(String name) {
        try{
            String sql="INSERT INTO cuisine(name) VALUES(?)";
            PreparedStatement statement=connection.prepareStatement(sql);

            statement.setString(1,name);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<CuisineType> getALlCuisines() {
        List<CuisineType> cuisines=new ArrayList<>();

        try{
            String sql="SELECT cuisine_id,name FROM cuisine";
            PreparedStatement statement=connection.prepareStatement(sql);

            ResultSet resultSet=statement.executeQuery();

            while(resultSet.next()){
                cuisines.add(new CuisineType(resultSet.getLong("cuisine_id"),
                        resultSet.getString("name")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cuisines;
    }

    @Override
    public List<FoodItem> getAllFoodItems(long cuisineId) {
        List<FoodItem> foodItems=new ArrayList<>();

        try{
            String sql="SELECT fi.food_item_id,fi.cuisine_id,fi.name as name,fi.price,fi.available,c.name as cuisine_name FROM food_item fi JOIN cuisine c USING(cuisine_id) where fi.cuisine_id=?";
            PreparedStatement statement=connection.prepareStatement(sql);

            statement.setLong(1,cuisineId);
            ResultSet resultSet=statement.executeQuery();

            while(resultSet.next()){
                foodItems.add(new FoodItem(resultSet.getLong("food_item_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("cuisine_name"),
                        resultSet.getBoolean("available"),
                        resultSet.getLong("cuisine_id")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return foodItems;
    }

    public HashMap<CuisineType, List<FoodItem>> getMenu() {

        HashMap<CuisineType, List<FoodItem>> menu = new HashMap<>();

        try {

            String sql = """
                SELECT c.cuisine_id,
                       c.name AS cuisine_name,
                       fi.food_item_id,
                       fi.name AS food_name,
                       fi.price,
                       fi.available
                FROM cuisine c
                LEFT JOIN food_item fi
                ON c.cuisine_id = fi.cuisine_id
                """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                long cuisineId = rs.getLong("cuisine_id");
                String cuisineName = rs.getString("cuisine_name");

                CuisineType cuisine = new CuisineType(cuisineId, cuisineName);

                menu.putIfAbsent(cuisine, new ArrayList<>());

                long foodId = rs.getLong("food_item_id");

                if (!rs.wasNull()) {

                    FoodItem foodItem = new FoodItem(
                            foodId,
                            rs.getString("food_name"),
                            rs.getDouble("price"),
                            cuisineName,
                            rs.getBoolean("available"),
                            cuisineId
                    );

                    menu.get(cuisine).add(foodItem);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return menu;
    }

    @Override
    public FoodItem getItemFromId(long itemId) {
        FoodItem foodItem=null;
        try{
            String sql="SELECT fi.food_item_id,fi.cuisine_id,fi.name as name,fi.price,fi.available,c.name as cuisine_name FROM food_item fi JOIN cuisine c USING(cuisine_id) where fi.food_item_id=?";
            PreparedStatement statement=connection.prepareStatement(sql);

            statement.setLong(1,itemId);
            ResultSet resultSet=statement.executeQuery();

            if(resultSet.next()){
                foodItem=new FoodItem(resultSet.getLong("food_item_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("cuisine_name"),
                        resultSet.getBoolean("available"),
                        resultSet.getLong("cuisine_id"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return foodItem;
    }

    @Override
    public boolean removeItem(long itemId) {

        try {
            String sql = "DELETE FROM food_item WHERE food_item_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, itemId);

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
    @Override
    public boolean removeCuisine(long cuisineId) {

        try {
            String sql = "DELETE FROM cuisine WHERE cuisine_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, cuisineId);

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public void changePrice(long id, double newPrice) {

        try {
            String sql = "UPDATE food_item SET price=? WHERE food_item_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setDouble(1, newPrice);
            statement.setLong(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isMenuEmpty() {
        try {
            String sql = "SELECT 1 FROM food_item WHERE available=true";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            return !rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}
