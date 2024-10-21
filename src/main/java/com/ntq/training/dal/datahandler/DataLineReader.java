package com.ntq.training.dal.datahandler;

import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.constants.FileReaderConstants;
import com.ntq.training.validator.ProductValidator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataLineReader {
    private static final ProductValidator productValidator = new ProductValidator();

    public static Function<List<String>, Product> mapToProduct = (line) -> {
        String productId = line.get(FileReaderConstants.ProductField.PRODUCT_ID.getIndex());
        String productName = line.get(FileReaderConstants.ProductField.PRODUCT_NAME.getIndex());
        BigDecimal productPrice = new BigDecimal(line.get(FileReaderConstants.ProductField.PRODUCT_PRICE.getIndex()));
        Integer productStockAvailable = Integer.parseInt(line.get(FileReaderConstants.ProductField.PRODUCT_STOCK_AVAILABLE.getIndex()));
        return Product.builder()
                .id(productId)
                .name(productName)
                .price(productPrice)
                .stockQuantity(productStockAvailable)
                .build();
    };

    public static Function<List<String>, Customer> mapToCustomer = (line) -> {
        String customerId = line.get(FileReaderConstants.CustomerField.CUSTOMER_ID.getIndex());
        String customerName = line.get(FileReaderConstants.CustomerField.CUSTOMER_NAME.getIndex());
        String customerEmail = line.get(FileReaderConstants.CustomerField.CUSTOMER_EMAIL.getIndex());
        String customerPhoneNumber = line.get(FileReaderConstants.CustomerField.CUSTOMER_PHONE_NUMBER.getIndex());
        return Customer.builder()
                .id(customerId)
                .name(customerName)
                .email(customerEmail)
                .phoneNumber(customerPhoneNumber)
                .build();
    };

    public static Function<List<String>, Order> mapToOrder = (line) -> {
        String orderId = line.get(FileReaderConstants.OrderField.ORDER_ID.getIndex());
        String orderCustomerId = line.get(FileReaderConstants.OrderField.ORDER_CUSTOMER_ID.getIndex());
        String raw_productQuantities = line.get(FileReaderConstants.OrderField.ORDER_PRODUCT_QUANTITIES.getIndex());
        String raw_orderDate = line.get(FileReaderConstants.OrderField.ORDER_ORDER_DATE.getIndex());
        Map<String, Integer> productQuantities = Arrays.stream(raw_productQuantities.split(FileReaderConstants.PRODUCT_QUANTITIES_SEPARATOR))
                .map(entry -> entry.split(FileReaderConstants.PRODUCT_QUANTITY_SEPARATOR))
                .collect(Collectors.toMap(
                        entry -> entry[0],
                        entry -> Integer.parseInt(entry[1])
                ));
        OffsetDateTime orderDate = OffsetDateTime.parse(raw_orderDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return Order.builder()
                .id(orderId)
                .customerId(orderCustomerId)
                .productQuantities(productQuantities)
                .orderDate(orderDate)
                .build();
    };
}
