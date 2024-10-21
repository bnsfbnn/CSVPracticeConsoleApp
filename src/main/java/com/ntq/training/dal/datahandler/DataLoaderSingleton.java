package com.ntq.training.dal.datahandler;

import com.ntq.training.dal.entity.Product;
import lombok.Getter;

public class DataLoaderSingleton {
    @Getter
    private static final DataLoader<Product> instance = new DataLoader<>();

    private DataLoaderSingleton() {
    }

}
