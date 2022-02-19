package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.entity.Product;
import com.luxoft.olshevchenko.webshop.service.ProductService;
import com.luxoft.olshevchenko.webshop.web.templater.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
public class AddProductServlet extends HttpServlet {
    private final ProductService productService;

    public AddProductServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String page = PageGenerator.getPage("add_product.html");
        response.getWriter().println(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String name = request.getParameter("name");
            if(name != null && name.length() > 0 && request.getParameter("price") != null) {
                Product product = getProduct(request);
                productService.add(product);

                String msgSuccess = "Product <i>" + name + "</i> was successfully added!";
                Map<String, Object> parameters = Map.of("msgSuccess", msgSuccess);
                String page = PageGenerator.getPage("add_product.html", parameters);
                response.getWriter().write(page);

            } else {
                String errorMsg = "Please fill up all fields!";
                Map<String, Object> parameters = Map.of("errorMsg", errorMsg);
                String pageError = PageGenerator.getPage("add_product.html", parameters);
                response.getWriter().write(pageError);
            }
        } catch (Exception e) {
            String errorMsg = "Please fill up all fields!";
            Map<String, Object> parameters = Map.of("errorMsg", errorMsg);
            String pageError = PageGenerator.getPage("add_product.html", parameters);
            response.getWriter().write(pageError);
        }
    }

    private Product getProduct(HttpServletRequest request) {
        Product product = Product.builder()
                .name(request.getParameter("name"))
                .price(Double.parseDouble(request.getParameter("price")))
                .creationDate(LocalDateTime.now().withNano(0).withSecond(0))
                .build();
        return product;
    }


}
