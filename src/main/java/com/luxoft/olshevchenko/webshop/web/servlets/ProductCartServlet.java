package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.dto.ProductForCart;
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
import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
public class ProductCartServlet extends HttpServlet {
//    private final ProductService productService;
//    private final SecurityService securityService;
//
//    public ProductCartServlet(ProductService productService, SecurityService securityService) {
//        this.productService = productService;
//        this.securityService = securityService;
//    }

    private final SecurityService securityService = ServiceLocator.get(SecurityService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();
        dataForProductsList(request, parameters);
        String page = PageGenerator.getPage("cart.html", parameters);
        response.getWriter().write(page);
    }

    private void dataForProductsList(HttpServletRequest request, HashMap<String, Object> parameters) {

        HttpSession session = request.getSession();
        List<Product> products = (List<Product>) session.getAttribute("cart");
        products.sort(Comparator.comparingInt(Product::getId));

        Set<ProductForCart> productsForCart = new HashSet<>();
        for (int i = 0; i < products.size(); i++) {
            int quantity = Collections.frequency(products, products.get(i));
            productsForCart.add(dtoConvertToProductForCart(products.get(i), quantity));
        }

        String email = (String) session.getAttribute("email");
        String id = (String) session.getAttribute("userId");
        parameters.put("products", productsForCart);
        parameters.put("email", email);
        parameters.put("userId", id);
        parameters.put("login", Boolean.toString(securityService.isAuth(request)));
    }


    private ProductForCart dtoConvertToProductForCart(Product product, int quantity) {
        ProductForCart productForCart = new ProductForCart();
        productForCart.setId(product.getId());
        productForCart.setName(product.getName());
        productForCart.setPrice(product.getPrice());
        productForCart.setQuantity(quantity);
        return productForCart;
    }
}
