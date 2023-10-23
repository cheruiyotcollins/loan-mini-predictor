package com.jia.mini.loan.predictor.controller;

import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.entities.LoanInstallment;
import com.jia.mini.loan.predictor.service.LoanInstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping(value="/api/loans/installments")
public class LoanInstallmentController {
    @Autowired
    LoanInstallmentService loanInstallmentService;

     @GetMapping
    public ResponseEntity<GeneralResponse>  getLoanInstallmentByLoanId(@RequestParam Long id){

        return loanInstallmentService.getLoanInstallmentByLoanId(id);
    }
}
