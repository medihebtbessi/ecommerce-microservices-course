package com.iheb.ecommerce.orderLine;

import com.iheb.ecommerce.order.Order;
import com.iheb.ecommerce.order.OrderLineRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request) {
        return  OrderLine.builder()
                .id(request.id())
                .productId(request.productId())
                .quantity(request.quantity())
                .order(
                        Order.builder()
                                .id(request.id())
                                .build()
                )
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQuantity()
        );
    }
}
