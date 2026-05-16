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

public class FashionStoreTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testProductVariantAndInventoryCreation();
        testSellerCanUpdateAndDeleteOwnProduct();
        testSellerCannotDeleteOtherSellerProduct();
        testCartTotalCalculation();
        testCheckoutReducesInventoryAndClearsCart();
        testCheckoutRejectsEmptyCart();

        System.out.println();
        System.out.println("Test result: " + passed + " passed, " + failed + " failed");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testProductVariantAndInventoryCreation() {
        ProductCatalogService catalogService = new ProductCatalogService();
        Seller seller = new Seller("S001", "Seller", "seller@test.com", "Style Hub");

        Product product = catalogService.createProduct(
                seller,
                "P001",
                "Denim Jacket",
                "Classic jacket",
                "UrbanWear",
                new BigDecimal("79.99")
        );

        ProductVariant variant = catalogService.addVariant(
                product,
                "V001",
                "M",
                "Blue",
                "DJ-M-BLUE",
                new BigDecimal("79.99"),
                10
        );

        assertEquals("product should have one variant", 1, product.getVariants().size());
        assertEquals("variant stock should be 10", 10, variant.getInventoryItem().getQuantityAvailable());
    }

    private static void testSellerCanUpdateAndDeleteOwnProduct() {
        ProductCatalogService catalogService = new ProductCatalogService();
        Seller seller = new Seller("S001", "Seller", "seller@test.com", "Style Hub");
        Product product = catalogService.createProduct(
                seller,
                "P001",
                "Denim Jacket",
                "Classic jacket",
                "UrbanWear",
                new BigDecimal("79.99")
        );

        boolean updated = catalogService.updateProduct(
                seller,
                product.getProductId(),
                "Updated Jacket",
                "Updated description",
                "UrbanWear",
                new BigDecimal("89.99")
        );
        boolean deleted = catalogService.deleteProduct(seller, product.getProductId());

        assertTrue("seller should update own product", updated);
        assertBigDecimalEquals("product price should be updated", new BigDecimal("89.99"), product.getBasePrice());
        assertTrue("seller should delete own product", deleted);
        assertEquals("catalog should be empty after delete", 0, catalogService.getProducts().size());
    }

    private static void testSellerCannotDeleteOtherSellerProduct() {
        ProductCatalogService catalogService = new ProductCatalogService();
        Seller owner = new Seller("S001", "Owner", "owner@test.com", "Owner Store");
        Seller otherSeller = new Seller("S002", "Other Seller", "other@test.com", "Other Store");
        Product product = catalogService.createProduct(
                owner,
                "P001",
                "Denim Jacket",
                "Classic jacket",
                "UrbanWear",
                new BigDecimal("79.99")
        );

        boolean deleted = catalogService.deleteProduct(otherSeller, product.getProductId());

        assertFalse("seller should not delete another seller product", deleted);
        assertEquals("catalog should still contain product", 1, catalogService.getProducts().size());
    }

    private static void testCartTotalCalculation() {
        ProductVariant variant = createVariantWithStock(new BigDecimal("50.00"), 10);
        Customer customer = new Customer("C001", "Customer", "customer@test.com");

        customer.getShoppingCart().addItem(variant, 3);

        assertEquals("cart should contain one item", 1, customer.getShoppingCart().getItems().size());
        assertBigDecimalEquals("cart total should be 150.00", new BigDecimal("150.00"), customer.getShoppingCart().calculateTotal());
    }

    private static void testCheckoutReducesInventoryAndClearsCart() {
        ProductVariant variant = createVariantWithStock(new BigDecimal("25.00"), 8);
        Customer customer = new Customer("C002", "Customer Two", "customer2@test.com");
        CheckoutService checkoutService = new CheckoutService(new MockPaymentGateway());

        customer.getShoppingCart().addItem(variant, 2);
        Order order = checkoutService.checkout(customer);

        assertBigDecimalEquals("order total should be 50.00", new BigDecimal("50.00"), order.calculateTotal());
        assertEquals("inventory should reduce from 8 to 6", 6, variant.getInventoryItem().getQuantityAvailable());
        assertEquals("cart should be empty after checkout", 0, customer.getShoppingCart().getItems().size());
        assertEquals("customer should receive loyalty points", 50, customer.getLoyaltyPoints());
    }

    private static void testCheckoutRejectsEmptyCart() {
        Customer customer = new Customer("C003", "Empty Cart Customer", "empty@test.com");
        CheckoutService checkoutService = new CheckoutService(new MockPaymentGateway());

        try {
            checkoutService.checkout(customer);
            fail("empty cart checkout should throw exception");
        } catch (IllegalStateException expected) {
            pass("empty cart checkout should throw exception");
        }
    }

    private static ProductVariant createVariantWithStock(BigDecimal price, int stock) {
        ProductCatalogService catalogService = new ProductCatalogService();
        Seller seller = new Seller("S-T", "Test Seller", "testseller@test.com", "Test Store");
        Product product = catalogService.createProduct(
                seller,
                "P-T",
                "Test Product",
                "Test Description",
                "Test Brand",
                price
        );

        return catalogService.addVariant(
                product,
                "V-T",
                "M",
                "Black",
                "TEST-SKU",
                price,
                stock
        );
    }

    private static void assertEquals(String testName, int expected, int actual) {
        if (expected == actual) {
            pass(testName);
        } else {
            fail(testName + " | expected: " + expected + ", actual: " + actual);
        }
    }

    private static void assertBigDecimalEquals(String testName, BigDecimal expected, BigDecimal actual) {
        if (expected.compareTo(actual) == 0) {
            pass(testName);
        } else {
            fail(testName + " | expected: " + expected + ", actual: " + actual);
        }
    }

    private static void assertTrue(String testName, boolean actual) {
        if (actual) {
            pass(testName);
        } else {
            fail(testName + " | expected: true, actual: false");
        }
    }

    private static void assertFalse(String testName, boolean actual) {
        if (!actual) {
            pass(testName);
        } else {
            fail(testName + " | expected: false, actual: true");
        }
    }

    private static void pass(String testName) {
        passed++;
        System.out.println("[PASS] " + testName);
    }

    private static void fail(String testName) {
        failed++;
        System.out.println("[FAIL] " + testName);
    }
}
