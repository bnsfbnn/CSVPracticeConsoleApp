package com.ntq.training.bl.impl;

import com.ntq.training.bl.OrderService;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.dto.OrderToAddDTO;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.datahandler.DataLineParser;
import com.ntq.training.dal.datahandler.DataLineWriter;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.validator.UniqueValidator;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

public class OrderServiceImpl implements OrderService {

    @Override
    public Map<Integer, Order> loadFile(String filePath) {
        DataLoader<Order> dataLoader = new DataLoader<>();
        UniqueValidator<Order> uniqueValidator = new UniqueValidator<>();
        Map<Integer, Order> orderMap = dataLoader.loadData(filePath, DataLineParser.mapToOrder);
        Function<Order, String> uniquenessCustomerIdExtractor = Order::getId;
        orderMap = uniqueValidator.validate(orderMap, uniquenessCustomerIdExtractor, Order.class);
        return orderMap;
    }

    @Override
    public void saveFile(String filePath, Map<Integer, Order> orders) {
        DataWriter<Order> dataWriter = new DataWriter<>();
        dataWriter.saveData(orders, filePath, DataLineWriter.getOrderHeaders, DataLineWriter.getOrderRowMapper());
    }

    @Override
    public Map<Integer, Order> insert(String filePath, Map<Integer, Order> orders, Map<Integer, OrderToAddDTO> newOrders) {
        return orders;
    }

    @Override
    public Map<Integer, Order> update(String filePath, Map<Integer, Order> entities, Map<Integer, Order> updateEntities) {
        return Map.of();
    }

    @Override
    public Map<Integer, OrderToAddDTO> loadAddingFile(String filePath) {
        DataLoader<OrderToAddDTO> dataLoader = new DataLoader<>();
        return dataLoader.loadData(filePath, DataLineParser.mapToAddingOrder);
    }

    @Override
    public Map<Integer, Order> delete(String filePath, Map<Integer, Order> entities, Map<Integer, Order> deleteEntities) {
        return Map.of();
    }

    @Override
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
