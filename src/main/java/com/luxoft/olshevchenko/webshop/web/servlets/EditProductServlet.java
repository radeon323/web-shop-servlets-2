package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.entity.Product;
import com.luxoft.olshevchenko.webshop.service.ProductService;
import com.luxoft.olshevchenko.webshop.web.templater.PageGenerator;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
public class EditProductServlet extends HttpServlet {
    private final ProductService productService;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> parameters = new HashMap<>();
        try {
            Optional<Product> optionalProduct = validateAndGetProduct(request, response, parameters);
            optionalProduct.ifPresent(product -> editProduct(product, response, parameters));
        } catch (Exception e) {
            writeErrorResponse(response);
        }
    }

    @SneakyThrows
    private void editProduct(Product product, HttpServletResponse response, HashMap<String, Object> parameters) {
        productService.edit(product);
        String msgSuccess = "Product <i>" + product.getName() + "</i> was successfully changed!";
        parameters.put("msgSuccess", msgSuccess);
        String page = PageGenerator.getPage("edit_product.html", parameters);
        response.getWriter().write(page);
    }

    @SneakyThrows
    private Optional<Product> validateAndGetProduct(HttpServletRequest request, HttpServletResponse response, HashMap<String, Object> parameters) {
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        int id = Integer.parseInt(request.getParameter("id"));
        if(name != null && name.length() > 0 && price != null && Double.parseDouble(price) > 0 ) {
            Product product = Product.builder()
                    .id(id)
                    .name(name)
                    .price(Double.parseDouble(price))
                    .build();
            parameters.put("product", product);
            return Optional.of(product);
        } else {
            writeErrorResponse(response);
            return Optional.empty();
        }
    }

    @SneakyThrows
    private void writeErrorResponse(HttpServletResponse response) {
        String errorMsg = "Please fill up all fields!";
        Map<String, Object> parameters = Map.of("errorMsg", errorMsg);
        String page = PageGenerator.getPage("edit_product.html", parameters);
        response.getWriter().write(page);
    }


}
