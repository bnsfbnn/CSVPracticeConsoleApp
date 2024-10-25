package com.ntq.training.util;

import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;

import java.util.List;
import java.util.function.Function;

public class DataLineWriterHelper {
    public static Function<Product, List<String>> getProductRowMapper() {
        return product -> List.of(
                String.valueOf(product.getId()),
                product.getName(),
                String.valueOf(product.getPrice()),
                String.valueOf(product.getStockQuantity())
        );
    }

    public static Function<Customer, List<String>> getCustomerRowMapper() {
        return customer -> List.of(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );
    }

    public static Function<Product, List<String>> getProductHeaders = (Void) -> List.of("id", "name", "price", "stock available");
    public static Function<Customer, List<String>> getCustomerHeaders = (Void) -> List.of("id", "name", "email", "phone number");
    public static Function<Order, List<String>> getOrderHeader = (Void) -> List.of("id", "customer id", "product quantities", "order date", "total amount");
}
