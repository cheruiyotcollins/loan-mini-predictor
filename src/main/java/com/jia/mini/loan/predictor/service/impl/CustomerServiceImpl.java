package com.jia.mini.loan.predictor.service.impl;

import com.jia.mini.loan.predictor.dto.CreateCustomerRequest;
import com.jia.mini.loan.predictor.dto.ResponseDto;
import com.jia.mini.loan.predictor.entities.Customer;
import com.jia.mini.loan.predictor.enums.CustomerCreditStatus;
import com.jia.mini.loan.predictor.repository.CustomerRepository;
import com.jia.mini.loan.predictor.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    ResponseDto responseDto;
    @Override
    //creating new customer
    public ResponseEntity<ResponseDto> addCustomer(CreateCustomerRequest createCustomerRequest) {
        responseDto = new ResponseDto();
        try {
            Customer customer = Customer.builder()
                    .customerMobileNo(createCustomerRequest.getCustomerMobileNo())
                    .customerName(createCustomerRequest.getCustomerName())
                    .email(createCustomerRequest.getEmail())
                    .customerStatus(CustomerCreditStatus.valueOf(createCustomerRequest.getCustomerStatus()))
                    .build();
            customerRepository.save(customer);
            responseDto.setStatus(HttpStatus.CREATED);
            responseDto.setDescription("Customer Created Successfully");
        }catch (Exception e){
            responseDto.setDescription("Customer Not Created");
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @Override
    //listing all customers
    public ResponseEntity<List<Customer>>getCustomerInfo() {

        List<Customer> customerInfo = customerRepository.findAll();

        return new ResponseEntity<>(customerInfo, HttpStatus.FOUND);
    }
}
