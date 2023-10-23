package com.jia.mini.loan.predictor.service;

import com.jia.mini.loan.predictor.dto.CreateCustomerRequest;
import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    ResponseEntity<GeneralResponse> addCustomer(CreateCustomerRequest createCustomerRequest);
    ResponseEntity<List<Customer>>getCustomerInfo();
}
