package com.ntq.training.bl.impl;

import com.ntq.training.bl.OrderService;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.dto.OrderToAddDTO;
import com.ntq.training.dal.dto.OrderToDeleteDTO;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.datahandler.DataLineParser;
import com.ntq.training.dal.datahandler.DataLineWriter;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.validator.UniqueValidator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    public Map<Integer, Order> loadFile(String filePath) throws Exception {
        DataLoader<Order> dataLoader = new DataLoader<>();
        UniqueValidator<Order> uniqueValidator = new UniqueValidator<>();
        Map<Integer, Order> orderMap = dataLoader.loadData(filePath, DataLineParser.mapToOrder, true);
        Function<Order, String> uniquenessCustomerIdExtractor = Order::getId;
        orderMap = uniqueValidator.validate(orderMap, uniquenessCustomerIdExtractor, Order.class);
        return orderMap;
    }

    @Override
    public void saveFile(String filePath, Map<Integer, Order> orders) throws Exception {
        DataWriter<Order> dataWriter = new DataWriter<>();
        dataWriter.saveData(orders, filePath, DataLineWriter.getOrderHeaders, DataLineWriter.getOrderRowMapper());
    }

    @Override
    public Map<Integer, Order> insert(String filePath, Map<Integer, Order> orders, Map<Integer, OrderToAddDTO> newOrders) {
        int maxOrderId = orders.values().stream()
                .map(Order::getId)
                .filter(id -> id.startsWith("ORD"))
                .mapToInt(id -> Integer.parseInt(id.substring(3)))
                .max()
                .orElse(0);

        for (Map.Entry<Integer, OrderToAddDTO> entry : newOrders.entrySet()) {
            Integer lineIndex = entry.getKey();
            OrderToAddDTO orderToAddDTO = entry.getValue();
            Order newOrder = new Order();
            newOrder.setId("ORD" + (++maxOrderId));
            newOrder.setCustomerId(orderToAddDTO.getCustomerId());
            newOrder.setProductQuantities(orderToAddDTO.getProductQuantities());
            newOrder.setOrderDate(orderToAddDTO.getOrderDate());
            newOrder.setTotalAmount(orderToAddDTO.getTotalAmount());

            orders.put(lineIndex, newOrder);
        }
        return orders;
    }

    @Override
    public Map<Integer, Order> update(String filePath, Map<Integer, Order> orders, Map<Integer, Order> updateOrders) {
        for (Map.Entry<Integer, Order> entry : updateOrders.entrySet()) {
            Integer lineIndex = entry.getKey();
            Order updateOrder = entry.getValue();
            String orderId = updateOrder.getId();

            if (orders.values().stream().anyMatch(order -> order.getId().equals(orderId))) {
                orders.replace(lineIndex, updateOrder);
            } else {
                log.error("FUNCTION 4.3 - Order at line {} with orderId {} does not exist in current orders.", lineIndex, orderId);
            }
        }
        return orders;
    }

    @Override
    public Map<Integer, Order> delete(String filePath, Map<Integer, Order> orders, Map<Integer, OrderToDeleteDTO> deleteOrders) {
        for (Map.Entry<Integer, OrderToDeleteDTO> entry : deleteOrders.entrySet()) {
            Integer lineIndex = entry.getKey();
            OrderToDeleteDTO orderToDelete = entry.getValue();
            String orderId = orderToDelete.getId();

            Integer orderKey = orders.entrySet().stream()
                    .filter(e -> e.getValue().getId().equals(orderId))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

            if (orderKey != null) {
                orders.remove(orderKey);
            } else {
                log.error("FUNCTION 4.3 - Order at line {} with orderId {} does not exist in the current order list.", lineIndex, orderId);
            }
        }
        return orders;
    }

    @Override
    public Map<Integer, OrderToAddDTO> loadAddingFile(String filePath) throws Exception {
        DataLoader<OrderToAddDTO> dataLoader = new DataLoader<>();
        return dataLoader.loadData(filePath, DataLineParser.mapToAddingOrder, true);

    }

    @Override
    public Map<Integer, OrderToDeleteDTO> loadDeletingFile(String filePath) throws Exception {
        DataLoader<OrderToDeleteDTO> dataLoader = new DataLoader<>();
        return dataLoader.loadData(filePath, DataLineParser.mapToDeletingOrder, false);
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
