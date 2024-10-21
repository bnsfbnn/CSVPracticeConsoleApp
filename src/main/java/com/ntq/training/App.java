package com.ntq.training;

import com.ntq.training.gui.ConsoleUI;

public class App {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java App <functionCode> <filePath>");
            return;
        }
        String functionCode = args[0];
        String filePath = args[1];

        ConsoleUI consoleUI = new ConsoleUI();
        consoleUI.run(functionCode, filePath);
    }
}
