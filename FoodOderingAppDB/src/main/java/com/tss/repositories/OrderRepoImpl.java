package com.tss.repositories;

import com.tss.config.DBConnection;
import com.tss.model.*;
import com.tss.payments.CashOnDelivery;
import com.tss.payments.PaymentMode;
import com.tss.payments.PaymentModeType;
import com.tss.payments.UPI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderRepoImpl implements OrderRepo {

    private Connection connection;

    public OrderRepoImpl() {
        connection = DBConnection.connect();
    }

    @Override
    public Cart cartOfOrder(long orderId) {
        Cart cart = null;
        try {
            String sql = "SELECT fi.name AS item_name, fi.price, c.name AS cuisine_name, oi.quantity FROM order_items oi JOIN food_item fi USING(food_item_id) JOIN cuisine c USING(cuisine_id) WHERE order_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setLong(1, orderId);
            ResultSet cartContainer = ps.executeQuery();

            HashMap<FoodItem, Integer> items = new HashMap<>();
            while (cartContainer.next()) {
                items.put(new FoodItem(
                        cartContainer.getString("item_name"),
                        cartContainer.getDouble("price"),
                        cartContainer.getString("cuisine_name")
                ), cartContainer.getInt("quantity"));
            }

            cart = new Cart(items);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cart;
    }

    @Override
    public void placeNewOrder(Order order) {

        try{
            String sql1="INSERT INTO price_discount(minimum_amount,discount_percentage) VALUES (?,?) RETURNING discount_id";
            PreparedStatement statement1=connection.prepareStatement(sql1);
            statement1.setDouble(1,order.getPossibleDiscount().getMinimumAmount());
            statement1.setDouble(2,order.getPossibleDiscount().getDiscount());

            ResultSet resultSet1=statement1.executeQuery();
            long discountId=-1;
            if(resultSet1.next()){
                discountId=resultSet1.getLong("discount_id");
            }

            String sql2="INSERT INTO orders(customer_id,final_amount,payment_mode,discount_id) VALUES(?,?,?,?) RETURNING order_id";
            PreparedStatement statement2= connection.prepareStatement(sql2);

            statement2.setLong(1,order.getCustomerId());
            statement2.setDouble(2,order.getFinalAmount());
            statement2.setString(3,order.getPaymentMode().getPaymentModeType().name());
            statement2.setLong(4,discountId);

            ResultSet resultSet2=statement2.executeQuery();

            if(resultSet2.next()){
                long orderId=resultSet2.getLong("order_id");
                String sql3="INSERT INTO order_items(order_id,food_item_id,quantity,price) VALUES (?,?,?,?)";
                PreparedStatement statement3=connection.prepareStatement(sql3);
                statement3.setLong(1,orderId);

                for(FoodItem item : order.getCart().getCart().keySet()){
                    statement3.setLong(2,item.getId());
                    statement3.setInt(3,order.getCart().getCart().get(item));
                    statement3.setDouble(4,item.getPrice());
                    statement3.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        try {
            String sql = "SELECT o.order_id,o.customer_id,o.final_amount,o.status,o.payment_mode, pd.minimum_amount, pd.discount_percentage FROM orders o JOIN price_discount pd USING(discount_id)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                long orderId = resultSet.getLong("order_id");
                double amount = resultSet.getDouble("final_amount");

                Cart cart = cartOfOrder(orderId);
                Discount discount = new PriceDiscount(resultSet.getDouble("minimum_amount"), resultSet.getDouble("discount_percentage"));

                PaymentModeType mode = PaymentModeType.valueOf(resultSet.getString("payment_mode"));
                PaymentMode payment=mode.create(amount);
                OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));

                orders.add(
                        new Order(
                                orderId,
                                discount,
                                cart,
                                payment,
                                resultSet.getLong("customer_id"),
                                status
                        )
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }


    @Override
    public List<Order> ordersFromDeliveryAgentId(long deliveryPartnerId) {
        List<Order> orders = new ArrayList<>();

        try {
            String sql = "SELECT o.order_id,o.customer_id,o.final_amount,o.status,o.payment_mode, pd.minimum_amount, pd.discount_percentage FROM orders o JOIN order_assignment os USING(order_id) JOIN price_discount pd USING(discount_id) WHERE os.delivery_partner_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, deliveryPartnerId);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                long orderId = resultSet.getLong("order_id");
                double amount = resultSet.getDouble("final_amount");

                Cart cart = cartOfOrder(orderId);
                Discount discount = new PriceDiscount(resultSet.getDouble("minimum_amount"), resultSet.getDouble("discount_percentage"));

                PaymentModeType mode = PaymentModeType.valueOf(resultSet.getString("payment_mode"));
                PaymentMode payment=mode.create(amount);
                OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));

                orders.add(
                        new Order(
                                orderId,
                                discount,
                                cart,
                                payment,
                                resultSet.getLong("customer_id"),
                                status
                        )
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

    @Override
    public List<Order> ordersFromCustomerId(long customerId) {
        List<Order> orders = new ArrayList<>();

        try {
            String sql = "SELECT o.order_id,o.customer_id,o.final_amount,o.status,o.payment_mode, pd.minimum_amount, pd.discount_percentage FROM orders o JOIN price_discount pd USING(discount_id) WHERE o.customer_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, customerId);
            ResultSet resultSet = ps.executeQuery();


            while (resultSet.next()) {
                long orderId = resultSet.getLong("order_id");
                double amount = resultSet.getDouble("final_amount");

                Cart cart = cartOfOrder(orderId);
                Discount discount = new PriceDiscount(resultSet.getDouble("minimum_amount"), resultSet.getDouble("discount_percentage"));

                PaymentModeType mode = PaymentModeType.valueOf(resultSet.getString("payment_mode"));
                PaymentMode payment=mode.create(amount);
                OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));

                orders.add(
                        new Order(
                                orderId,
                                discount,
                                cart,
                                payment,
                                resultSet.getLong("customer_id"),
                                status
                        )
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

    @Override
    public List<Order> getOrderFromStatus(OrderStatus orderStatus) {
        List<Order> orders = new ArrayList<>();

        try {
            String sql = "SELECT o.order_id,o.customer_id,o.final_amount,o.status,o.payment_mode, pd.minimum_amount, pd.discount_percentage FROM orders o JOIN price_discount pd USING(discount_id) WHERE o.status=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,orderStatus.toString());
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                long orderId = resultSet.getLong("order_id");
                double amount = resultSet.getDouble("final_amount");

                Cart cart = cartOfOrder(orderId);
                Discount discount = new PriceDiscount(resultSet.getDouble("minimum_amount"), resultSet.getDouble("discount_percentage"));

                PaymentModeType mode = PaymentModeType.valueOf(resultSet.getString("payment_mode"));
                PaymentMode payment=mode.create(amount);
                OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));

                orders.add(
                        new Order(
                                orderId,
                                discount,
                                cart,
                                payment,
                                resultSet.getLong("customer_id"),
                                status
                        )
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }
}
