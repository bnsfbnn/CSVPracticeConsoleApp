package com.ntq.training.bl.impl;

import com.ntq.training.bl.IBaseService;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.dal.datahandler.DataLineParser;
import com.ntq.training.dal.datahandler.DataLineWriter;
import com.ntq.training.infra.validator.UniqueValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Function;

@Slf4j
public class ProductService implements IBaseService<Product> {
    private final DataLoader<Product> dataLoader = new DataLoader<>();
    private final DataWriter<Product> dataWriter = new DataWriter<>();
    private final UniqueValidator<Product> uniqueValidator = new UniqueValidator<>();


    @Override
    public Map<Integer, Product> loadFile(String filePath) {
        Map<Integer, Product> productMap = dataLoader.loadData(filePath, DataLineParser.mapToProduct);
        Function<Product, String> uniquenessProductIdExtractor = Product::getId;
        productMap = uniqueValidator.validate(productMap, uniquenessProductIdExtractor, Product.class);
        return productMap;
    }

    @Override
    public void saveFile(String filePath, Map<Integer, Product> products) {
        dataWriter.saveData(products, filePath, DataLineWriter.getProductHeaders, DataLineWriter.getProductRowMapper());
    }

    @Override
    public Map<Integer, Product> insert(String filePath, Map<Integer, Product> products, Map<Integer, Product> newProducts) {
        for (Map.Entry<Integer, Product> entry : newProducts.entrySet()) {
            Integer line = entry.getKey();
            Product newProduct = entry.getValue();
            String newProductId = newProduct.getId();
            boolean productExists = products.values().stream()
                    .anyMatch(existingProduct -> existingProduct.getId().equals(newProductId));
            if (!productExists) {
                products.put(line, newProduct);
                log.info("FUNCTION 2.1 - Added new product with ID: {}", newProductId);
            } else {
                log.warn("FUNCTION 2.1 - Product with ID: {} already exists and will not be added again.", newProductId);
            }
        }
        return products;
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
