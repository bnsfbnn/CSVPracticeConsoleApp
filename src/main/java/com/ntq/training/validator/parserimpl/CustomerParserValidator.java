package com.ntq.training.validator.parserimpl;

import com.ntq.training.dal.entity.Customer;
import com.ntq.training.constants.ValidationMessages;
import com.ntq.training.validator.CommonParserValidator;
import com.ntq.training.validator.ParserValidator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerParserValidator implements ParserValidator<Customer> {
    public List<String> validate(Customer customer) {
        return Stream.of(
                        CommonParserValidator.isValidEmail(customer.getEmail()) ? "" : ValidationMessages.CUSTOMER_EMAIL_INVALID,
                        CommonParserValidator.isValidPhoneNumber(customer.getPhoneNumber()) ? "" : ValidationMessages.CUSTOMER_PHONE_INVALID
                )
                .filter(error -> !error.isEmpty())
                .collect(Collectors.toList());
    }
}
