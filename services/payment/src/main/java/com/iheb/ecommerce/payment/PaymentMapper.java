package com.iheb.ecommerce.payment;

import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {
    public Payment toPayment(PaymentRequest payment) {
        return Payment.builder()
                .id(payment.id())
                .orderId(payment.orderId())
                .paymentMethod(payment.paymentMethod())
                .amount(payment.amount())
                .build();
    }
}
