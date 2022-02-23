package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.entity.Product;
import com.luxoft.olshevchenko.webshop.service.ProductService;
import com.luxoft.olshevchenko.webshop.service.SecurityService;
import com.luxoft.olshevchenko.webshop.web.templater.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class ShowAllProductsServlet extends HttpServlet {
    private final ProductService productService;
    private final SecurityService securityService;

    public ShowAllProductsServlet(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();
        dataForProductsList(request, parameters);
        String page = PageGenerator.getPage("products_list.html", parameters);
        response.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> parameters = new HashMap<>();
        dataForProductsList(request, parameters);

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            productService.remove(id);

            String msgSuccess = "Product " + id + " was successfully deleted!";
            parameters.put("msgSuccess", msgSuccess);

            String page = PageGenerator.getPage("products_list.html", parameters);
            response.getWriter().write(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dataForProductsList(HttpServletRequest request, HashMap<String, Object> parameters) {
        List<Product> products = productService.findAll();
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String id = (String) session.getAttribute("userId");
        parameters.put("products", products);
        parameters.put("email", email);
        parameters.put("userId", id);
        parameters.put("login", Boolean.toString(securityService.isAuth(request)));
    }


}
