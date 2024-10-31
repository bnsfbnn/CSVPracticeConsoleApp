package com.ntq.training;

import com.ntq.training.pl.IBaseFunction;
import com.ntq.training.pl.impl.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class App {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java App <functionCode> <filePath>");
            return;
        }
        String functionCode = args[0];
        String filePath = args[1];

        Map<String, IBaseFunction> functionMap = Map.ofEntries(
                Map.entry("1", new LoadDataHandler()),
                Map.entry("2.1", new AddProductHandler()),
                Map.entry("2.2", new EditProductHandler()),
                Map.entry("2.3", new DeleteProductHandler()),
                Map.entry("3.1", new DeleteCustomerHandler()),
                Map.entry("3.2", new AddCustomerHandler()),
                Map.entry("3.3", new EditCustomerHandler()),
                Map.entry("4.1", new AddOrderHandler()),
                Map.entry("4.2", new EditOrderHandler()),
                Map.entry("4.3", new DeleteOrderHandler()),
                Map.entry("5.1", new SearchTopProductHandler()),
                Map.entry("5.2", new SearchOrdersByProductHandler())
        );

        IBaseFunction baseFunction = functionMap.get(functionCode);
        if (baseFunction != null) {
            baseFunction.processFunction(filePath);
        } else {
            System.out.println("Invalid function code.");
        }
    }
}
