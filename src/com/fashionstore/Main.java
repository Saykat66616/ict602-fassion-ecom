package com.fashionstore;

import com.fashionstore.model.Customer;
import com.fashionstore.model.Order;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;
import com.fashionstore.model.Seller;
import com.fashionstore.service.CheckoutService;
import com.fashionstore.service.MockPaymentGateway;
import com.fashionstore.service.ProductCatalogService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductCatalogService catalogService = new ProductCatalogService();
        CheckoutService checkoutService = new CheckoutService(new MockPaymentGateway());

        Seller demoSeller = seedCatalog(catalogService);

        System.out.println("=== Fashion Store ===");
        System.out.println("1. Customer login");
        System.out.println("2. Seller login");
        int role = readInt(scanner, "Choose account type: ");

        if (role == 1) {
            Customer customer = customerLogin(scanner);
            runCustomerMenu(scanner, catalogService, checkoutService, customer);
        } else if (role == 2) {
            Seller seller = sellerLogin(scanner, demoSeller);
            runSellerMenu(scanner, catalogService, seller);
        } else {
            System.out.println("Invalid account type.");
        }
    }

    private static Customer customerLogin(Scanner scanner) {
        System.out.println("=== Customer Login ===");
        String name = readText(scanner, "Enter your name: ");
        String email = readText(scanner, "Enter your email: ");

        return new Customer("C-" + shortId(), name, email);
    }

    private static Seller sellerLogin(Scanner scanner, Seller demoSeller) {
        System.out.println("=== Seller Login ===");
        String name = readText(scanner, "Enter seller name: ");
        String email = readText(scanner, "Enter seller email: ");
        String storeName = readText(scanner, "Enter store name: ");

        if (email.equalsIgnoreCase(demoSeller.getEmail())) {
            return demoSeller;
        }

        return new Seller("S-" + shortId(), name, email, storeName);
    }

    private static void runCustomerMenu(Scanner scanner, ProductCatalogService catalogService, CheckoutService checkoutService, Customer customer) {
        System.out.println();
        System.out.println("Welcome customer, " + customer.getName() + "!");

        boolean running = true;
        while (running) {
            printCustomerMenu();
            int choice = readInt(scanner, "Choose option: ");

            switch (choice) {
                case 1:
                    printCatalogWithNumbers(catalogService);
                    break;
                case 2:
                    addProductToCart(scanner, catalogService, customer);
                    break;
                case 3:
                    printCart(customer);
                    break;
                case 4:
                    checkout(checkoutService, customer);
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you for visiting Style Hub.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void printCustomerMenu() {
        System.out.println();
        System.out.println("=== Customer Menu ===");
        System.out.println("1. View products");
        System.out.println("2. Add product to cart");
        System.out.println("3. View cart");
        System.out.println("4. Checkout");
        System.out.println("5. Exit");
    }

    private static Seller seedCatalog(ProductCatalogService catalogService) {
        Seller seller = new Seller("S001", "Style Hub Seller", "seller@stylehub.com", "Style Hub");

        Product jacket = catalogService.createProduct(
                seller,
                "P001",
                "Denim Jacket",
                "Classic blue denim jacket",
                "UrbanWear",
                new BigDecimal("79.99")
        );
        catalogService.addVariant(jacket, "V001", "M", "Blue", "DJ-M-BLUE", new BigDecimal("79.99"), 10);
        catalogService.addVariant(jacket, "V002", "L", "Black", "DJ-L-BLACK", new BigDecimal("84.99"), 5);

        Product shirt = catalogService.createProduct(
                seller,
                "P002",
                "Cotton Shirt",
                "Soft casual shirt",
                "DailyFit",
                new BigDecimal("34.99")
        );
        catalogService.addVariant(shirt, "V003", "M", "White", "CS-M-WHITE", new BigDecimal("34.99"), 12);
        catalogService.addVariant(shirt, "V004", "L", "Green", "CS-L-GREEN", new BigDecimal("36.99"), 8);

        return seller;
    }

    private static void runSellerMenu(Scanner scanner, ProductCatalogService catalogService, Seller seller) {
        System.out.println();
        System.out.println("Welcome seller, " + seller.getName() + " (" + seller.getStoreName() + ")!");

        boolean running = true;
        while (running) {
            printSellerMenu();
            int choice = readInt(scanner, "Choose option: ");

            switch (choice) {
                case 1:
                    printSellerProducts(catalogService, seller);
                    break;
                case 2:
                    addSellerProduct(scanner, catalogService, seller);
                    break;
                case 3:
                    updateSellerProduct(scanner, catalogService, seller);
                    break;
                case 4:
                    deleteSellerProduct(scanner, catalogService, seller);
                    break;
                case 5:
                    running = false;
                    System.out.println("Seller session closed.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void printSellerMenu() {
        System.out.println();
        System.out.println("=== Seller Menu ===");
        System.out.println("1. View my products");
        System.out.println("2. Add product");
        System.out.println("3. Update product");
        System.out.println("4. Delete product");
        System.out.println("5. Exit");
    }

    private static void addSellerProduct(Scanner scanner, ProductCatalogService catalogService, Seller seller) {
        System.out.println();
        System.out.println("=== Add Product ===");
        String name = readText(scanner, "Product name: ");
        String description = readText(scanner, "Description: ");
        String brand = readText(scanner, "Brand: ");
        BigDecimal basePrice = readMoney(scanner, "Base price: ");

        Product product = catalogService.createProduct(
                seller,
                "P-" + shortId(),
                name,
                description,
                brand,
                basePrice
        );

        System.out.println("Add first product variant");
        String size = readText(scanner, "Size: ");
        String color = readText(scanner, "Color: ");
        String sku = readText(scanner, "SKU: ");
        BigDecimal variantPrice = readMoney(scanner, "Variant price: ");
        int stock = readPositiveInt(scanner, "Initial stock: ");

        catalogService.addVariant(product, "V-" + shortId(), size, color, sku, variantPrice, stock);
        System.out.println("Product added successfully: " + product.getProductId());
    }

    private static void updateSellerProduct(Scanner scanner, ProductCatalogService catalogService, Seller seller) {
        System.out.println();
        System.out.println("=== Update Product ===");
        printSellerProducts(catalogService, seller);
        String productId = readText(scanner, "Enter product ID to update: ");
        String name = readText(scanner, "New product name: ");
        String description = readText(scanner, "New description: ");
        String brand = readText(scanner, "New brand: ");
        BigDecimal basePrice = readMoney(scanner, "New base price: ");

        boolean updated = catalogService.updateProduct(seller, productId, name, description, brand, basePrice);
        if (updated) {
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Product not found, or this product does not belong to this seller.");
        }
    }

    private static void deleteSellerProduct(Scanner scanner, ProductCatalogService catalogService, Seller seller) {
        System.out.println();
        System.out.println("=== Delete Product ===");
        printSellerProducts(catalogService, seller);
        String productId = readText(scanner, "Enter product ID to delete: ");

        boolean deleted = catalogService.deleteProduct(seller, productId);
        if (deleted) {
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println("Product not found, or this product does not belong to this seller.");
        }
    }

    private static void printSellerProducts(ProductCatalogService catalogService, Seller seller) {
        List<Product> products = catalogService.getProductsBySeller(seller);

        System.out.println();
        System.out.println("=== My Products ===");
        if (products.isEmpty()) {
            System.out.println("No products found for this seller.");
            return;
        }

        for (Product product : products) {
            System.out.println(product.getProductId() + " - " + product.getName() + " | Base price: $" + product.getBasePrice());
            for (ProductVariant variant : product.getVariants()) {
                System.out.println("  - " + variant
                        + " | $" + variant.getPrice()
                        + " | Stock: " + variant.getInventoryItem().getQuantityAvailable());
            }
        }
    }

    private static void addProductToCart(Scanner scanner, ProductCatalogService catalogService, Customer customer) {
        List<ProductVariant> variants = printCatalogWithNumbers(catalogService);
        if (variants.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        int productNumber = readInt(scanner, "Enter product number: ");
        if (productNumber < 1 || productNumber > variants.size()) {
            System.out.println("Invalid product number.");
            return;
        }

        ProductVariant selectedVariant = variants.get(productNumber - 1);
        int availableStock = selectedVariant.getInventoryItem().getQuantityAvailable();
        if (availableStock == 0) {
            System.out.println("Sorry, this product is out of stock.");
            return;
        }

        int quantity = readInt(scanner, "Enter quantity: ");
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }
        if (quantity > availableStock) {
            System.out.println("Only " + availableStock + " item(s) available.");
            return;
        }

        customer.getShoppingCart().addItem(selectedVariant, quantity);
        System.out.println(quantity + " item(s) added to cart.");
    }

    private static List<ProductVariant> printCatalogWithNumbers(ProductCatalogService catalogService) {
        List<ProductVariant> variants = new ArrayList<>();
        System.out.println();
        System.out.println("=== Product Catalog ===");

        for (Product product : catalogService.getProducts()) {
            System.out.println(product);
            for (ProductVariant variant : product.getVariants()) {
                variants.add(variant);
                System.out.println(variants.size() + ". " + variant
                        + " | $" + variant.getPrice()
                        + " | Stock: " + variant.getInventoryItem().getQuantityAvailable());
            }
        }

        return variants;
    }

    private static void printCart(Customer customer) {
        System.out.println();
        System.out.println("=== Your Cart ===");
        if (customer.getShoppingCart().getItems().isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        customer.getShoppingCart().printCart();
    }

    private static void checkout(CheckoutService checkoutService, Customer customer) {
        System.out.println();
        System.out.println("=== Checkout ===");

        if (customer.getShoppingCart().getItems().isEmpty()) {
            System.out.println("Your cart is empty. Add products before checkout.");
            return;
        }

        try {
            Order order = checkoutService.checkout(customer);
            System.out.println("Order placed successfully!");
            System.out.println(order);
            System.out.println("Loyalty points: " + customer.getLoyaltyPoints());
        } catch (IllegalStateException exception) {
            System.out.println("Checkout failed: " + exception.getMessage());
        }
    }

    private static String readText(Scanner scanner, String prompt) {
        String value;
        do {
            System.out.print(prompt);
            value = scanner.nextLine().trim();
        } while (value.isEmpty());

        return value;
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            int value = readInt(scanner, prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Value must be greater than zero.");
        }
    }

    private static BigDecimal readMoney(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                BigDecimal amount = new BigDecimal(value);
                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    return amount;
                }
                System.out.println("Amount must be greater than zero.");
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    private static String shortId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
