package com.ntq.training.bl.impl;

import com.ntq.training.bl.IBaseService;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.util.DataLineParserHelper;
import com.ntq.training.util.DataLineWriterHelper;
import com.ntq.training.validator.UniqueValidator;

import java.util.Map;
import java.util.function.Function;

public class ProductServiceImpl implements IBaseService<Product> {
    private final DataLoader<Product> dataLoader = new DataLoader<>();
    private final DataWriter<Product> dataWriter = new DataWriter<>();
    private final UniqueValidator<Product> uniqueValidator = new UniqueValidator<>();


    @Override
    public Map<Integer, Product> loadFile(String filePath) {
        Map<Integer, Product> productMap = dataLoader.loadData(filePath, DataLineParserHelper.mapToProduct);
        Function<Product, String> uniquenessExtractor = Product::getId;
        productMap = uniqueValidator.validate(productMap, uniquenessExtractor, Product.class);
        return productMap;
    }

    @Override
    public void saveFile(String filePath, Map<Integer, Product> entities) {
        dataWriter.saveData(entities, filePath, DataLineWriterHelper.getProductHeaders, DataLineWriterHelper.getProductRowMapper());
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
