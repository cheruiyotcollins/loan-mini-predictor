package com.jia.mini.loan.predictor.controller;

import com.jia.mini.loan.predictor.dto.ResponseDto;
import com.jia.mini.loan.predictor.service.LoanInstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/loans/installments")
public class LoanInstallmentController {
    @Autowired
    LoanInstallmentService loanInstallmentService;

    //getting LoanInstallment for a particular loan
     @GetMapping
    public ResponseEntity<ResponseDto>  getLoanInstallmentByLoanId(@RequestParam Long id){

        return loanInstallmentService.getLoanInstallmentByLoanId(id);
    }
}
