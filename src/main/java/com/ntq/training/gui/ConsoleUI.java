package com.ntq.training.gui;

import com.ntq.training.service.CustomerService;
import com.ntq.training.service.OrderService;
import com.ntq.training.service.ProductService;

import java.util.Scanner;

public class ConsoleUI {



    private final ProductService productService = new ProductService();
    private final CustomerService customerService = new CustomerService();
    private final OrderService orderService = new OrderService();

    public void run(String functionCode, String filePath) {
        Scanner scanner = new Scanner(System.in);

        switch (functionCode) {
            case "1":
                loadData(scanner, filePath);
            case "2":
                manageProducts(scanner, filePath);
                break;
            case "3":
                manageCustomers(scanner, filePath);
                break;
            case "4":
                manageOrders(scanner, filePath);
                break;
            default:
                System.out.println("Invalid function code.");
        }
    }

    private void loadData(Scanner scanner, String filePath) {
        System.out.println("Load Data: ");

    }

    private void manageProducts(Scanner scanner, String filePath) {
        System.out.println("Product Management Menu:");
        System.out.println("1. Add Product");
        System.out.println("2. Update Product");
        System.out.println("3. Delete Product");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println("Enter product details...");
                break;
            case 2:
                System.out.println("Enter product ID to update...");
                break;
            case 3:
                System.out.println("Enter product ID to delete...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void manageCustomers(Scanner scanner, String filePath) {
        System.out.println("Customer Management Menu:");
    }

    private void manageOrders(Scanner scanner, String filePath) {
        System.out.println("Order Management Menu:");
    }
}
