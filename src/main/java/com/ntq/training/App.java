package com.ntq.training;

import com.ntq.training.pl.IBaseController;
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
        IBaseController.run(functionCode, filePath);
    }
}
