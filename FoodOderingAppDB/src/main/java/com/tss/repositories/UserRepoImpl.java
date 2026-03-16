package com.tss.repositories;

import com.tss.config.DBConnection;
import com.tss.model.users.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepoImpl implements UserRepo {
    private Connection connection;

    public UserRepoImpl() {
        connection = DBConnection.connect();
    }

    @Override
    public boolean canAddNumber(long number, UserType type) {

        String table = "";

        switch (type) {
            case CUSTOMER -> table = "customers";
            case DELIVERY_PARTNER -> table = "delivery_partner";
            case ADMIN -> table = "admins";
        }

        String sql = "SELECT 1 FROM " + table + " WHERE phone=? LIMIT 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, number);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    @Override
    public boolean checkPassword(long number, String password, UserType type) {

        String table = "";

        switch (type) {
            case CUSTOMER -> table = "customer";
            case DELIVERY_PARTNER -> table = "delivery_partner";
            case ADMIN -> table = "admin";
        }

        String sql = "SELECT 1 FROM " + table + " WHERE phone=? AND password=? LIMIT 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, number);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public User getUserFromId(long id) {
        String sql = "SELECT * FROM users WHERE user_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                UserType type = UserType.valueOf(resultSet.getString("user_type"));

                User user = null;
                switch (type) {
                    case CUSTOMER -> {
                        String q = "SELECT user_id,name,phone,password,address FROM user JOIN customer USING(user_id) WHERE user_id=?;";
                        PreparedStatement statement1 = connection.prepareStatement(q);
                        statement1.setLong(1, id);
                        ResultSet result = statement1.executeQuery();
                        if(result.next()){
                            user = new Customer(
                                    result.getLong("user_id"),
                                    result.getString("name"),
                                    result.getLong("phone"),
                                    result.getString("password"),
                                    result.getString("address")
                            );
                        }

                    }
                    case DELIVERY_PARTNER -> {
                        String q = "SELECT user_id,name,phone,password,is_available,is_active FROM user JOIN delivery_partner USING(user_id) WHERE user_id=?;";
                        PreparedStatement statement1 = connection.prepareStatement(q);
                        statement1.setLong(1, id);
                        ResultSet result = statement1.executeQuery();
                        if(result.next()){
                            user = new DeliveryPartner(
                                    result.getLong("user_id"),
                                    result.getString("name"),
                                    result.getLong("phone"),
                                    result.getString("password"),
                                    result.getBoolean("is_available"),
                                    result.getBoolean("is_active")
                            );
                        }
                    }
                    case ADMIN -> {
                        String q = "SELECT user_id,name,phone,password FROM user WHERE user_id=?;";
                        PreparedStatement statement1 = connection.prepareStatement(q);
                        statement1.setLong(1, id);
                        ResultSet result = statement1.executeQuery();
                        if(result.next()){
                            user = new Admin(
                                    result.getLong("user_id"),
                                    result.getString("name"),
                                    result.getLong("phone"),
                                    result.getString("password")
                            );
                        }
                    }
                }

                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public User getUserFromNumber(long phone, UserType type) {

        String sql = "";

        switch (type) {
            case CUSTOMER ->
                    sql = "SELECT user_id,name,phone,password,address FROM users JOIN customer USING(user_id) WHERE phone=?";

            case DELIVERY_PARTNER ->
                    sql = "SELECT user_id,name,phone,password,is_available,is_active FROM users JOIN delivery_partner USING(user_id) WHERE phone=?";

            case ADMIN ->
                    sql = "SELECT user_id,name,phone,password FROM users JOIN admin USING(user_id) WHERE phone=?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, phone);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                switch (type) {

                    case CUSTOMER -> {
                        return new Customer(
                                rs.getLong("user_id"),
                                rs.getString("name"),
                                rs.getLong("phone"),
                                rs.getString("password"),
                                rs.getString("address")
                        );
                    }
                    case DELIVERY_PARTNER -> {
                        return new DeliveryPartner(
                                rs.getLong("user_id"),
                                rs.getString("name"),
                                rs.getLong("phone"),
                                rs.getString("password"),
                                rs.getBoolean("is_available"),
                                rs.getBoolean("is_active")
                        );
                    }
                    case ADMIN -> {
                        return new Admin(
                                rs.getLong("user_id"),
                                rs.getString("name"),
                                rs.getLong("phone"),
                                rs.getString("password")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public boolean changePassword(long userId, String newPassword, UserType type) {

        String sql = "UPDATE users SET password=? WHERE user_id=? AND user_type=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setLong(2, userId);
            ps.setString(3, type.name());

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public boolean changePhoneNumber(long userId, long newPhone, UserType type) {

        String sql = "UPDATE users SET phone=? WHERE user_id=? AND user_type=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, newPhone);
            ps.setLong(2, userId);
            ps.setString(3, type.name());

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public void addAdmin(Admin admin){
        try{
            String sql="INSERT INTO users(name,phone,password,user_type) VALUES (?,?,?,?) RETURNING user_id";
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setString(1,admin.getName());
            statement.setLong(2,admin.getAccountInfo().getPhoneNumber());
            statement.setString(3,admin.getAccountInfo().getPassword());
            statement.setString(4,admin.getUserType().name());

            ResultSet resultSet=statement.executeQuery();

            if(resultSet.next()){
                String sql1="INSERT INTO admin(user_id) VALUES (?)";
                PreparedStatement statement1=connection.prepareStatement(sql1);
                statement1.setLong(1,resultSet.getLong("user_id"));
                statement1.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
