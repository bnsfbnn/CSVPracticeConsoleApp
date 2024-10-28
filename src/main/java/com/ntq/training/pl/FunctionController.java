package com.ntq.training.pl;

import com.ntq.training.pl.impl.*;

import java.util.Map;

public interface FunctionController {
    static void run(String functionCode, String filePath) {
        Map<String, FunctionController> functionMap = Map.of(
                "1", new LoadDataController(),
                "2.1", new AddProductController(),
                "2.2", new EditProductController(),
                "2.3", new DeleteDataController(),
                "3.1", new AddCustomerController(),
                "3.2", new EditProductController(),
                "3.3", new DeleteDataController(),
                "4.1", new AddOrderController(),
                "4.2", new EditProductController(),
                "4.3", new DeleteDataController()
        );
        FunctionController functionController = functionMap.get(functionCode);
        if (functionController != null) {
            functionController.processFunction(filePath);
        } else {
            System.out.println("Invalid function code.");
        }
    }

    void processFunction(String filePath);
}
