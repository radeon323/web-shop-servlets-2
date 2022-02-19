package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.entity.Product;
import com.luxoft.olshevchenko.webshop.service.ProductService;
import com.luxoft.olshevchenko.webshop.web.templater.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Oleksandr Shevchenko
 */
public class EditProductServlet extends HttpServlet {
    private ProductService productService;

    public EditProductServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productService.findById(id);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("product", product);

        String page = PageGenerator.getPage("edit_product.html", parameters);
        response.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Product product = getProduct(request);
            product.setId(id);
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("product", product);

            String name = request.getParameter("name");
            if(name != null && name.length() > 0 && request.getParameter("price") != null) {
                productService.edit(product);

                String msgSuccess = "Product <i>" + name + "</i> was successfully changed!";
                parameters.put("msgSuccess", msgSuccess);
                String page = PageGenerator.getPage("edit_product.html", parameters);
                response.getWriter().write(page);

            } else {
                String errorMsg = "Please fill up all fields";
                parameters.put("errorMsg", errorMsg);
                String page = PageGenerator.getPage("edit_product.html", parameters);
                response.getWriter().write(page);
            }
        } catch (Exception e) {
            String errorMsg = "Please fill up all fields";
            response.getWriter().println(errorMsg);
        }
    }

    private Product getProduct(HttpServletRequest request) {
        Product product = Product.builder()
                .name(request.getParameter("name"))
                .price(Double.parseDouble(request.getParameter("price")))
                .build();
        return product;
    }


}
