package com.ksy.impl.persistant;

import javax.annotation.PreDestroy;
import java.sql.*;

public class ConnectionProvider {
    private final Connection connection;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Driver class not found");
        }
    }

    public ConnectionProvider(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    Connection getConnection() {
        return connection;
    }

    @PreDestroy
    public void destruct() throws SQLException {
        connection.close();
    }
}