package com.ntq.training.pl;

import com.ntq.training.bl.IDataService;
import com.ntq.training.bl.impl.CustomerServiceImpl;
import com.ntq.training.bl.impl.OrderServiceImpl;
import com.ntq.training.bl.impl.ProductServiceImpl;
import com.ntq.training.infra.constants.FileConstants;

import java.nio.file.Paths;
import java.util.Map;

public class CommonDataHandler {
    protected final Map<String, Object> services = Map.of(
            "products", new ProductServiceImpl(),
            "customers", new CustomerServiceImpl(),
            "orders", new OrderServiceImpl()
    );

    protected <T> Map<Integer, T> loadData(String type, String filePath) throws Exception {
        IDataService<T> service = (IDataService<T>) services.get(type);
        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, type + FileConstants.ORIGIN_CSV_FILE_EXTENSION).toString();
        return service.loadFile(loaderPath);
    }

    protected <T> void saveData(String type, String filePath, Map<Integer, T> data) throws Exception {
        IDataService<T> service = (IDataService<T>) services.get(type);
        String writerPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, type + FileConstants.OUTPUT_CSV_FILE_EXTENSION).toString();
        service.saveFile(writerPath, data);
    }
}
