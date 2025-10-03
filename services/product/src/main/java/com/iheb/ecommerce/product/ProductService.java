package com.iheb.ecommerce.product;

import com.iheb.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    public Integer createProduct(@Valid ProductRequest request) {
        var product = mapper.toProduct(request);
        return repository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        var productIds= request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        var storedProducts = repository.findAllByIdInOrderById(productIds);
        if (productIds.size() != storedProducts.size()) {
            throw  new ProductPurchaseException("One or more products does not exists");
        }
        var storedRequest = request.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for (int i=0; i<storedProducts.size(); i++) {
            var product= storedProducts.get(i);
            var productRequest=storedRequest.get(i);
            if (product.getAvailableQuantity()<productRequest.quantity()){
                throw  new ProductPurchaseException("Product request quantity exceeded");
            }
            var newAvailableQuantity = product.getAvailableQuantity()-productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            repository.save(product);
            purchasedProducts.add(mapper.toProductPurchaseResponse(product,productRequest.quantity()));
        }
        return  purchasedProducts;
    }

    public ProductResponse findById(Integer productId) {
        return repository.findById(productId)
                .map(mapper::toProductResponse)
                .orElseThrow(()-> new EntityNotFoundException("Product not found with id: " + productId));
    }

    public List<ProductResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toProductResponse)
                .toList();
    }
}
