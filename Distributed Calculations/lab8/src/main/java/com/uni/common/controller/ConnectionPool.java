package com.uni.common.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConnectionPool implements AutoCloseable {
    private final String url;
    private final String user;
    private final String password;

    private final static int INITIAL_POOL_SIZE = 10;
    private final List<Connection> connectionPool = new ArrayList<>(INITIAL_POOL_SIZE);
    private final List<Connection> usedConnections =
            Collections.synchronizedList(new ArrayList<>(INITIAL_POOL_SIZE));

    public ConnectionPool(String url, String user, String password) throws SQLException {
        this.url = url;
        this.user = user;
        this.password = password;

        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            connectionPool.add(DriverManager.getConnection(this.url, this.user, this.password));
        }
    }

    public synchronized Connection getConnection() {
        while (connectionPool.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    public synchronized boolean releaseConnection(Connection connection) throws SQLException {
        connection.close();
        connectionPool.add(connection);
        notify();
        return usedConnections.remove(connection);
    }

    @Override
    public void close() throws SQLException {
        SQLException exception = null;
        for (Connection connection : connectionPool) {
            try {
                connection.close();
            } catch (SQLException e) {
                exception = e;
            }
        }
        connectionPool.clear();
        if (exception != null) {
            throw exception;
        }
    }
}
