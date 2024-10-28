package com.ntq.training.infra.validator.parserimpl;

import com.ntq.training.dal.entity.Customer;
import com.ntq.training.infra.constants.LogMessages;
import com.ntq.training.infra.validator.CommonParserValidator;
import com.ntq.training.infra.validator.ParserValidator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerParserValidator implements ParserValidator<Customer> {
    public List<String> validate(Customer customer) {
        return Stream.of(
                        CommonParserValidator.isValidEmail(customer.getEmail()) ? "" : LogMessages.CUSTOMER_EMAIL_INVALID,
                        CommonParserValidator.isValidPhoneNumber(customer.getPhoneNumber()) ? "" : LogMessages.CUSTOMER_PHONE_INVALID
                )
                .filter(error -> !error.isEmpty())
                .collect(Collectors.toList());
    }
}
