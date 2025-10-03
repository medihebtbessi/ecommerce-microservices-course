package com.iheb.ecommerce.customer;

import com.iheb.ecommerce.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    public String createCustomer( CustomerRequest request) {
        var customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer( CustomerRequest request) {
        var customer = repository.findById(request.id()).orElseThrow(()->new CustomerNotFoundException(
            String.format("Customer with id %s not found", request.id())
        ));
        mergerCustomer(customer,request);
        repository.save(customer);
    }

    private void mergerCustomer(Customer customer, CustomerRequest request) {

    }

    public List<CustomerResponse> findAllCustomers() {
        return repository.findAll().stream().map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {
        return repository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return repository.findById(customerId).map(mapper::fromCustomer)
                .orElseThrow(()->new CustomerNotFoundException("Customer with id " + customerId + " not found"));
    }

    public void deleteById(String customerId) {
        repository.deleteById(customerId);
    }
}
