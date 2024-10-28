package com.ntq.training;

import com.ntq.training.pl.FunctionController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java App <functionCode> <filePath>");
            return;
        }
        String functionCode = args[0];
        String filePath = args[1];
        FunctionController.run(functionCode, filePath);
    }
}
