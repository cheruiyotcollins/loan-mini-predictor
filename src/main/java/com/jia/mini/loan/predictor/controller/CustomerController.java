package com.jia.mini.loan.predictor.controller;

import com.jia.mini.loan.predictor.dto.CreateCustomerRequest;
import com.jia.mini.loan.predictor.dto.ResponseDto;
import com.jia.mini.loan.predictor.entities.Customer;
import com.jia.mini.loan.predictor.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/jia/customers/")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    //adding new customer
    @PostMapping("register")
    public ResponseEntity<ResponseDto> addCustomer(@RequestBody CreateCustomerRequest createCustomerRequest){

      return customerService.addCustomer(createCustomerRequest);
    }
    //retrieving all existing customers
    @GetMapping("list")
    public ResponseEntity<List<Customer>> getCustomerInfoList(){
        return customerService.getCustomerInfo();

    }
}
