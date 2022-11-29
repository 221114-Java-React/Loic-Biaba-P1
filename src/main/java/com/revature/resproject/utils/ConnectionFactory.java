package com.revature.resproject.utils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    /* The field for storing the singleton instance should be declared static */
    private static ConnectionFactory connectionFactory;

    /* load in the jdbc */
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* class to read properties file */
    private final Properties props = new Properties();

    /* The singleton's constructor should always be private to prevent direct construction calls with the `new` operator outside this class */
    private ConnectionFactory() {
        try {
            props.load(new FileReader("src/main/resources/db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* The static method that controls access to the singleton instance */
    public static ConnectionFactory getInstance() {
        /* Ensure that the instance hasn't yet been initialized */
        if (connectionFactory == null)
            connectionFactory = new ConnectionFactory();

        return connectionFactory;
    }

    public Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"));
        if (con == null) throw new RuntimeException("Could not establish connection with the database!");
        return con;
    }
}
