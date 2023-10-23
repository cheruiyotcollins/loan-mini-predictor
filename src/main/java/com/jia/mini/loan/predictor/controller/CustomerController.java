package com.jia.mini.loan.predictor.controller;

import com.jia.mini.loan.predictor.dto.CreateCustomerRequest;
import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.entities.Customer;
import com.jia.mini.loan.predictor.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/customer/")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    //adding new customer
    @PostMapping("add")
    public ResponseEntity<GeneralResponse> addCustomer(@RequestBody CreateCustomerRequest createCustomerRequest){

      return customerService.addCustomer(createCustomerRequest);
    }
    //retrieving all existing customers
    @GetMapping("list")
    public ResponseEntity<List<Customer>> getCustomerInfoList(){
        return customerService.getCustomerInfo();

    }
}
