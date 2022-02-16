package com.luxoft.olshevchenko.webshop.web;

import com.luxoft.olshevchenko.webshop.dao.jdbc.JdbcProductDao;
import com.luxoft.olshevchenko.webshop.service.ProductService;
import com.luxoft.olshevchenko.webshop.web.servlets.AddProductServlet;
import com.luxoft.olshevchenko.webshop.web.servlets.EditProductServlet;
import com.luxoft.olshevchenko.webshop.web.servlets.ShowAllProductsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author Oleksandr Shevchenko
 */
public class Main {
    public static void main(String[] args) throws Exception {

        JdbcProductDao jdbcProductDao = new JdbcProductDao();

        ProductService productService = new ProductService(jdbcProductDao);

        ShowAllProductsServlet showAllProductsServlet = new ShowAllProductsServlet(productService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(showAllProductsServlet), "/products");
        context.addServlet(new ServletHolder(showAllProductsServlet), "/*");

        context.addServlet(new ServletHolder(new AddProductServlet(productService)), "/products/add");
        context.addServlet(new ServletHolder(new EditProductServlet(productService)), "/products/edit");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
    }
}