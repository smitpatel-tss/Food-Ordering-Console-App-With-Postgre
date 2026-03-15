package com.tss.repositories;

import com.tss.config.DBConnection;
import com.tss.exceptions.UserNotFoundException;
import com.tss.model.Notification;
import com.tss.model.users.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepoImpl implements NotificationRepo {
    private Connection connection;

    public NotificationRepoImpl() {
        connection = DBConnection.connect();
    }

    @Override
    public void sendNotification(Notification notification) {
        try {
            String sql = "INSERT INTO notification(user_id,message,sender,receiver) VALUES(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, notification.getUserId());
            statement.setString(2, notification.getMessage());
            statement.setString(3, notification.getSender().name());
            statement.setString(4, notification.getReceiver().name());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Notification> getAllUnseenNotifications(long userId) {
        List<Notification> notifications = new ArrayList<>();
        try {
            String sql0 = "SELECT user_type, total_seen_notifications FROM users WHERE user_id=?";
            PreparedStatement statement0 = connection.prepareStatement(sql0);
            statement0.setLong(1, userId);

            ResultSet rs0 = statement0.executeQuery();

            int seenNotifications = 0;
            UserType userType = null;

            if (rs0.next()) {
                seenNotifications = rs0.getInt("total_seen_notifications");
                userType = UserType.valueOf(rs0.getString("user_type"));
            } else {
                throw new UserNotFoundException("User Not Exist!");
            }

            String sql = """
                    SELECT user_id, message, sender, receiver
                    FROM notification
                    WHERE
                        (user_id = ?)
                        OR (sender = 'ADMIN' AND receiver = ? AND user_id IS NULL)
                        OR (? = 'ADMIN' AND receiver = 'ADMIN')
                    ORDER BY created_at
                    OFFSET ?
                    """;

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, userId);
            statement.setString(2, userType.name());
            statement.setString(3, userType.name());
            statement.setInt(4, seenNotifications);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getLong("user_id"),
                        rs.getString("message"),
                        UserType.valueOf(rs.getString("sender")),
                        UserType.valueOf(rs.getString("receiver"))
                ));
            }

            int newSeen = seenNotifications + notifications.size();

            String updateSql = """
                    UPDATE users
                    SET total_seen_notifications = ?
                    WHERE user_id = ?
                    """;

            PreparedStatement updateStmt = connection.prepareStatement(updateSql);
            updateStmt.setInt(1, newSeen);
            updateStmt.setLong(2, userId);
            updateStmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return notifications;
    }

    @Override
    public List<Notification> getAllNotifications(long userId) {
        List<Notification> notifications = new ArrayList<>();

        try {
            String sql0 = "SELECT user_type FROM users WHERE user_id=?";
            PreparedStatement stmt0 = connection.prepareStatement(sql0);
            stmt0.setLong(1, userId);

            ResultSet rs0 = stmt0.executeQuery();

            UserType userType = null;
            if (rs0.next()) {
                userType = UserType.valueOf(rs0.getString("user_type"));
            } else {
                throw new UserNotFoundException("User Not Exist!");
            }

            String sql = """
                    SELECT user_id, message, sender, receiver
                    FROM notification
                    WHERE
                        (user_id = ?)
                        OR (sender = 'ADMIN' AND receiver = ? AND user_id IS NULL)
                        OR (? = 'ADMIN' AND receiver = 'ADMIN')
                    ORDER BY created_at
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, userId);
            stmt.setString(2, userType.name());
            stmt.setString(3, userType.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getLong("user_id"),
                        rs.getString("message"),
                        UserType.valueOf(rs.getString("sender")),
                        UserType.valueOf(rs.getString("receiver"))
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return notifications;
    }
}
