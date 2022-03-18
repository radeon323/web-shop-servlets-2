package com.luxoft.olshevchenko.webshop.web;

import com.luxoft.olshevchenko.webshop.dao.jdbc.JdbcProductDao;
import com.luxoft.olshevchenko.webshop.dao.jdbc.JdbcUserDao;
import com.luxoft.olshevchenko.webshop.service.ProductService;
import com.luxoft.olshevchenko.webshop.service.UserService;
import com.luxoft.olshevchenko.webshop.web.templater.PropertiesReader;
import org.postgresql.ds.PGSimpleDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Oleksandr Shevchenko
 */
public class ServiceLocator {
    private static final Map<Class<?>, Object> CONTEXT = new HashMap<>();

    static {
        Properties properties = PropertiesReader.getProperties();
        final String jdbcUrl = properties.getProperty("jdbc_url");
        final String jdbcName = properties.getProperty("jdbc_name");
        final String jdbcUser = properties.getProperty("jdbc_user");
        final String jdbcPassword = properties.getProperty("jdbc_password");

        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setDatabaseName(jdbcName);
        pgSimpleDataSource.setUser(jdbcUser);
        pgSimpleDataSource.setPassword(jdbcPassword);

        JdbcProductDao jdbcProductDao = new JdbcProductDao(pgSimpleDataSource);
        JdbcUserDao jdbcUserDao = new JdbcUserDao(pgSimpleDataSource);

        ProductService productService = new ProductService(jdbcProductDao);
        UserService userService = new UserService(jdbcUserDao);

        CONTEXT.put(ProductService.class, productService);
        CONTEXT.put(UserService.class, userService);
    }
}
