package com.ntq.training.bl.impl;

import com.ntq.training.bl.ProductService;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.dto.ProductToDeleteDTO;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.dal.datahandler.DataLineParser;
import com.ntq.training.dal.datahandler.DataLineWriter;
import com.ntq.training.infra.validator.UniqueValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class ProductServiceImpl implements ProductService {

    @Override
    public Map<Integer, Product> loadFile(String filePath) throws Exception {
        DataLoader<Product> dataLoader = new DataLoader<>();
        UniqueValidator<Product> uniqueValidator = new UniqueValidator<>();
        Map<Integer, Product> productMap = dataLoader.loadData(filePath, DataLineParser.mapToProduct, true);
        Function<Product, String> uniquenessProductIdExtractor = Product::getId;
        productMap = uniqueValidator.validate(productMap, uniquenessProductIdExtractor, Product.class);
        return productMap;
    }

    @Override
    public Map<Integer, ProductToDeleteDTO> loadDeletingFile(String filePath) throws Exception {
        DataLoader<ProductToDeleteDTO> dataLoader = new DataLoader<>();
        UniqueValidator<ProductToDeleteDTO> uniqueValidator = new UniqueValidator<>();
        Map<Integer, ProductToDeleteDTO> productMap = dataLoader.loadData(filePath, DataLineParser.mapToProductToDeleteDTO, true);
        Function<ProductToDeleteDTO, String> uniquenessProductIdExtractor = ProductToDeleteDTO::getId;
        productMap = uniqueValidator.validate(productMap, uniquenessProductIdExtractor, ProductToDeleteDTO.class);
        return productMap;
    }

    @Override
    public void saveFile(String filePath, Map<Integer, Product> products) throws Exception {
        DataWriter<Product> dataWriter = new DataWriter<>();
        dataWriter.saveData(products, filePath, DataLineWriter.getProductHeaders, DataLineWriter.getProductRowMapper());
    }

    @Override
    public Map<Integer, Product> insert(String filePath, Map<Integer, Product> products, Map<Integer, Product> newProducts) {
        int maxLine = products.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
        for (Product newProduct : newProducts.values()) {
            String newProductId = newProduct.getId();
            Optional<Map.Entry<Integer, Product>> existingProductEntry = findProductEntryById(products, newProductId);
            if (existingProductEntry.isPresent()) {
                log.error("FUNCTION 2.1 ERROR - Product with ID {} already exists and will not be added again.", newProductId);
            } else {
                products.put(++maxLine, newProduct);
            }
        }
        return products;
    }

    @Override
    public Map<Integer, Product> update(String filePath, Map<Integer, Product> products, Map<Integer, Product> updateProducts) {
        for (Product updateProduct : updateProducts.values()) {
            String updateProductId = updateProduct.getId();
            Optional<Map.Entry<Integer, Product>> existingProductEntry = findProductEntryById(products, updateProductId);
            if (existingProductEntry.isPresent()) {
                Product existingProduct = existingProductEntry.get().getValue();
                existingProduct.setName(updateProduct.getName());
                existingProduct.setPrice(updateProduct.getPrice());
                existingProduct.setStockQuantity(updateProduct.getStockQuantity());
            } else {
                log.error("FUNCTION 2.2 ERROR - Product with ID {} does not exist and cannot be updated.", updateProductId);
            }
        }
        return products;
    }

    @Override
    public Map<Integer, Product> delete(String filePath, Map<Integer, Product> products, Map<Integer, ProductToDeleteDTO> deleteProducts) {
        for (ProductToDeleteDTO deleteProduct : deleteProducts.values()) {
            String deleteProductId = deleteProduct.getId();
            Optional<Map.Entry<Integer, Product>> existingProductEntry = findProductEntryById(products, deleteProductId);
            if (existingProductEntry.isPresent()) {
                products.remove(existingProductEntry.get().getKey());
            } else {
                log.error("FUNCTION 2.3 ERROR - Product with ID {} does not exist and cannot be deleted.", deleteProductId);
            }
        }
        return products;
    }

    private Optional<Map.Entry<Integer, Product>> findProductEntryById(Map<Integer, Product> products, String productId) {
        return products.entrySet().stream()
                .filter(e -> e.getValue().getId().equals(productId))
                .findFirst();
    }
}
