package com.jia.mini.loan.predictor.service.impl;

import com.jia.mini.loan.predictor.dto.CreateCustomerRequest;
import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.entities.Customer;
import com.jia.mini.loan.predictor.enums.CustomerCreditStatus;
import com.jia.mini.loan.predictor.repository.CustomerRepository;
import com.jia.mini.loan.predictor.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    GeneralResponse generalResponse;
    @Override
    //creating new customer
    public ResponseEntity<GeneralResponse> addCustomer(CreateCustomerRequest createCustomerRequest) {
        generalResponse = new GeneralResponse();
        try {
            Customer customer = Customer.builder()
                    .customerMobileNo(createCustomerRequest.getCustomerMobileNo())
                    .customerName(createCustomerRequest.getCustomerName())
                    .email(createCustomerRequest.getEmail())
                    .customerStatus(CustomerCreditStatus.valueOf(createCustomerRequest.getCustomerStatus()))
                    .build();
            customerRepository.save(customer);
            generalResponse.setStatus(HttpStatus.CREATED);
            generalResponse.setDescription("Customer Created Successfully");
        }catch (Exception e){
            generalResponse.setDescription("Customer Not Created");
            generalResponse.setStatus(HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<>(generalResponse, generalResponse.getStatus());
    }

    @Override
    //listing all customers
    public ResponseEntity<List<Customer>>getCustomerInfo() {

        List<Customer> customerInfo = customerRepository.findAll();

        return new ResponseEntity<>(customerInfo, HttpStatus.FOUND);
    }
}
