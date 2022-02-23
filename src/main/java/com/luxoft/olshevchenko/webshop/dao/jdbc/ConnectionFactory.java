package com.luxoft.olshevchenko.webshop.dao.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Oleksandr Shevchenko
 */
public class ConnectionFactory implements DataSource {
    private final Properties properties;

    public ConnectionFactory(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    properties.getProperty("jdbc_url"),
                    properties.getProperty("jdbc_user"),
                    properties.getProperty("jdbc_password"));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can not connect to DB ", e);
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
