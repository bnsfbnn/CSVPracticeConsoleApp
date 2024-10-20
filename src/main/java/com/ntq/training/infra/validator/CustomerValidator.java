package com.ntq.training.infra.validator;

import com.ntq.training.dal.entity.Customer;
import com.ntq.training.infra.constants.ValidationMessages;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerValidator implements Validator<Customer> {
    @Override
    public List<String> validate(Customer customer) {
        return Stream.of(
                        CommonValidator.isNonEmpty(customer.getId()) ? "" : ValidationMessages.CUSTOMER_ID_EMPTY,
                        CommonValidator.isNonEmpty(customer.getName()) ? "" : ValidationMessages.CUSTOMER_NAME_EMPTY,
                        CommonValidator.isValidEmail(customer.getEmail()) ? "" : ValidationMessages.CUSTOMER_EMAIL_INVALID,
                        CommonValidator.isValidPhoneNumber(customer.getPhoneNumber()) ? "" : ValidationMessages.CUSTOMER_PHONE_INVALID
                )
                .filter(error -> !error.isEmpty())
                .collect(Collectors.toList());
    }
}
