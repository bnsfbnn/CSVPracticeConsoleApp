package com.ntq.training.validator;

import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;

import java.util.Optional;

public class ValidatorFactory {
    @SuppressWarnings("unchecked")
    public static <T> Optional<Validator<T>> getValidator(Class<T> clazz) {
        if (clazz == Product.class) {
            return Optional.of((Validator<T>) new ProductValidator());
        }
        if (clazz == Customer.class) {
            return Optional.of((Validator<T>) new CustomerValidator());
        }
        if (clazz == Order.class) {
            return Optional.of((Validator<T>) new OrderValidator());
        }
        return Optional.empty();
    }
}
