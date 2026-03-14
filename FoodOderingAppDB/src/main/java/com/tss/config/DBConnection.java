package com.tss.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {

    private static Connection connection;

    private DBConnection() {
    }

    public static Connection connect() {
        if (connection == null) {
            try {
                Properties properties = new Properties();
                InputStream input = DBConnection.class
                        .getClassLoader()
                        .getResourceAsStream("db.properties");

                properties.load(input);

                Class.forName(properties.getProperty("db.driver"));
                connection = DriverManager.getConnection(
                        properties.getProperty("db.url"),
                        properties.getProperty("db.username"),
                        properties.getProperty("db.password")
                );
                return connection;

            } catch (Exception e) {
                System.out.println("DATABASE DOWN!");
            }
        }
        return connection;
    }
}
