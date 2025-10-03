package com.iheb.ecommerce.order;

import com.iheb.ecommerce.customer.CustomerClient;
import com.iheb.ecommerce.exception.BusinessException;
import com.iheb.ecommerce.orderLine.OrderLineService;
import com.iheb.ecommerce.product.ProductClient;
import com.iheb.ecommerce.product.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class OrderService {
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    public Integer createOrder(OrderRequest request) {
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order:: No Customer exists with the provided customerId "));

        this.productClient.purchaseProducts(request.products());

        var order =repository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest :request.products()){
            orderLineService.saveOrderLine(
                new OrderLineRequest(
                        null,
                        order.getId(),
                        purchaseRequest.productId(),
                        purchaseRequest.quantity()
                )
            );
        }


        return null;

    }
}
