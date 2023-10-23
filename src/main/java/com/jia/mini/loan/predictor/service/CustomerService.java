package com.jia.mini.loan.predictor.service;

import com.jia.mini.loan.predictor.dto.CreateCustomerRequest;
import com.jia.mini.loan.predictor.dto.ResponseDto;
import com.jia.mini.loan.predictor.entities.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    ResponseEntity<ResponseDto> addCustomer(CreateCustomerRequest createCustomerRequest);
    ResponseEntity<List<Customer>>getCustomerInfo();
}
