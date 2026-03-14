package com.tss.repositories;

import com.tss.config.DBConnection;
import com.tss.model.Discount;
import com.tss.model.PriceDiscount;
import com.tss.model.users.DeliveryPartner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiscountRepoImpl implements DiscountRepo {
    private Connection connection;

    public DiscountRepoImpl(){
        connection= DBConnection.connect();
    }

    @Override
    public void addNewPriceDiscount(Discount discount) {
        try{
            String sql="INSERT INTO price_discount(minimum_amount, discount_percentage) VALUES (?,?)";
            PreparedStatement statement= connection.prepareStatement(sql);

            statement.setDouble(1,discount.getMinimumAmount());
            statement.setDouble(2,discount.getDiscount());

            statement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Discount> getAllDiscounts() {
        List<Discount> discounts=new ArrayList<>();

        try {
            String sql = "select minimum_amount,discount_percentage FROM price_discount";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                discounts.add(
                        new PriceDiscount(
                                resultSet.getDouble("minimum_amount"),
                                resultSet.getDouble("discount_percentage")
                        )
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return discounts;
    }

    public Discount giveMaxPossibleDiscount(double amount) {

        String sql = """
        SELECT discount_id, minimum_amount, discount_percentage
        FROM price_discount
        WHERE minimum_amount <= ?
        ORDER BY discount_percentage DESC
        LIMIT 1
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, amount);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new PriceDiscount(
                            rs.getInt("discount_id"),
                            rs.getDouble("minimum_amount"),
                            rs.getDouble("discount_percentage")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }

        return null;
    }
}
