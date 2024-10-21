package com.ntq.training.service;

import com.ntq.training.dal.datahandler.DataLoader;
import com.ntq.training.dal.datahandler.DataLoaderSingleton;
import com.ntq.training.dal.entity.Product;

import java.util.ArrayList;
import java.util.List;

public interface ProductService {
    void loadProduct();

    void addProduct(Product product);

    void updateProduct(String productId, Product updatedProduct);

    void deleteProduct(String productId);
}
