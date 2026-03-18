package com.tss.repositories;

import com.tss.config.DBConnection;
import com.tss.model.*;
import com.tss.model.users.DeliveryPartner;
import com.tss.payments.PaymentMode;
import com.tss.payments.PaymentModeType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeliveryPartnerRepoImpl implements DeliveryPartnerRepo {
    private Connection connection;

    public DeliveryPartnerRepoImpl() {
        connection = DBConnection.connect();
    }

    @Override
    public List<DeliveryPartner> getAllDeliveryPartners() {
        List<DeliveryPartner> allDeliveryPartners = new ArrayList<>();

        try {
            String sql = "select u.user_id,u.name,u.phone,is_active,is_available from delivery_partner dp join users u using(user_id)";

            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    allDeliveryPartners.add(
                            new DeliveryPartner(
                                    resultSet.getLong("user_id"),
                                    resultSet.getString("name"),
                                    resultSet.getLong("phone"),
                                    resultSet.getBoolean("is_active"),
                                    resultSet.getBoolean("is_available")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return allDeliveryPartners;
    }

    @Override
    public void addNewDeliveryPartner(DeliveryPartner deliveryPartner) {

        try {
            connection.setAutoCommit(false);

            String sql1 = "INSERT INTO users(name,user_type,phone,password) VALUES (?,?::user_type,?,?) RETURNING user_id";

            try (PreparedStatement ps1 = connection.prepareStatement(sql1)) {

                ps1.setString(1, deliveryPartner.getName());
                ps1.setString(2, deliveryPartner.getUserType().name());
                ps1.setLong(3, deliveryPartner.getAccountInfo().getPhoneNumber());
                ps1.setString(4, deliveryPartner.getAccountInfo().getPassword());

                try (ResultSet resultSet = ps1.executeQuery()) {

                    resultSet.next();
                    long id = resultSet.getLong("user_id");

                    String sql2 = "INSERT INTO delivery_partner(user_id) VALUES (?)";

                    try (PreparedStatement ps2 = connection.prepareStatement(sql2)) {
                        ps2.setLong(1, id);
                        ps2.executeUpdate();
                    }
                }
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public List<Order> pendingOrders(long deliveryPartnerId) {
        List<Order> orders = new ArrayList<>();

        String sql = """
                    SELECT 
                        o.order_id,
                        o.customer_id,
                        o.final_amount,
                        o.status,
                        o.payment_mode,
                        pd.minimum_amount,
                        pd.discount_percentage,
                        os.delivery_partner_id,
                        u_dp.name AS delivery_partner_name
                    FROM orders o
                    JOIN order_assignment os USING(order_id)
                    LEFT JOIN price_discount pd USING(discount_id)
                    LEFT JOIN users u_dp ON os.delivery_partner_id = u_dp.user_id
                    WHERE os.delivery_partner_id = ? AND o.status = 'OUT_FOR_DELIVERY'
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, deliveryPartnerId);

            try (ResultSet resultSet = ps.executeQuery()) {

                String innerSql = """
                            SELECT fi.name AS item_name, fi.price, c.name AS cuisine_name, oi.quantity
                            FROM order_items oi
                            JOIN food_item fi USING(food_item_id)
                            JOIN cuisine c USING(cuisine_id)
                            WHERE order_id = ?
                        """;

                try (PreparedStatement innerPs = connection.prepareStatement(innerSql)) {

                    while (resultSet.next()) {
                        long orderId = resultSet.getLong("order_id");

                        innerPs.setLong(1, orderId);

                        try (ResultSet cartContainer = innerPs.executeQuery()) {
                            double amount = resultSet.getDouble("final_amount");

                            HashMap<FoodItem, Integer> items = new HashMap<>();

                            while (cartContainer.next()) {
                                items.put(new FoodItem(
                                                cartContainer.getString("item_name"),
                                                cartContainer.getDouble("price"),
                                                cartContainer.getString("cuisine_name")
                                        ),
                                        cartContainer.getInt("quantity"));
                            }

                            Cart cart = new Cart(items, amount);

                            Discount discount = new PriceDiscount(
                                    resultSet.getDouble("minimum_amount"),
                                    resultSet.getDouble("discount_percentage")
                            );

                            PaymentModeType mode = PaymentModeType.valueOf(resultSet.getString("payment_mode"));
                            PaymentMode payment = mode.create(amount);
                            OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));

                            Long dpId = resultSet.getLong("delivery_partner_id");
                            if (resultSet.wasNull()) dpId = null;

                            String dpName = resultSet.getString("delivery_partner_name");
                            if (dpName != null && dpName.isEmpty()) dpName = null;

                            orders.add(new Order(
                                    orderId,
                                    discount,
                                    cart,
                                    payment,
                                    resultSet.getLong("customer_id"),
                                    status,
                                    dpId,
                                    dpName
                            ));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return orders;
    }

    @Override
    public void completeOrder(long deliveryPartnerId, long orderId) {
        try {
            connection.setAutoCommit(false);

            String sql = """
                        UPDATE orders o
                        SET status = 'DELIVERED'
                        FROM order_assignment os
                        WHERE o.order_id = ? AND os.order_id = o.order_id AND os.delivery_partner_id = ?
                    """;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, orderId);
                statement.setLong(2, deliveryPartnerId);
                int updated = statement.executeUpdate();

                if (updated == 0) {
                    throw new RuntimeException("Order not found!");
                }
            }

            String sql1 = "UPDATE delivery_partner SET is_available=? WHERE user_id=?";

            try (PreparedStatement statement1 = connection.prepareStatement(sql1)) {
                statement1.setBoolean(1, true);
                statement1.setLong(2, deliveryPartnerId);
                statement1.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void assignOrder() {

        try {

            connection.setAutoCommit(false);

            String sql = """
                    SELECT o.order_id FROM orders o
                    WHERE o.status = 'ACCEPTED'
                    AND NOT EXISTS (
                        SELECT 1
                        FROM order_assignment oa
                        WHERE oa.order_id = o.order_id
                    )
                    ORDER BY o.placed_at LIMIT 1
                    FOR UPDATE SKIP LOCKED
                    """;

            Long orderId = null;

            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    orderId = resultSet.getLong("order_id");
                }
            }

            if (orderId == null) {
                connection.rollback();
                return;
            }

            String sql1 = """
                    SELECT user_id
                    FROM delivery_partner
                    WHERE is_available = true AND is_active = true
                    ORDER BY last_delivery_at
                    LIMIT 1
                    FOR UPDATE SKIP LOCKED
                    """;

            Long deliveryPartnerId = null;

            try (PreparedStatement statement1 = connection.prepareStatement(sql1);
                 ResultSet resultSet1 = statement1.executeQuery()) {

                if (resultSet1.next()) {
                    deliveryPartnerId = resultSet1.getLong("user_id");
                }
            }

            if (deliveryPartnerId == null) {
                connection.rollback();
                return;
            }

            String sql2 = """
                    INSERT INTO order_assignment(order_id, delivery_partner_id)
                    VALUES (?, ?)
                    """;

            try (PreparedStatement statement2 = connection.prepareStatement(sql2)) {
                statement2.setLong(1, orderId);
                statement2.setLong(2, deliveryPartnerId);
                statement2.executeUpdate();
            }

            String sql3 = """
                    UPDATE delivery_partner
                    SET is_available = false
                    WHERE user_id = ?
                    """;

            try (PreparedStatement statement3 = connection.prepareStatement(sql3)) {
                statement3.setLong(1, deliveryPartnerId);
                statement3.executeUpdate();
            }

            String sql4 = """
                    UPDATE orders
                    SET status = 'OUT_FOR_DELIVERY'
                    WHERE order_id = ?
                    """;

            try (PreparedStatement statement4 = connection.prepareStatement(sql4)) {
                statement4.setLong(1, orderId);
                statement4.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {

            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isDeliveryPartnersEmpty() {

        try {

            String sql = "SELECT 1 FROM delivery_partner WHERE is_active=true";

            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                return !rs.next();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }
}