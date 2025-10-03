package com.iheb.ecommerce.payment;

import com.iheb.ecommerce.customer.CustomerResponse;
import com.iheb.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
