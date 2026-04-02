package in.ac.adit.pwj.miniproject.inventory;

import java.io.*;
import java.util.*;

/**
 * Base class representing a generic Product in the inventory.
 * Serves as the parent class for Electronics and Groceries.
 */
class Product {

    protected int id;           // Unique product identifier
    protected String name;      // Product name
    protected int quantity;     // Available stock count
    protected double price;     // Price per unit
    protected String category;  // Category: Electronics or Groceries

    /**
     * Constructs a new Product with all required details.
     *
     * @param id       Unique product ID
     * @param name     Product name
     * @param quantity Stock quantity
     * @param price    Price per unit
     * @param category Product category
     */
    public Product(int id, String name, int quantity, double price, String category) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    /**
     * Displays the product information in a formatted line.
     */
    public void display() {
        System.out.println(id + " " + name + " [" + category + "] Qty:" + quantity + " Price:" + price);
    }
}

/**
 * Represents an Electronics product.
 * Extends Product with category fixed to "Electronics".
 */
class Electronics extends Product {
    public Electronics(int id, String name, int quantity, double price) {
        super(id, name, quantity, price, "Electronics");
    }
}

/**
 * Represents a Groceries product.
 * Extends Product with category fixed to "Groceries".
 */
class Groceries extends Product {
    public Groceries(int id, String name, int quantity, double price) {
        super(id, name, quantity, price, "Groceries");
    }
}

/**
 * Custom exception to handle inventory stock-related errors.
 * Thrown when a product is not found or stock is insufficient.
 */
class StockException extends Exception {
    public StockException(String msg) {
        super(msg);
    }
}

/**
 * Core class managing all inventory operations.
 * Handles CRUD operations, file I/O, and order processing.
 */
class InventoryManager {

    // HashMap provides O(1) lookup by product ID
    private Map<Integer, Product> inventory = new HashMap<>();

    // Tracks all processed orders in memory during the session
    private List<String> orderHistory = new ArrayList<>();

    /**
     * Adds a product to the inventory.
     *
     * @param p The product to add (Electronics or Groceries)
     */
    public void addProduct(Product p) {
        inventory.put(p.id, p);
    }

    /**
     * Displays all products currently in the inventory.
     */
    public void displayProducts() {
        for (Product p : inventory.values()) {
            p.display();
        }
    }

    /**
     * Searches products by a keyword match in the product name.
     * The search is case-insensitive.
     *
     * @param keyword The search term to look for
     */
    public void searchProduct(String keyword) {
        boolean found = false;
        for (Product p : inventory.values()) {
            if (p.name.toLowerCase().contains(keyword.toLowerCase())) {
                p.display();
                found = true;
            }
        }
        if (!found) System.out.println("No product found.");
    }

    /**
     * Filters and displays products belonging to a specific category.
     *
     * @param category The category to filter by (e.g., "Electronics")
     */
    public void filterByCategory(String category) {
        boolean found = false;
        for (Product p : inventory.values()) {
            if (p.category.equalsIgnoreCase(category)) {
                p.display();
                found = true;
            }
        }
        if (!found) System.out.println("No products in this category.");
    }

    /**
     * Inner class that implements Runnable to process orders on a separate thread.
     * Enables concurrent order handling.
     */
    class OrderProcessor implements Runnable {
        int productId, qty;

        /**
         * @param productId The ID of the product to order
         * @param qty       The number of units to order
         */
        public OrderProcessor(int productId, int qty) {
            this.productId = productId;
            this.qty = qty;
        }

        /**
         * Executes the order processing on a new thread.
         * Catches and displays StockException errors.
         */
        public void run() {
            try {
                processOrder(productId, qty);
            } catch (StockException e) {
                System.out.println("Order Failed: " + e.getMessage());
            }
        }
    }

    /**
     * Processes an order for a given product and quantity.
     * Synchronized to ensure thread safety in concurrent environments.
     *
     * @param id  Product ID to order
     * @param qty Quantity to deduct from stock
     * @throws StockException if the product doesn't exist or stock is insufficient
     */
    public synchronized void processOrder(int id, int qty) throws StockException {
        // Validate product existence
        if (!inventory.containsKey(id))
            throw new StockException("Invalid Product ID");

        Product p = inventory.get(id);

        // Validate sufficient stock
        if (p.quantity < qty)
            throw new StockException("Insufficient stock for " + p.name);

        // Deduct stock and record order
        p.quantity -= qty;
        String order = "Order: " + p.name + " Qty: " + qty;
        orderHistory.add(order);
        System.out.println(order + " successful.");
    }

    /**
     * Saves the current inventory to a CSV file at D:\inventory\inventory.txt.
     * Each product is written as: id,name,quantity,price,category
     */
    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\inventory\\inventory.txt"))) {
            for (Product p : inventory.values()) {
                bw.write(p.id + "," + p.name + "," + p.quantity + "," + p.price + "," + p.category);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    /**
     * Loads inventory data from the file on startup.
     * Parses each CSV line into a Product object.
     */
    public void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\inventory\\inventory.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                Product p = new Product(
                        Integer.parseInt(d[0]),   // id
                        d[1],                     // name
                        Integer.parseInt(d[2]),   // quantity
                        Double.parseDouble(d[3]), // price
                        d[4]                      // category
                );
                inventory.put(p.id, p);
            }
        } catch (IOException e) {
            System.out.println("No previous data.");
        }
    }
}

/**
 * Entry point of the Inventory Management System.
 * Provides a menu-driven console interface for all operations.
 */
public class MainApp {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        InventoryManager manager = new InventoryManager();

        // Load existing inventory from file on startup
        manager.loadFromFile();

        int choice;

        do {
            System.out.println("\n1.Add Product  2.Display  3.Search  4.Filter  5.Order  6.Exit");
            choice = sc.nextInt();

            switch (choice) {

                case 1: // Add a new product
                    System.out.print("Enter ID Name Qty Price Category(Electronics/Groceries): ");
                    int id = sc.nextInt();
                    String name = sc.next();
                    int qty = sc.nextInt();
                    double price = sc.nextDouble();
                    String cat = sc.next();

                    // Create appropriate subclass based on category
                    if (cat.equalsIgnoreCase("Electronics"))
                        manager.addProduct(new Electronics(id, name, qty, price));
                    else
                        manager.addProduct(new Groceries(id, name, qty, price));
                    break;

                case 2: // Display all products
                    manager.displayProducts();
                    break;

                case 3: // Search by keyword
                    System.out.print("Enter keyword: ");
                    manager.searchProduct(sc.next());
                    break;

                case 4: // Filter by category
                    System.out.print("Enter category: ");
                    manager.filterByCategory(sc.next());
                    break;

                case 5: // Place an order using a new thread
                    System.out.print("Enter product ID and quantity: ");
                    int pid = sc.nextInt();
                    int q = sc.nextInt();

                    // Run order processing on a separate thread
                    Thread t = new Thread(manager.new OrderProcessor(pid, q));
                    t.start();

                    // Wait for thread to finish before continuing
                    try { t.join(); } catch (Exception e) {}
                    break;

                case 6: // Save inventory and exit
                    manager.saveToFile();
                    System.out.println("Data saved. Exiting...");
                    break;
            }

        } while (choice != 6);

        sc.close();
    }
}
