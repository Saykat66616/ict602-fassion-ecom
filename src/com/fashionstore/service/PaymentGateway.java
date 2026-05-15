package com.fashionstore.service;

import com.fashionstore.model.Payment;

import java.math.BigDecimal;

// Abstraction: checkout depends on this interface, not on a specific payment provider.
public interface PaymentGateway {
    boolean authorize(BigDecimal amount);

    boolean capture(Payment payment);
}
