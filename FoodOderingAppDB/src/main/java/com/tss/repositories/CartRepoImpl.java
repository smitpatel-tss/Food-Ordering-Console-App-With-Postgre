package com.tss.repositories;

import com.tss.config.DBConnection;
import com.tss.model.Cart;
import com.tss.model.FoodItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CartRepoImpl implements CartRepo {
    private Connection connection;

    public CartRepoImpl(){
        connection= DBConnection.connect();
    }


    public Long getCartIdByUserId(long userId){

        try{
            String query = "SELECT cart_id FROM carts WHERE user_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("cart_id");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public long createCart(long userId)  {

        try{
            String query = "INSERT INTO carts(user_id) VALUES (?) RETURNING cart_id";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("cart_id");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return getCartIdByUserId(userId);
    }

    @Override
    public void addItemToCart(long userId, long foodItemId, int quantity) {
        try{

            Long cartId = getCartIdByUserId( userId);

            if (cartId == null) {
                cartId = createCart( userId);
            }

            String checkItem = "SELECT quantity FROM cart_items WHERE cart_id=? AND food_item_id=?";
            PreparedStatement ps1 = connection.prepareStatement(checkItem);

            ps1.setLong(1, cartId);
            ps1.setLong(2, foodItemId);

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {

                int currentQuantity = rs.getInt("quantity");

                String update = "UPDATE cart_items SET quantity=? WHERE cart_id=? AND food_item_id=?";
                PreparedStatement ps2 = connection.prepareStatement(update);

                ps2.setInt(1, currentQuantity + quantity);
                ps2.setLong(2, cartId);
                ps2.setLong(3, foodItemId);

                ps2.executeUpdate();

            } else {

                String insert = "INSERT INTO cart_items(cart_id, food_item_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement ps3 = connection.prepareStatement(insert);

                ps3.setLong(1, cartId);
                ps3.setLong(2, foodItemId);
                ps3.setInt(3, quantity);

                ps3.executeUpdate();
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeItemFromCart(long userId, long foodItemId, int quantity) {

        try  {

            Long cartId = getCartIdByUserId(userId);
            if (cartId == null) {
                return;
            }

            String checkQuery = "SELECT quantity FROM cart_items WHERE cart_id=? AND food_item_id=?";
            PreparedStatement ps1 = connection.prepareStatement(checkQuery);

            ps1.setLong(1, cartId);
            ps1.setLong(2, foodItemId);

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {

                int currentQty = rs.getInt("quantity");

                if (currentQty <= quantity) {

                    String deleteQuery = "DELETE FROM cart_items WHERE cart_id=? AND food_item_id=?";
                    PreparedStatement ps2 = connection.prepareStatement(deleteQuery);

                    ps2.setLong(1, cartId);
                    ps2.setLong(2, foodItemId);

                    ps2.executeUpdate();

                } else {

                    String updateQuery = "UPDATE cart_items SET quantity=? WHERE cart_id=? AND food_item_id=?";
                    PreparedStatement ps3 = connection.prepareStatement(updateQuery);

                    ps3.setInt(1, currentQty - quantity);
                    ps3.setLong(2, cartId);
                    ps3.setLong(3, foodItemId);

                    ps3.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void clearCart(long userId) {

        try  {

            Long cartId = getCartIdByUserId( userId);

            if (cartId == null) {
                return;
            }

            String query = "DELETE FROM cart_items WHERE cart_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setLong(1, cartId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Cart getCart(long userId) {

        try  {

            Long cartId = getCartIdByUserId(userId);

            if (cartId == null) {
                return new Cart(new HashMap<>(), 0);
            }

            String query = """
            SELECT f.food_item_id, f.name, f.price, f.available,
                   c.cuisine_id, c.name AS cuisine_name,
                   ci.quantity
            FROM cart_items ci
            JOIN food_item f ON ci.food_item_id = f.food_item_id
            JOIN cuisine c ON f.cuisine_id = c.cuisine_id
            WHERE ci.cart_id = ?
        """;

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, cartId);

            ResultSet rs = ps.executeQuery();

            HashMap<FoodItem, Integer> items = new HashMap<>();
            double totalPrice = 0;

            while (rs.next()) {

                long foodId = rs.getLong("food_item_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                boolean available = rs.getBoolean("available");
                long cuisineId = rs.getLong("cuisine_id");
                String cuisineName = rs.getString("cuisine_name");
                int quantity = rs.getInt("quantity");

                FoodItem foodItem = new FoodItem(
                        foodId,
                        name,
                        price,
                        cuisineName,
                        available,
                        cuisineId
                );

                items.put(foodItem, quantity);

                totalPrice += price * quantity;
            }

            return new Cart(items, totalPrice);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new Cart(new HashMap<>(), 0);
    }

    @Override
    public double calculateCartTotal(long userId) {

        try {

            Long cartId = getCartIdByUserId(userId);

            if (cartId == null) {
                return 0;
            }

            String query = """
            SELECT SUM(f.price * ci.quantity) AS total
            FROM cart_items ci
            JOIN food_item f ON ci.food_item_id = f.food_item_id
            WHERE ci.cart_id = ?
        """;

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, cartId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    @Override
    public boolean isCartEmpty(long userId) {
        try {

            Long cartId = getCartIdByUserId(userId);
            if (cartId == null) {
                return true;
            }

            String query = "SELECT 1 FROM cart_items WHERE cart_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setLong(1, cartId);

            ResultSet rs = ps.executeQuery();

            return !rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }
}
