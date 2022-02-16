package com.luxoft.olshevchenko.webshop.service;

import com.luxoft.olshevchenko.webshop.dao.ProductDao;
import com.luxoft.olshevchenko.webshop.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class ProductService {

    private ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Product findById(int id) {
        return productDao.findById(id);
    }

    public void add(Product product) {
        product.setCreationDate(LocalDateTime.now());
        productDao.add(product);
    }

    public void remove(int id) {
        productDao.remove(id);
    }

    public void edit(Product product) {
        productDao.edit(product);
    }

}
