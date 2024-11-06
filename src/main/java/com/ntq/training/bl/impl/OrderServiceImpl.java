package com.ntq.training.bl.impl;

import com.ntq.training.bl.OrderService;
import com.ntq.training.dal.datahandler.DataLineParser;
import com.ntq.training.dal.datahandler.DataLineWriter;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.dto.OrderToAddDTO;
import com.ntq.training.dal.dto.OrderToDeleteDTO;
import com.ntq.training.dal.dto.ProductOnlyIdDTO;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.validator.UniqueValidator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Map<Integer, Order> insert(String filePath, Map<Integer, Order> orders,
                                      Map<Integer, OrderToAddDTO> newOrders) {
        int maxOrderId = orders.values().stream()
                .map(Order::getId)
                .filter(id -> id.startsWith("ORD"))
                .mapToInt(id -> Integer.parseInt(id.substring(3)))
                .max()
                .orElse(0);
        int maxRow = orders.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
        for (OrderToAddDTO newOrder : newOrders.values()) {
            orders.put(++maxRow, Order.builder()
                    .id("ORD" + (++maxOrderId))
                    .customerId(newOrder.getCustomerId())
                    .productQuantities(newOrder.getProductQuantities())
                    .orderDate(newOrder.getOrderDate())
                    .totalAmount(newOrder.getTotalAmount())
                    .build());
        }
        return orders;
    }

    @Override
    public Map<Integer, Order> update(String filePath, Map<Integer, Order> orders, Map<Integer, Order> updateOrders) {
        for (Map.Entry<Integer, Order> entry : updateOrders.entrySet()) {
            Integer rowIndex = entry.getKey();
            Order updateOrder = entry.getValue();
            String updateOrderId = updateOrder.getId();
            if (orders.values().stream().anyMatch(order -> order.getId().equals(updateOrderId))) {
                orders.replace(rowIndex, updateOrder);
            } else {
                log.error("FUNCTION 4.3 ERROR - Order at line {} with orderId {} does not exist in current orders.",
                        rowIndex, updateOrderId);
            }
        }
        return orders;
    }

    @Override
    public Map<Integer, Order> delete(String filePath, Map<Integer, Order> orders,
                                      Map<Integer, OrderToDeleteDTO> deleteOrders) {
        for (Map.Entry<Integer, OrderToDeleteDTO> entry : deleteOrders.entrySet()) {
            Integer rowIndex = entry.getKey();
            OrderToDeleteDTO deleteOrder = entry.getValue();
            String deleteOrderId = deleteOrder.getId();
            Optional<Map.Entry<Integer, Order>> existingOrderEntry = findOrderEntryById(orders, deleteOrderId);
            if (existingOrderEntry.isPresent()) {
                orders.remove(existingOrderEntry.get().getKey());
            } else {
                log.error(
                        "FUNCTION 4.3 ERROR - Order at line {} with orderId {} in the orders.delete.csv does not exist in the current order list.",
                        rowIndex, deleteOrderId);
            }
        }
        return orders;
    }

    @Override
    public Map<Integer, OrderToAddDTO> loadAddingFile(String filePath) throws Exception {
        DataLoader<OrderToAddDTO> dataLoader = new DataLoader<>();
        return dataLoader.loadData(filePath, DataLineParser.mapToOrderToAddDTO, true);

    }

    @Override
    public Map<Integer, OrderToDeleteDTO> loadDeletingFile(String filePath) throws Exception {
        DataLoader<OrderToDeleteDTO> dataLoader = new DataLoader<>();
        return dataLoader.loadData(filePath, DataLineParser.mapToOrderToDeleteDTO, false);
    }

    @Override
    public void calculateTotalAmountForOrders(Map<Integer, Order> orders, Map<Integer, Product> products) {
        for (Order order : orders.values()) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Map.Entry<String, Integer> entry : order.getProductQuantities().entrySet()) {
                String productId = entry.getKey();
                Integer quantity = entry.getValue();
                Optional<Product> product = findProductById(products, productId);
                if (product.isPresent()) {
                    BigDecimal productTotal = product.get().getPrice().multiply(BigDecimal.valueOf(quantity));
                    totalAmount = totalAmount.add(productTotal);
                }
            }
            order.setTotalAmount(totalAmount);
        }
    }

    @Override
    public Map<Integer, Order> searchOrderByProductId(Map<Integer, Order> orders,
                                                      Map<Integer, ProductOnlyIdDTO> searchProductIds) {
        List<String> productIdsToSearch = searchProductIds.values().stream()
                .map(ProductOnlyIdDTO::getId)
                .toList();

        return orders.entrySet().stream()
                .filter(entry -> entry.getValue().getProductQuantities().keySet().stream()
                        .anyMatch(productIdsToSearch::contains))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Optional<Product> findProductById(Map<Integer, Product> products, String productId) {
        return products.values().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();
    }

    private Optional<Map.Entry<Integer, Order>> findOrderEntryById(Map<Integer, Order> orders, String orderId) {
        return orders.entrySet().stream()
                .filter(e -> e.getValue().getId().equals(orderId))
                .findFirst();
    }
}
