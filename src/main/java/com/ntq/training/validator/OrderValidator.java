package com.ntq.training.validator;

import com.ntq.training.constants.ValidationUniqueFields;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.constants.ValidationMessages;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderValidator implements Validator<Order> {
    @Override
    public List<String> validate(Order order, Map<String, Set<String>> existFields) {
        Set<String> existingOrderIds = existFields.getOrDefault(ValidationUniqueFields.ORDER_IDS.getFieldName(), new HashSet<>());
        return Stream.of(
                        CommonValidator.isNonEmpty(order.getId()) ? "" : ValidationMessages.ORDER_ID_EMPTY,
                        !isDuplicateId(order.getId(), existingOrderIds) ? "" : ValidationMessages.ORDER_ID_DUPLICATE,
                        CommonValidator.isNonEmpty(order.getCustomerId()) ? "" : ValidationMessages.ORDER_CUSTOMER_ID_EMPTY
                        //,
//                        CommonValidator.isNonEmpty(order.getProductQuantities()) ? "" : ValidationMessages.ORDER_PRODUCT_QUANTITIES_EMPTY,
//                        CommonValidator.isValidDate(order.getOrderDate()) ? "" : ValidationMessages.ORDER_DATE_INVALID
                )
                .filter(error -> !error.isEmpty())
                .collect(Collectors.toList());
    }

    private boolean isDuplicateId(String orderId, Set<String> existedIds) {
        if (existedIds.contains(orderId)) {
            return true;
        } else {
            existedIds.add(orderId);
            return false;
        }
    }
}