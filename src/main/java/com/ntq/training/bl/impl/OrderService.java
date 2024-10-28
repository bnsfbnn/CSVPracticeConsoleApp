package com.ntq.training.bl.impl;

import com.ntq.training.bl.IBaseService;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.datahandler.DataLineParser;
import com.ntq.training.dal.datahandler.DataLineWriter;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.validator.UniqueValidator;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

public class OrderService implements IBaseService<Order> {
    private final DataLoader<Order> dataLoader = new DataLoader<>();
    private final DataWriter<Order> dataWriter = new DataWriter<>();
    private final UniqueValidator<Order> uniqueValidator = new UniqueValidator<>();

    @Override
    public Map<Integer, Order> loadFile(String filePath) {
        Map<Integer, Order> orderMap = dataLoader.loadData(filePath, DataLineParser.mapToOrder);
        Function<Order, String> uniquenessCustomerIdExtractor = Order::getId;
        orderMap = uniqueValidator.validate(orderMap, uniquenessCustomerIdExtractor, Order.class);
        return orderMap;
    }

    @Override
    public void saveFile(String filePath, Map<Integer, Order> orders) {
        dataWriter.saveData(orders, filePath, DataLineWriter.getOrderHeaders, DataLineWriter.getOrderRowMapper());
    }

    @Override
    public Map<Integer, Order> insert(String filePath, Map<Integer, Order> orders, Map<Integer, Order> newOrders) {

    }

    @Override
    public boolean update(String filePath) {
        return false;
    }

    @Override
    public boolean delete(String filePath) {
        return false;
    }

    public void calculateTotalAmountForOrders(Map<Integer, Order> orders, Map<Integer, Product> products) {
        for (Order order : orders.values()) {
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (Map.Entry<String, Integer> entry : order.getProductQuantities().entrySet()) {
                String productId = entry.getKey();
                Integer quantity = entry.getValue();

                Product product = findProductById(products, productId);
                if (product != null) {
                    BigDecimal productTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
                    totalAmount = totalAmount.add(productTotal);
                }
            }
            order.setTotalAmount(totalAmount);
        }
    }

    private Product findProductById(Map<Integer, Product> products, String productId) {
        return products.values().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElse(null);
    }
}
