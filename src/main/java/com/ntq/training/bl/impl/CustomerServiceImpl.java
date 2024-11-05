package com.ntq.training.bl.impl;

import com.ntq.training.bl.CustomerService;
import com.ntq.training.dal.datahandler.DataLineParser;
import com.ntq.training.dal.datahandler.DataLineWriter;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.dto.CustomerToDeleteDTO;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.infra.validator.UniqueValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class CustomerServiceImpl implements CustomerService {

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
        Map<Integer, CustomerToDeleteDTO> customerMap = dataLoader.loadData(filePath, DataLineParser.mapToCustomerToDeleteDTO, true);
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
        int maxLine = customers.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
        for (Customer newCustomer : newCustomers.values()) {
            String newCustomerPhoneNumber = newCustomer.getPhoneNumber();
            Optional<Map.Entry<Integer, Customer>> existingCustomerEntry = findCustomerEntryByPhoneNumber(customers, newCustomerPhoneNumber);
            if (existingCustomerEntry.isPresent()) {
                Customer existingCustomer = customers.get(existingCustomerEntry.get().getKey());
                existingCustomer.setId(newCustomer.getId());
                existingCustomer.setName(newCustomer.getName());
                existingCustomer.setEmail(newCustomer.getEmail());
            } else {
                customers.put(++maxLine, newCustomer);
            }
        }
        return customers;
    }

    @Override
    public Map<Integer, Customer> update(String filePath, Map<Integer, Customer> customers, Map<Integer, Customer> updateCustomers) {
        for (Customer updateCustomer : updateCustomers.values()) {
            String updateCustomerPhoneNumber = updateCustomer.getPhoneNumber();
            Optional<Map.Entry<Integer, Customer>> existingCustomerEntry = findCustomerEntryByPhoneNumber(customers, updateCustomerPhoneNumber);
            if (existingCustomerEntry.isPresent()) {
                Customer existingCustomer = customers.get(existingCustomerEntry.get().getKey());
                existingCustomer.setId(updateCustomer.getId());
                existingCustomer.setName(updateCustomer.getName());
                existingCustomer.setEmail(updateCustomer.getEmail());
            } else {
                log.error("FUNCTION 3.3 ERROR - Customer with phone number {} does not exist and cannot be updated.", updateCustomerPhoneNumber);
            }
        }
        return customers;
    }

    @Override
    public Map<Integer, Customer> delete(String filePath, Map<Integer, Customer> customers, Map<Integer, CustomerToDeleteDTO> deleteCustomers) {
        for (CustomerToDeleteDTO deleteCustomer : deleteCustomers.values()) {
            String deleteCustomerPhoneNumber = deleteCustomer.getPhoneNumber();
            Optional<Map.Entry<Integer, Customer>> existingCustomerEntry = findCustomerEntryByPhoneNumber(customers, deleteCustomerPhoneNumber);
            if (existingCustomerEntry.isPresent()) {
                customers.remove(existingCustomerEntry.get().getKey());
            } else {
                log.error("FUNCTION 3.1 ERROR - Customer with phone number {} does not exist and cannot be deleted.", deleteCustomerPhoneNumber);
            }
        }
        return customers;
    }

    private Optional<Map.Entry<Integer, Customer>> findCustomerEntryByPhoneNumber(Map<Integer, Customer> customers, String customerPhoneNumber) {
        return customers.entrySet().stream()
                .filter(entry -> entry.getValue().getPhoneNumber().equals(customerPhoneNumber))
                .findFirst();
    }
}