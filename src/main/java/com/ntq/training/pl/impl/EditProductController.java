package com.ntq.training.pl.impl;

import com.ntq.training.bl.ProductService;
import com.ntq.training.bl.impl.ProductServiceImpl;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.pl.IBaseController;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class EditProductController implements IBaseController {
    @Override
    public void processFunction(String filePath) {
        ProductService service = new ProductServiceImpl();
        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "products" + FileConstants.ORIGIN_CSV_FILE_EXTENSION).toString();
        Map<Integer, Product> products = service.loadFile(loaderPath);
        log.info("FUNCTION 2.2 - Load data from products.origin.csv successfully!");

        String updateLoaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "products" + FileConstants.EDIT_CSV_FILE_EXTENSION).toString();
        Map<Integer, Product> updateProducts = service.loadFile(updateLoaderPath);
        log.info("FUNCTION 2.2 - Load data from products.edit.csv successfully!");

        products = service.update(filePath, products, updateProducts);
        String writerPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, "products" + FileConstants.OUTPUT_CSV_FILE_EXTENSION).toString();
        service.saveFile(writerPath, products);
        log.info("FUNCTION 2.2 - Load data to products.output.csv successfully!");
    }
}
