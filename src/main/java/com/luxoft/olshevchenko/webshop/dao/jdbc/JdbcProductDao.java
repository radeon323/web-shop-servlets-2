package com.luxoft.olshevchenko.webshop.dao.jdbc;

import com.luxoft.olshevchenko.webshop.dao.ProductDao;
import com.luxoft.olshevchenko.webshop.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class JdbcProductDao implements ProductDao {
    private static final ProductRowMapper PRODUCT_ROW_MAPPER = new ProductRowMapper();

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
    }

    @Override
    public List<Product> findAll() {
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, price, creation_date FROM products");
            ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Product> products = Collections.synchronizedList(new ArrayList<>());
            while(resultSet.next()) {
                Product product = PRODUCT_ROW_MAPPER.mapRow(resultSet);
                products.add(product);
            }
            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findById(int id) {
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, price, creation_date FROM products WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int pId = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    double price = resultSet.getDouble(3);
                    Timestamp creationDate = resultSet.getTimestamp(4);
                    Product product = Product.builder().
                            id(pId)
                            .name(name)
                            .price(price)
                            .creationDate(creationDate.toLocalDateTime())
                            .build();
                    return product;
                }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM products WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void edit(Product product) {
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE products SET name = ?, price = ? WHERE id = ?")) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Product product) {
        try (Connection connection = getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO products (name, price, creation_date) VALUES (?, ?, ?)");
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(product.getCreationDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
