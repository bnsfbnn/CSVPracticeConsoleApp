package com.ntq.training.util;

import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.constants.FileConstants;
import com.ntq.training.validator.parserimpl.CustomerParserValidator;
import com.ntq.training.validator.ParserValidator;
import com.ntq.training.validator.parserimpl.ProductParserValidator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
public class DataLineParserHelper {

    public static BiFunction<Integer, List<String>, Optional<Product>> mapToProduct = (rowIndex, line) -> {
        String productId = line.get(FileConstants.ProductField.PRODUCT_ID.getIndex());
        String productName = line.get(FileConstants.ProductField.PRODUCT_NAME.getIndex());
        BigDecimal productPrice;
        try {
            productPrice = new BigDecimal(line.get(FileConstants.ProductField.PRODUCT_PRICE.getIndex()));
        } catch (NumberFormatException e) {
            log.error("PARSER VALIDATION ERROR: Row {} in product file has an invalid format for product price.", rowIndex);
            return Optional.empty();
        }
        Integer productStockAvailable;
        try {
            productStockAvailable = Integer.parseInt(line.get(FileConstants.ProductField.PRODUCT_STOCK_AVAILABLE.getIndex()));
        } catch (NumberFormatException e) {
            log.error("PARSER VALIDATION ERROR: Row {} in product file has an invalid format for product stock available.", rowIndex);
            return Optional.empty();
        }
        Product rawProduct = Product.builder()
                .id(productId)
                .name(productName)
                .price(productPrice)
                .stockQuantity(productStockAvailable)
                .build();
        ParserValidator<Product> productParserValidator = new ProductParserValidator();
        List<String> validationErrors = productParserValidator.validate(rawProduct);
        if (!validationErrors.isEmpty()) {
            validationErrors.forEach(error -> {
                log.error("PARSER VALIDATION ERROR: Row {} in product file has {}", rowIndex, error);
            });
            return Optional.empty();
        }
        return Optional.of(rawProduct);
    };

    public static BiFunction<Integer, List<String>, Optional<Customer>> mapToCustomer = (rowIndex, line) -> {
        String customerId = line.get(FileConstants.CustomerField.CUSTOMER_ID.getIndex());
        String customerName = line.get(FileConstants.CustomerField.CUSTOMER_NAME.getIndex());
        String customerEmail = line.get(FileConstants.CustomerField.CUSTOMER_EMAIL.getIndex());
        String customerPhoneNumber = line.get(FileConstants.CustomerField.CUSTOMER_PHONE_NUMBER.getIndex());
        Customer rawCustomer = Customer.builder()
                .id(customerId)
                .name(customerName)
                .email(customerEmail)
                .phoneNumber(customerPhoneNumber)
                .build();
        ParserValidator<Customer> customerParserValidator = new CustomerParserValidator();
        List<String> validationErrors = customerParserValidator.validate(rawCustomer);
        if (!validationErrors.isEmpty()) {
            validationErrors.forEach(error -> {
                log.error("CUSTOMER VALIDATION ERROR: Row {} in customer file has {}", rowIndex, error);
            });
            return Optional.empty();
        }
        return Optional.of(rawCustomer);

    };

    public static BiFunction<Integer, List<String>, Optional<Order>> mapToOrder = (rowIndex, line) -> {
        String orderId = line.get(FileConstants.OrderField.ORDER_ID.getIndex());
        String orderCustomerId = line.get(FileConstants.OrderField.ORDER_CUSTOMER_ID.getIndex());
        String rawProductQuantities = line.get(FileConstants.OrderField.ORDER_PRODUCT_QUANTITIES.getIndex());
        String raw_orderDate = line.get(FileConstants.OrderField.ORDER_ORDER_DATE.getIndex());
        Map<String, Integer> productQuantities;
        try {
            productQuantities = Arrays.stream(rawProductQuantities.split(FileConstants.PRODUCT_QUANTITIES_SEPARATOR))
                    .map(entry -> entry.split(FileConstants.PRODUCT_QUANTITY_SEPARATOR))
                    .collect(Collectors.toMap(
                            entry -> entry[0],
                            entry -> {
                                try {
                                    int quantity = Integer.parseInt(entry[1]);
                                    if (quantity <= 0) {
                                        log.error("ORDER VALIDATION ERROR: Row {} in order file has product quantity is a negative number.", rowIndex);
                                        return null;
                                    }
                                    return quantity;
                                } catch (NumberFormatException e) {
                                    log.error("ORDER VALIDATION ERROR: Row {} in order file has product quantity not a number.", rowIndex);
                                    return null;
                                }
                            }
                    ));
        } catch (Exception e) {
            log.error("ORDER VALIDATION ERROR: Row {} in order file cannot parsing product quantities.", rowIndex);
            return Optional.empty();
        }
        productQuantities = productQuantities.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        OffsetDateTime orderDate;
        try {
            orderDate = OffsetDateTime.parse(raw_orderDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (DateTimeParseException e) {
            log.error("ORDER VALIDATION ERROR: Row {} in order file is invalid date format for order date.", rowIndex);
            return Optional.empty();
        }
        return Optional.of(Order.builder()
                .id(orderId)
                .customerId(orderCustomerId)
                .productQuantities(productQuantities)
                .orderDate(orderDate)
                .build());
    };
}
