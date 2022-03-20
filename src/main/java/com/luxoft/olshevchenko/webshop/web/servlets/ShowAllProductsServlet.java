package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.entity.Product;
import com.luxoft.olshevchenko.webshop.service.ProductService;
import com.luxoft.olshevchenko.webshop.service.SecurityService;
import com.luxoft.olshevchenko.webshop.web.ServiceLocator;
import com.luxoft.olshevchenko.webshop.web.templater.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class ShowAllProductsServlet extends HttpServlet {
//    private final ProductService productService;
//    private final SecurityService securityService;
//
//    public ShowAllProductsServlet(ProductService productService, SecurityService securityService) {
//        this.productService = productService;
//        this.securityService = securityService;
//    }

    private final ProductService productService = ServiceLocator.get(ProductService.class);
    private final SecurityService securityService = ServiceLocator.get(SecurityService.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();

        List<Product> products = productService.findAll();
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String id = (String) session.getAttribute("userId");
        parameters.put("products", products);
        parameters.put("email", email);
        parameters.put("userId", id);
        parameters.put("login", Boolean.toString(securityService.isAuth(request)));

        String page = PageGenerator.getPage("products_list.html", parameters);
        response.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        String email = (String) session.getAttribute("email");
        String userId = (String) session.getAttribute("userId");


        parameters.put("cart", cart);
        parameters.put("email", email);
        parameters.put("userId", userId);
        parameters.put("login", Boolean.toString(securityService.isAuth(request)));

        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productService.findById(id);
        cart.add(product);
        response.sendRedirect("products");

    }


}
