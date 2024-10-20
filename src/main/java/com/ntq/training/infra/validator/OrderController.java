package com.ntq.training.infra.validator;

import com.ntq.training.dal.entity.Order;
import com.ntq.training.infra.constants.ValidationMessages;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderController implements Validator<Order> {
    @Override
    public List<String> validate(Order order) {
        return Stream.of(
                        CommonValidator.isNonEmpty(order.getId()) ? "" : ValidationMessages.ORDER_ID_EMPTY,
                        CommonValidator.isNonEmpty(order.getCustomerId()) ? "" : ValidationMessages.ORDER_CUSTOMER_ID_EMPTY,
                        CommonValidator.isNonEmpty(order.getProductQuantities()) ? "" : ValidationMessages.ORDER_PRODUCT_QUANTITIES_EMPTY,
                        CommonValidator.isValidDate(order.getOrderDate()) ? "" : ValidationMessages.ORDER_DATE_INVALID
                )
                .filter(error -> !error.isEmpty())
                .collect(Collectors.toList());
    }
}