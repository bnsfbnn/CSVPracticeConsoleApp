package com.ntq.training.infra.validator;

import com.ntq.training.dal.dto.OrderToAddDTO;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class
OrderConsistencyChecker {

    public static Map<Integer, Order> checkOrderConsistency(
            Map<Integer, Order> orders,
            Map<Integer, Customer> customers,
            Map<Integer, Product> products) {
        Map<Integer, Order> validOrders = new LinkedHashMap<>();
        for (Map.Entry<Integer, Order> entry : orders.entrySet()) {
            Order order = entry.getValue();
            Integer lineIndex = entry.getKey();
            if (!isCustomerIdPresent(order.getCustomerId(), customers)) {
                log.error("CONSISTENCY VALIDATION ERROR: Order at line {} has invalid customerId: {}", lineIndex, order.getCustomerId());
                continue;
            }
            if (!areProductIdsPresent(order.getProductQuantities(), products)) {
                log.error("CONSISTENCY VALIDATION ERROR: Order at line {} has invalid productQuantities: {}", lineIndex, order.getProductQuantities().keySet());
                continue;
            }
            validOrders.put(lineIndex, order);
        }
        return validOrders;
    }

    public static Map<Integer, OrderToAddDTO> checkOrderToAddDTOConsistency(
            Map<Integer, OrderToAddDTO> orders,
            Map<Integer, Customer> customers,
            Map<Integer, Product> products) {
        Map<Integer, OrderToAddDTO> validOrders = new LinkedHashMap<>();
        for (Map.Entry<Integer, OrderToAddDTO> entry : orders.entrySet()) {
            OrderToAddDTO order = entry.getValue();
            Integer lineIndex = entry.getKey();
            if (!isCustomerIdPresent(order.getCustomerId(), customers)) {
                log.error("CONSISTENCY VALIDATION ERROR: Order at line {} has invalid customerId: {}", lineIndex, order.getCustomerId());
                continue;
            }
            if (!areProductIdsPresent(order.getProductQuantities(), products)) {
                log.error("CONSISTENCY VALIDATION ERROR: Order at line {} has invalid productQuantities: {}", lineIndex, String.join(",", order.getProductQuantities().keySet()));
                continue;
            }
            validOrders.put(lineIndex, order);
        }
        return validOrders;
    }

    private static boolean isCustomerIdPresent(String customerId, Map<Integer, Customer> customers) {
        return customers.values().stream().anyMatch(customer -> customer.getId().equals(customerId));
    }

    private static boolean areProductIdsPresent(Map<String, Integer> productQuantities, Map<Integer, Product> products) {
        Set<String> productIds = products.values().stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
        return productQuantities.keySet().stream().allMatch(productIds::contains);
    }
}
