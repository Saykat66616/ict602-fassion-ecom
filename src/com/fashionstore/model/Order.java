package com.fashionstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String orderId;
    private final Customer customer;
    private final LocalDateTime orderDate;
    // Composition: an Order owns its OrderItem records.
    private final List<OrderItem> items = new ArrayList<>();
    // Composition: an Order keeps its Payment records.
    private final List<Payment> payments = new ArrayList<>();
    private OrderStatus status;

    public Order(String orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.CREATED;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        // State change: order becomes PAID only after an approved payment.
        if (payment.getStatus() == PaymentStatus.APPROVED) {
            status = OrderStatus.PAID;
        }
    }

    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.calculateSubtotal());
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Order ").append(orderId).append(" for ").append(customer.getName()).append("\n");
        builder.append("Date: ").append(orderDate).append("\n");
        builder.append("Status: ").append(status).append("\n");
        for (OrderItem item : items) {
            builder.append("- ").append(item).append("\n");
        }
        builder.append("Total: $").append(calculateTotal()).append("\n");
        for (Payment payment : payments) {
            builder.append("Payment: ").append(payment).append("\n");
        }
        return builder.toString();
    }
}
