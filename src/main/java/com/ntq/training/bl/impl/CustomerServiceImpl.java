package com.ntq.training.bl.impl;

import com.ntq.training.bl.IBaseService;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.util.DataLineParserHelper;
import com.ntq.training.util.DataLineWriterHelper;
import com.ntq.training.validator.UniqueValidator;

import java.util.Map;
import java.util.function.Function;

public class CustomerServiceImpl implements IBaseService<Customer> {
    private final DataLoader<Customer> dataLoader = new DataLoader<>();
    private final DataWriter<Customer> dataWriter = new DataWriter<>();
    private final UniqueValidator<Customer> uniqueValidator = new UniqueValidator<>();


    @Override
    public Map<Integer, Customer> loadFile(String filePath) {
        Map<Integer, Customer> customerMap = dataLoader.loadData(filePath, DataLineParserHelper.mapToCustomer);
        Function<Customer, String> uniquenessExtractor = Customer::getId;
        Function<Customer, String> uniquenessExtractor2 = Customer::getEmail;
        Function<Customer, String> uniquenessExtractor3 = Customer::getPhoneNumber;
        customerMap = uniqueValidator.validate(customerMap, uniquenessExtractor, Customer.class);
        customerMap = uniqueValidator.validate(customerMap, uniquenessExtractor2, Customer.class);
        customerMap = uniqueValidator.validate(customerMap, uniquenessExtractor3, Customer.class);
        return customerMap;
    }

    @Override
    public void saveFile(String filePath, Map<Integer, Customer> entities) {
        dataWriter.saveData(entities, filePath, DataLineWriterHelper.getCustomerHeaders, DataLineWriterHelper.getCustomerRowMapper());
    }


    @Override
    public boolean insert(String filePath) {
        return false;
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