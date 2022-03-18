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
public class DeleteProductServlet extends HttpServlet {
    private final ProductService productService;
    private final SecurityService securityService;

    public DeleteProductServlet(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> parameters = new HashMap<>();

        List<Product> products = productService.findAll();
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String userId = (String) session.getAttribute("userId");
        parameters.put("products", products);
        parameters.put("email", email);
        parameters.put("userId", userId);
        parameters.put("login", Boolean.toString(securityService.isAuth(request)));

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


}
