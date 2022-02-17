package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.entity.Product;
import com.luxoft.olshevchenko.webshop.service.ProductService;
import com.luxoft.olshevchenko.webshop.web.templater.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class ShowAllProductsServlet extends HttpServlet {
    private final ProductService productService;
    private final PageGenerator instance = PageGenerator.instance();

    public ShowAllProductsServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> products = productService.findAll();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("products", products);

        String page = instance.getPage("products_list.html", parameters);
        response.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        List<Product> products = productService.findAll();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("products", products);

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Product product = productService.findById(id);

            productService.remove(id);

            String msgSuccess = "Product " + product.getName() + " was successfully deleted!";
            parameters.put("msgSuccess", msgSuccess);

            String page = instance.getPage("products_list.html", parameters);
            response.getWriter().write(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
