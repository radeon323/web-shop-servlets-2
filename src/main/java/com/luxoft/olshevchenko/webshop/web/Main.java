package com.luxoft.olshevchenko.webshop.web;

import com.luxoft.olshevchenko.webshop.dao.jdbc.JdbcProductDao;
import com.luxoft.olshevchenko.webshop.dao.jdbc.JdbcUserDao;
import com.luxoft.olshevchenko.webshop.service.ProductService;
import com.luxoft.olshevchenko.webshop.service.SecurityService;
import com.luxoft.olshevchenko.webshop.service.UserService;
import com.luxoft.olshevchenko.webshop.web.filter.SecurityFilter;
import com.luxoft.olshevchenko.webshop.web.servlets.*;
import com.luxoft.olshevchenko.webshop.web.templater.PropertiesReader;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.servlet.DispatcherType;
import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
public class Main {
    public static void main(String[] args) throws Exception {

        Properties properties = PropertiesReader.getProperties();
        final String jdbcUrl = properties.getProperty("jdbc_url");
        final String jdbcName = properties.getProperty("jdbc_name");
        final String jdbcUser = properties.getProperty("jdbc_user");
        final String jdbcPassword = properties.getProperty("jdbc_password");

        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setDatabaseName(jdbcName);
        pgSimpleDataSource.setUser(jdbcUser);
        pgSimpleDataSource.setPassword(jdbcPassword);

        Flyway flyway = Flyway.configure().dataSource(jdbcUrl, jdbcUser, jdbcPassword)
                .load();
        flyway.migrate();

        JdbcProductDao jdbcProductDao = new JdbcProductDao(pgSimpleDataSource);
        JdbcUserDao jdbcUserDao = new JdbcUserDao(pgSimpleDataSource);

        List<String> userTokens = Collections.synchronizedList(new ArrayList<>());

        ProductService productService = new ProductService(jdbcProductDao);
        UserService userService = new UserService(jdbcUserDao);
        SecurityService securityService = new SecurityService(userTokens);

        ShowAllProductsServlet showAllProductsServlet = new ShowAllProductsServlet(productService, securityService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(showAllProductsServlet), "/products");

        context.addServlet(new ServletHolder(new AddProductServlet(productService)), "/products/add");
        context.addServlet(new ServletHolder(new EditProductServlet(productService)), "/products/edit");
        context.addServlet(new ServletHolder(new DeleteProductServlet(productService, securityService)), "/products/delete/*");
        context.addServlet(new ServletHolder(new ProductCartServlet(productService, securityService)), "/products/cart");
        context.addServlet(new ServletHolder(new LoginServlet(userService, userTokens)), "/login");
        context.addServlet(new ServletHolder(new LogoutServlet()), "/logout");
        context.addServlet(new ServletHolder(new RegisterServlet(userService)), "/register");

        context.addFilter(new FilterHolder(new SecurityFilter(securityService)), "/*", EnumSet.of(DispatcherType.REQUEST));

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
    }
}