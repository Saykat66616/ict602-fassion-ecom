package com.fashionstore.service;

import com.fashionstore.model.Payment;

import java.math.BigDecimal;

// Polymorphism: this class can be used wherever PaymentGateway is required.
public class MockPaymentGateway implements PaymentGateway {
    @Override
    public boolean authorize(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public boolean capture(Payment payment) {
        return authorize(payment.getAmount());
    }
}
