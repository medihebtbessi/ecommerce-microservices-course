package com.iheb.ecommerce.order;

import com.iheb.ecommerce.customer.CustomerClient;
import com.iheb.ecommerce.exception.BusinessException;
import com.iheb.ecommerce.kafka.OrderConfirmation;
import com.iheb.ecommerce.kafka.OrderProducer;
import com.iheb.ecommerce.orderLine.OrderLineService;
import com.iheb.ecommerce.payment.PaymentClient;
import com.iheb.ecommerce.payment.PaymentRequest;
import com.iheb.ecommerce.product.ProductClient;
import com.iheb.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class OrderService {
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;
    public Integer createOrder(OrderRequest request) {
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order:: No Customer exists with the provided customerId "));

       var purchasedProducts= this.productClient.purchaseProducts(request.products());

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

        var paymentRequest=new PaymentRequest(
               request.amount(),
               request.paymentMethod(),
               order.getId(),
                order.getReference(),
                customer

        );

        paymentClient.requestOrderPayment(paymentRequest);
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository
                .findAll()
                .stream()
                .map(mapper::fromOrder)
                .toList();
    }

    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(()->new EntityNotFoundException("No Order found with id " + orderId));
    }
}
