package com.ntq.training.validator;

import com.ntq.training.constants.ValidationUniqueFields;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.constants.ValidationMessages;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerValidator implements Validator<Customer> {
    @Override
    public List<String> validate(Customer customer, Map<String, Set<String>> existFields) {
        Set<String> existingCustomerIds = existFields.getOrDefault(ValidationUniqueFields.CUSTOMER_IDS.getFieldName(), new HashSet<>());
        Set<String> existingCustomerEmails = existFields.getOrDefault(ValidationUniqueFields.CUSTOMER_EMAILS.getFieldName(), new HashSet<>());
        Set<String> existingCustomerPhoneNumbers = existFields.getOrDefault(ValidationUniqueFields.CUSTOMER_PHONE_NUMBERS.getFieldName(), new HashSet<>());
        return Stream.of(
                        CommonValidator.isNonEmpty(customer.getId()) ? "" : ValidationMessages.CUSTOMER_ID_EMPTY,
                        !isDuplicateId(customer.getId(), existingCustomerIds) ? "" : ValidationMessages.CUSTOMER_ID_DUPLICATE,
                        CommonValidator.isNonEmpty(customer.getName()) ? "" : ValidationMessages.CUSTOMER_NAME_EMPTY,
                        CommonValidator.isValidEmail(customer.getEmail()) ? "" : ValidationMessages.CUSTOMER_EMAIL_INVALID,
                        !isDuplicateEmail(customer.getEmail(), existingCustomerEmails) ? "" : ValidationMessages.CUSTOMER_EMAIL_DUPLICATE,
                        CommonValidator.isValidPhoneNumber(customer.getPhoneNumber()) ? "" : ValidationMessages.CUSTOMER_PHONE_INVALID,
                        !isDuplicatePhoneNumber(customer.getPhoneNumber(), existingCustomerPhoneNumbers) ? "" : ValidationMessages.CUSTOMER_PHONE_DUPLICATE
                )
                .filter(error -> !error.isEmpty())
                .collect(Collectors.toList());
    }

    private boolean isDuplicateId(String customerId, Set<String> existedIds) {
        if (existedIds.contains(customerId)) {
            return true;
        } else {
            existedIds.add(customerId);
            return false;
        }
    }

    private boolean isDuplicateEmail(String customerEmail, Set<String> existedEmails) {
        if (existedEmails.contains(customerEmail)) {
            return true;
        } else {
            existedEmails.add(customerEmail);
            return false;
        }
    }

    private boolean isDuplicatePhoneNumber(String customerPhoneNumber, Set<String> existedPhoneNumbers) {
        if (existedPhoneNumbers.contains(customerPhoneNumber)) {
            return true;
        } else {
            existedPhoneNumbers.add(customerPhoneNumber);
            return false;
        }
    }
}
