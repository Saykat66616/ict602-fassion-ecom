package com.fashionstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    // Encapsulation: payment status changes only through approve() or decline().
    private final String paymentId;
    private final BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime paidAt;

    public Payment(String paymentId, BigDecimal amount) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void approve() {
        status = PaymentStatus.APPROVED;
        paidAt = LocalDateTime.now();
    }

    public void decline() {
        status = PaymentStatus.DECLINED;
    }

    @Override
    public String toString() {
        return paymentId + " $" + amount + " " + status + (paidAt == null ? "" : " at " + paidAt);
    }
}
