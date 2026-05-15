package com.fashionstore.service;

import com.fashionstore.model.CartItem;
import com.fashionstore.model.Customer;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.Payment;

import java.math.BigDecimal;
import java.util.UUID;

public class CheckoutService {
    // Dependency inversion: service depends on PaymentGateway interface.
    private final PaymentGateway paymentGateway;

    public CheckoutService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public Order checkout(Customer customer) {
        // Service layer: coordinates cart, inventory, order, and payment objects.
        if (customer.getShoppingCart().getItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart.");
        }

        Order order = new Order("ORD-" + UUID.randomUUID().toString().substring(0, 8), customer);

        for (CartItem cartItem : customer.getShoppingCart().getItems()) {
            cartItem.getVariant().getInventoryItem().reserveStock(cartItem.getQuantity());
            order.addItem(new OrderItem(cartItem.getVariant(), cartItem.getQuantity()));
        }

        BigDecimal total = order.calculateTotal();
        Payment payment = new Payment("PAY-" + UUID.randomUUID().toString().substring(0, 8), total);

        if (paymentGateway.capture(payment)) {
            payment.approve();
            order.addPayment(payment);
            customer.addLoyaltyPoints(total.intValue());
            customer.getShoppingCart().clear();
            return order;
        }

        payment.decline();
        order.addPayment(payment);
        throw new IllegalStateException("Payment declined.");
    }
}
