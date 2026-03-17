package com.tss.repositories;

import com.tss.config.DBConnection;
import com.tss.model.users.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepoImpl implements CustomerRepo {

    private Connection connection;

    public CustomerRepoImpl() {
        connection = DBConnection.connect();
    }

    @Override
    public void addNewCustomer(Customer customer) {
        try {
            connection.setAutoCommit(false);

            String sql1 = "INSERT INTO users(name,user_type,phone,password) VALUES (?,?,?,?) RETURNING user_id";

            try (PreparedStatement ps1 = connection.prepareStatement(sql1)) {

                ps1.setString(1, customer.getName());
                ps1.setString(2, customer.getUserType().name());
                ps1.setLong(3, customer.getAccountInfo().getPhoneNumber());
                ps1.setString(4, customer.getAccountInfo().getPassword());

                try (ResultSet resultSet = ps1.executeQuery()) {

                    resultSet.next();
                    long id = resultSet.getLong("user_id");

                    String sql2 = "INSERT INTO customer(user_id,address) VALUES (?,?)";

                    try (PreparedStatement ps2 = connection.prepareStatement(sql2)) {

                        ps2.setLong(1, id);
                        ps2.setString(2, customer.getAddress());
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
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try {
            String sql = "select u.user_id,u.name,u.phone,address from customer c join users u using(user_id)";

            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    customers.add(
                            new Customer(
                                    resultSet.getLong("user_id"),
                                    resultSet.getString("name"),
                                    resultSet.getLong("phone"),
                                    resultSet.getString("address")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customers;
    }

    @Override
    public Customer getCustomerById(long id) {
        Customer customer = null;

        try {
            String sql = "select u.user_id,u.name,u.phone,address from customer c join users u using(user_id) WHERE u.user_id=?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setLong(1, id);

                try (ResultSet resultSet = ps.executeQuery()) {

                    if (resultSet.next()) {
                        customer = new Customer(
                                resultSet.getLong("user_id"),
                                resultSet.getString("name"),
                                resultSet.getLong("phone"),
                                resultSet.getString("address")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customer;
    }

    @Override
    public void updateAddress(long userId, String address) {

        try {
            String sql = "UPDATE customer SET address = ? WHERE user_id = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, address);
                ps.setLong(2, userId);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}