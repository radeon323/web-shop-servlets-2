package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.entity.Product;
import com.luxoft.olshevchenko.webshop.service.ProductService;
import com.luxoft.olshevchenko.webshop.web.templater.PageGenerator;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Optional<Product> optionalProduct = validateAndGetProduct(request, response);
            optionalProduct.ifPresent(product -> addProduct(product, response));
        } catch (Exception e) {
            writeErrorResponse(response);
        }
    }

    @SneakyThrows
    private void addProduct(Product product, HttpServletResponse response) {
        productService.add(product);
        String msgSuccess = "Product <i>" + product.getName() + "</i> was successfully added!";
        Map<String, Object> parameters = Map.of("msgSuccess", msgSuccess);
        String page = PageGenerator.getPage("add_product.html", parameters);
        response.getWriter().write(page);
    }

    @SneakyThrows
    private Optional<Product> validateAndGetProduct(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        if(name != null && name.length() > 0 && price != null && Double.parseDouble(price) > 0 ) {
            Product product = Product.builder()
                    .name(name)
                    .price(Double.parseDouble(price))
                    .creationDate(LocalDateTime.now().withNano(0).withSecond(0))
                    .build();
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
        String pageError = PageGenerator.getPage("add_product.html", parameters);
        response.getWriter().write(pageError);
    }


}
