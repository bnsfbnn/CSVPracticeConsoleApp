package com.ntq.training.bl.impl;

import com.ntq.training.bl.CustomerService;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.dto.CustomerToDeleteDTO;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.datahandler.DataLineParser;
import com.ntq.training.dal.datahandler.DataLineWriter;
import com.ntq.training.infra.validator.UniqueValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class CustomerServiceImpl implements CustomerService {
    Map<String, Integer> phoneNumberToIdMap = new HashMap<>();

    @Override
    public Map<Integer, Customer> loadFile(String filePath) throws Exception {
        DataLoader<Customer> dataLoader = new DataLoader<>();
        UniqueValidator<Customer> uniqueValidator = new UniqueValidator<>();
        Map<Integer, Customer> customerMap = dataLoader.loadData(filePath, DataLineParser.mapToCustomer, true);
        Function<Customer, String> uniquenessCustomerPhoneNumberExtractor = Customer::getPhoneNumber;
        customerMap = uniqueValidator.validate(customerMap, uniquenessCustomerPhoneNumberExtractor, Customer.class);
        Function<Customer, String> uniquenessCustomerIdExtractor = Customer::getId;
        customerMap = uniqueValidator.validate(customerMap, uniquenessCustomerIdExtractor, Customer.class);
        Function<Customer, String> uniquenessCustomerNameExtractor = Customer::getEmail;
        customerMap = uniqueValidator.validate(customerMap, uniquenessCustomerNameExtractor, Customer.class);
        return customerMap;
    }

    @Override
    public Map<Integer, CustomerToDeleteDTO> loadDeletingFile(String filePath) throws Exception {
        DataLoader<CustomerToDeleteDTO> dataLoader = new DataLoader<>();
        UniqueValidator<CustomerToDeleteDTO> uniqueValidator = new UniqueValidator<>();
        Map<Integer, CustomerToDeleteDTO> customerMap = dataLoader.loadData(filePath, DataLineParser.mapToDeletingCustomer, true);
        Function<CustomerToDeleteDTO, String> uniquenessCustomerPhoneNumberExtractor = CustomerToDeleteDTO::getPhoneNumber;
        customerMap = uniqueValidator.validate(customerMap, uniquenessCustomerPhoneNumberExtractor, CustomerToDeleteDTO.class);
        return customerMap;
    }

    @Override
    public void saveFile(String filePath, Map<Integer, Customer> customers) throws Exception {
        DataWriter<Customer> dataWriter = new DataWriter<>();
        dataWriter.saveData(customers, filePath, DataLineWriter.getCustomerHeaders, DataLineWriter.getCustomerRowMapper());
    }

    @Override
    public Map<Integer, Customer> insert(String filePath, Map<Integer, Customer> customers, Map<Integer, Customer> newCustomers) {
        for (Map.Entry<Integer, Customer> entry : customers.entrySet()) {
            phoneNumberToIdMap.put(entry.getValue().getPhoneNumber(), entry.getKey());
        }
        for (Map.Entry<Integer, Customer> entry : newCustomers.entrySet()) {
            Integer line = entry.getKey();
            Customer newCustomer = entry.getValue();
            String newCustomerPhoneNumber = newCustomer.getPhoneNumber();
            if (phoneNumberToIdMap.containsKey(newCustomerPhoneNumber)) {
                boolean emailExists = customers.values().stream()
                        .anyMatch(customer -> customer.getEmail().equals(newCustomer.getEmail()));
                if (emailExists) {
                    log.error("FUNCTION 3.2 - Cannot update customer. Email {} already exists.", newCustomer.getEmail());
                } else {
                    Integer existingId = phoneNumberToIdMap.get(newCustomerPhoneNumber);
                    Customer existingCustomer = customers.get(existingId);
                    existingCustomer.setId(newCustomer.getId());
                    existingCustomer.setName(newCustomer.getName());
                    existingCustomer.setEmail(newCustomer.getEmail());
                }
            } else {
                customers.put(line, newCustomer);
                phoneNumberToIdMap.put(newCustomerPhoneNumber, line);
            }
        }
        return customers;
    }

    @Override
    public Map<Integer, Customer> update(String filePath, Map<Integer, Customer> customers, Map<Integer, Customer> updateCustomers) {
        for (Map.Entry<Integer, Customer> entry : customers.entrySet()) {
            phoneNumberToIdMap.put(entry.getValue().getPhoneNumber(), entry.getKey());
        }
        for (Map.Entry<Integer, Customer> entry : updateCustomers.entrySet()) {
            Integer line = entry.getKey();
            Customer updateCustomer = entry.getValue();
            String updateCustomerPhoneNumber = updateCustomer.getPhoneNumber();
            if (phoneNumberToIdMap.containsKey(updateCustomerPhoneNumber)) {
                boolean emailExists = customers.values().stream()
                        .anyMatch(customer -> customer.getEmail().equals(updateCustomer.getEmail()));
                if (emailExists) {
                    log.error("FUNCTION 3.3 - Cannot update customer. Email {} already exists.", updateCustomer.getEmail());
                } else {
                    Integer existingId = phoneNumberToIdMap.get(updateCustomerPhoneNumber);
                    Customer existingCustomer = customers.get(existingId);
                    existingCustomer.setId(updateCustomer.getId());
                    existingCustomer.setName(updateCustomer.getName());
                    existingCustomer.setEmail(updateCustomer.getEmail());
                }
            } else {
                log.error("FUNCTION 3.3 - Customer with phone number {} does not exist and cannot be updated.", updateCustomerPhoneNumber);
            }
        }
        return customers;
    }

    @Override
    public Map<Integer, Customer> delete(String filePath, Map<Integer, Customer> customers, Map<Integer, CustomerToDeleteDTO> deleteCustomers) {
        for (Map.Entry<Integer, CustomerToDeleteDTO> entry : deleteCustomers.entrySet()) {
            CustomerToDeleteDTO deleteCustomer = entry.getValue();
            String deleteCustomerId = deleteCustomer.getPhoneNumber();
            boolean customerExists = customers.values().stream()
                    .anyMatch(existingCustomer -> existingCustomer.getPhoneNumber().equals(deleteCustomerId));
            if (customerExists) {
                Optional<Map.Entry<Integer, Customer>> existingCustomerEntry = customers.entrySet().stream()
                        .filter(e -> e.getValue().getId().equals(deleteCustomerId))
                        .findFirst();
                existingCustomerEntry.ifPresent(entryToRemove -> {
                    customers.remove(entryToRemove.getKey());
                });
            } else {
                log.error("FUNCTION 3.1 - Customer with ID: {} does not exist and cannot be deleted.", deleteCustomerId);
            }
        }
        return customers;
    }
}