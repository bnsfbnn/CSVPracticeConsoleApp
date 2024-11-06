package com.ntq.training.bl.impl;

import com.ntq.training.bl.ProductService;
import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataWriter;
import com.ntq.training.dal.dto.ProductOnlyIdDTO;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.dal.datahandler.DataLineParser;
import com.ntq.training.dal.datahandler.DataLineWriter;
import com.ntq.training.infra.validator.UniqueValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Map<Integer, ProductOnlyIdDTO> loadOnlyIdFieldFile(String filePath) throws Exception {
        DataLoader<ProductOnlyIdDTO> dataLoader = new DataLoader<>();
        UniqueValidator<ProductOnlyIdDTO> uniqueValidator = new UniqueValidator<>();
        Map<Integer, ProductOnlyIdDTO> productMap = dataLoader.loadData(filePath,
                DataLineParser.mapToProductOnlyIdDTO, true);
        Function<ProductOnlyIdDTO, String> uniquenessProductIdExtractor = ProductOnlyIdDTO::getId;
        productMap = uniqueValidator.validate(productMap, uniquenessProductIdExtractor, ProductOnlyIdDTO.class);
        return productMap;
    }

    @Override
    public Map<Integer, Product> findTop3ProductHasMostOrder(Map<Integer, Product> products,
                                                             Map<Integer, Order> orders) {
        Map<String, Integer> productIdOrderCount = orders.values().stream()
                .flatMap(order -> order.getProductQuantities().entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum));
        List<String> top3ProductIds = productIdOrderCount.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
        Map<Integer, Product> top3ProductsMap = new HashMap<>();
        for (String productId : top3ProductIds) {
            products.values().stream()
                    .filter(product -> product.getId().equals(productId))
                    .findFirst()
                    .ifPresent(product -> top3ProductsMap.put(product.getId().hashCode(), product));
        }
        return top3ProductsMap;
    }

    @Override
    public void saveFile(String filePath, Map<Integer, Product> products) throws Exception {
        DataWriter<Product> dataWriter = new DataWriter<>();
        dataWriter.saveData(products, filePath, DataLineWriter.getProductHeaders, DataLineWriter.getProductRowMapper());
    }

    @Override
    public Map<Integer, Product> insert(String filePath, Map<Integer, Product> products,
                                        Map<Integer, Product> newProducts) {
        int maxRow = products.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
        for (Map.Entry<Integer, Product> productEntry : newProducts.entrySet()) {
            Integer rowIndex = productEntry.getKey();
            Product newProduct = productEntry.getValue();
            String newProductId = newProduct.getId();
            Optional<Map.Entry<Integer, Product>> existingProductEntry = findProductEntryById(products, newProductId);
            if (existingProductEntry.isPresent()) {
                log.error(
                        "FUNCTION 2.1 ERROR - Product at row {} with ID {} in the products.new.csv file already exists and will not be added again.",
                        rowIndex, newProductId);
            } else {
                products.put(++maxRow, newProduct);
            }
        }
        return products;
    }

    @Override
    public Map<Integer, Product> update(String filePath, Map<Integer, Product> products,
                                        Map<Integer, Product> updateProducts) {
        for (Map.Entry<Integer, Product> productEntry : updateProducts.entrySet()) {
            Integer rowIndex = productEntry.getKey();
            Product updateProduct = productEntry.getValue();
            String updateProductId = updateProduct.getId();
            Optional<Map.Entry<Integer, Product>> existingProductEntry = findProductEntryById(products,
                    updateProductId);
            if (existingProductEntry.isPresent()) {
                Product existingProduct = existingProductEntry.get().getValue();
                existingProduct.setName(updateProduct.getName());
                existingProduct.setPrice(updateProduct.getPrice());
                existingProduct.setStockQuantity(updateProduct.getStockQuantity());
            } else {
                log.error(
                        "FUNCTION 2.2 ERROR - Product at row {} with ID {} in the products.edit.csv file does not exist and cannot be updated.",
                        rowIndex, updateProductId);
            }
        }
        return products;
    }

    @Override
    public Map<Integer, Product> delete(String filePath, Map<Integer, Product> products,
                                        Map<Integer, ProductOnlyIdDTO> deleteProducts) {
        for (Map.Entry<Integer, ProductOnlyIdDTO> productEntry : deleteProducts.entrySet()) {
            Integer rowIndex = productEntry.getKey();
            ProductOnlyIdDTO deleteProduct = productEntry.getValue();
            String deleteProductId = deleteProduct.getId();
            Optional<Map.Entry<Integer, Product>> existingProductEntry = findProductEntryById(products,
                    deleteProductId);
            if (existingProductEntry.isPresent()) {
                products.remove(existingProductEntry.get().getKey());
            } else {
                log.error(
                        "FUNCTION 2.3 ERROR - Product at row {} with ID {} in the products.delete.csv file does not exist and cannot be deleted.",
                        rowIndex, deleteProductId);
            }
        }
        return products;
    }

    private Optional<Map.Entry<Integer, Product>> findProductEntryById(Map<Integer, Product> products,
                                                                       String productId) {
        return products.entrySet().stream()
                .filter(e -> e.getValue().getId().equals(productId))
                .findFirst();
    }
}
