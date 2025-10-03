package com.iheb.ecommerce.orderLine;

import com.iheb.ecommerce.order.OrderLineRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper mapper;
    public Integer saveOrderLine(OrderLineRequest request) {
        var order=mapper.toOrderLine(request);
        return orderLineRepository.save(order).getId();
    }

    public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
        return orderLineRepository.findAllByOrderId(orderId)
                .stream().map(mapper::toOrderLineResponse)
                .toList();
    }
}
