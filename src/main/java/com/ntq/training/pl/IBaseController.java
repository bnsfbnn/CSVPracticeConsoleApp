package com.ntq.training.pl;

import com.ntq.training.pl.impl.*;

import java.util.Map;

public interface IBaseController {
    static void run(String functionCode, String filePath) {
        Map<String, IBaseController> functionMap = Map.of(
                "1", new LoadDataController(),
                "2.1", new AddProductController(),
                "2.2", new EditProductController(),
                "2.3", new DeleteProductController(),
                "3.1", new DeleteProductController(),
                "3.2", new AddCustomerController(),
                "3.3", new EditCustomerController(),
                "4.1", new AddOrderController(),
                "4.2", new EditOrderController(),
                "4.3", new DeleteOrderController()
        );
        IBaseController functionController = functionMap.get(functionCode);
        if (functionController != null) {
            functionController.processFunction(filePath);
        } else {
            System.out.println("Invalid function code.");
        }
    }

    void processFunction(String filePath);
}
