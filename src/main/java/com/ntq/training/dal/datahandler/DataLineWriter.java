package com.ntq.training.dal.datahandler;

import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataLineWriter {
    public static Function<Product, List<String>> getProductHeaders = (Void) -> List.of("Id", "Name", "Price", "StockAvailable");
    public static Function<Customer, List<String>> getCustomerHeaders = (Void) -> List.of("Id", "Name", "Email", "PhoneNumber");
    public static Function<Order, List<String>> getOrderHeaders = (Void) -> List.of("Id", "CustomerId", "ProductQuantities", "OrderDate", "TotalAmount");

    public static Function<Product, List<String>> getProductRowMapper() {
        return product -> List.of(
                String.valueOf(product.getId()),
                product.getName(),
                String.valueOf(product.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP)),
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

    public static Function<Order, List<String>> getOrderRowMapper() {
        return order -> List.of(
                order.getId(),
                order.getCustomerId(),
                order.getProductQuantities().entrySet().stream()
                        .map(entry -> entry.getKey() + FileConstants.PRODUCT_QUANTITY_SEPARATOR + entry.getValue())
                        .collect(Collectors.joining(FileConstants.PRODUCT_QUANTITIES_SEPARATOR)),
                order.getOrderDate().toString(),
                (order.getTotalAmount().equals(BigDecimal.ZERO)) ? "Not calculate" : String.valueOf(order.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP))
        );
    }
}
