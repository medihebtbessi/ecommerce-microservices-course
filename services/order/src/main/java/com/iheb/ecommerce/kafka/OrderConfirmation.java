package com.iheb.ecommerce.kafka;

import com.iheb.ecommerce.customer.CustomerResponse;
import com.iheb.ecommerce.order.PaymentMethod;
import com.iheb.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
