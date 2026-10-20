package com.rosales.ui;

import com.rosales.dao.ScenarioDao;
import com.rosales.model.ScenarioEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Interactive console menu. No SQL or JDBC here: it only talks to the DAO.
 * Every read uses nextLine(), so no leftover newline stays in the Scanner buffer
 * after numeric input (numbers are parsed from the full line).
 */
public class ScenarioMenu {

    private static final Set<String> CATEGORIES = Set.of("ANTIBIOTIC", "PAINKILLER", "VITAMIN");
    private static final String HEAVY = "=".repeat(52);
    private static final String LIGHT = "-".repeat(52);

    private final ScenarioDao dao;
    private final Scanner scanner = new Scanner(System.in);

    public ScenarioMenu(ScenarioDao dao) {
        this.dao = dao;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addMedicine();
                case "2" -> viewInventory();
                case "3" -> {
                    System.out.println("\nExiting system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("\nERROR: Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println(HEAVY);
        System.out.println("           PHARMACY MEDICINE INVENTORY");
        System.out.println(HEAVY);
        System.out.println("[1] Add New Medicine");
        System.out.println("[2] View Medicine Inventory");
        System.out.println("[3] Exit System");
        System.out.println(LIGHT);
        System.out.print("Enter choice: ");
    }

    private void addMedicine() {
        System.out.println("\n--- ADD NEW MEDICINE ---");
        String brand = promptBrand();
        String category = promptCategory();
        double price = promptPrice();
        int stock = promptStock();

        try {
            dao.insert(new ScenarioEntity(0, brand, category, price, stock));
            System.out.println("\nSUCCESS: Medicine item added to inventory!");
        } catch (SQLException e) {
            System.out.println("\nERROR: Database problem - " + e.getMessage());
        }
    }

    private void viewInventory() {
        try {
            List<ScenarioEntity> items = dao.findAll();

            String sep = "+" + "-".repeat(6) + "+" + "-".repeat(22) + "+" + "-".repeat(14)
                    + "+" + "-".repeat(12) + "+" + "-".repeat(9) + "+";
            String title = "PHARMACY MEDICINE STOCK";
            int pad = (sep.length() - title.length()) / 2;

            System.out.println();
            System.out.println("=".repeat(sep.length()));
            System.out.println(" ".repeat(pad) + title);
            System.out.println("=".repeat(sep.length()));
            System.out.println(sep);
            System.out.printf("| %-4s | %-20s | %-12s | %-10s | %-7s |%n",
                    "ID", "BRAND NAME", "CATEGORY", "PRICE", "STOCK");
            System.out.println(sep);

            if (items.isEmpty()) {
                System.out.printf("| %-" + (sep.length() - 4) + "s |%n", "No records found.");
            } else {
                for (ScenarioEntity m : items) {
                    System.out.printf("| %-4d | %-20s | %-12s | %-10.2f | %-7d |%n",
                            m.id(), truncate(m.brandName(), 20), m.category(), m.price(), m.stock());
                }
            }
            System.out.println(sep);
            System.out.println("Total Records Displayed: " + items.size());
        } catch (SQLException e) {
            System.out.println("\nERROR: Database problem - " + e.getMessage());
        }
    }

    // ---------- input helpers with validation ----------

    private String promptBrand() {
        while (true) {
            System.out.print("Enter Brand Name: ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("ERROR: Brand name cannot be empty.");
        }
    }

    private String promptCategory() {
        while (true) {
            System.out.print("Enter Category (ANTIBIOTIC / PAINKILLER / VITAMIN): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (CATEGORIES.contains(input)) {
                return input;
            }
            System.out.println("ERROR: Category must be ANTIBIOTIC, PAINKILLER, or VITAMIN.");
        }
    }

    private double promptPrice() {
        while (true) {
            System.out.print("Enter Price: ");
            try {
                double price = Double.parseDouble(scanner.nextLine().trim());
                if (price >= 0) {
                    return price;
                }
                System.out.println("ERROR: Price cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter a valid number (e.g., 8.50).");
            }
        }
    }

    private int promptStock() {
        while (true) {
            System.out.print("Enter Stock Quantity: ");
            try {
                int stock = Integer.parseInt(scanner.nextLine().trim());
                if (stock >= 0) {
                    return stock;
                }
                System.out.println("ERROR: Stock cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter a whole number (e.g., 150).");
            }
        }
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}
