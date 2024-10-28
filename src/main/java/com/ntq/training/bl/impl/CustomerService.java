package com.ntq.training.bl.impl;

import com.ntq.training.bl.IBaseService;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.datahandler.DataLineParser;
import com.ntq.training.dal.datahandler.DataLineWriter;
import com.ntq.training.infra.validator.UniqueValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class CustomerService implements IBaseService<Customer> {
    private final DataLoader<Customer> dataLoader = new DataLoader<>();
    private final DataWriter<Customer> dataWriter = new DataWriter<>();
    private final UniqueValidator<Customer> uniqueValidator = new UniqueValidator<>();


    @Override
    public Map<Integer, Customer> loadFile(String filePath) {
        Map<Integer, Customer> customerMap = dataLoader.loadData(filePath, DataLineParser.mapToCustomer);
        Function<Customer, String> uniquenessCustomerIdExtractor = Customer::getId;
        Function<Customer, String> uniquenessCustomerNameExtractor = Customer::getEmail;
        Function<Customer, String> uniquenessCustomerPhoneNumberExtractor = Customer::getPhoneNumber;
        customerMap = uniqueValidator.validate(customerMap, uniquenessCustomerIdExtractor, Customer.class);
        customerMap = uniqueValidator.validate(customerMap, uniquenessCustomerNameExtractor, Customer.class);
        customerMap = uniqueValidator.validate(customerMap, uniquenessCustomerPhoneNumberExtractor, Customer.class);
        return customerMap;
    }

    @Override
    public void saveFile(String filePath, Map<Integer, Customer> customers) {
        dataWriter.saveData(customers, filePath, DataLineWriter.getCustomerHeaders, DataLineWriter.getCustomerRowMapper());
    }


    @Override
    public Map<Integer, Customer> insert(String filePath, Map<Integer, Customer> customers, Map<Integer, Customer> newCustomers) {
        Map<String, Integer> phoneNumberToIdMap = new HashMap<>();
        for (Map.Entry<Integer, Customer> entry : customers.entrySet()) {
            phoneNumberToIdMap.put(entry.getValue().getPhoneNumber(), entry.getKey());
        }
        for (Map.Entry<Integer, Customer> entry : newCustomers.entrySet()) {
            Integer line = entry.getKey();
            Customer newCustomer = entry.getValue();
            String newCustomerPhoneNumber = newCustomer.getPhoneNumber();

            if (phoneNumberToIdMap.containsKey(newCustomerPhoneNumber)) {
                Integer existingId = phoneNumberToIdMap.get(newCustomerPhoneNumber);
                Customer existingCustomer = customers.get(existingId);
                existingCustomer.setId(newCustomer.getId());
                existingCustomer.setName(newCustomer.getName());
                existingCustomer.setEmail(newCustomer.getEmail());
                log.info("FUNCTION 3.2 - Updated existing customer with phone number: {}", newCustomerPhoneNumber);
            } else {
                customers.put(line, newCustomer);
                phoneNumberToIdMap.put(newCustomerPhoneNumber, line);
                log.info("FUNCTION 3.2 - Added new customer with phone number: {}", newCustomerPhoneNumber);
            }
        }
        return customers;
    }

    @Override
    public boolean update(String filePath) {
        return false;
    }

    @Override
    public boolean delete(String filePath) {
        return false;
    }
}