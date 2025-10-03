package com.iheb.ecommerce.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "product-service",
        url = "${application.config.payment-url}"
)
public interface PaymentClient {
    @PostMapping
    public Integer requestOrderPayment(@RequestBody PaymentRequest request);
}
