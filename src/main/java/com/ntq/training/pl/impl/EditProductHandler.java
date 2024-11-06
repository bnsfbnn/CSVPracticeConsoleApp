package com.ntq.training.pl.impl;

import com.ntq.training.bl.ProductService;
import com.ntq.training.bl.impl.ProductServiceImpl;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.pl.IBaseFunction;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class EditProductHandler implements IBaseFunction {
    ProductService service = new ProductServiceImpl();
    
    @Override
    public void processFunction(String filePath) throws Exception {
        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "products" + FileConstants.ORIGIN_CSV_FILE_EXTENSION).toString();
        Map<Integer, Product> products = service.loadFile(loaderPath);

        String updateLoaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "products" + FileConstants.EDIT_CSV_FILE_EXTENSION).toString();
        Map<Integer, Product> updateProducts = service.loadFile(updateLoaderPath);

        products = service.update(filePath, products, updateProducts);
        String writerPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, "products" + FileConstants.OUTPUT_CSV_FILE_EXTENSION).toString();
        service.saveFile(writerPath, products);
    }
}
