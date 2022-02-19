package com.luxoft.olshevchenko.webshop.dao.jdbc;

import com.luxoft.olshevchenko.webshop.web.templater.PropertiesReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Oleksandr Shevchenko
 */
public class ConnectionFactory {

    public Connection getConnection() {
        Properties properties = PropertiesReader.getProperties();
        String jdbcURL = properties.getProperty("jdbc_url");
        String jdbcUser = properties.getProperty("jdbc_user");
        String jdbcPassword = properties.getProperty("jdbc_password");
        try {
            return DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
