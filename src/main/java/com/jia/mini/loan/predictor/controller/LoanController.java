package com.jia.mini.loan.predictor.controller;

import com.jia.mini.loan.predictor.dto.ResponseDto;
import com.jia.mini.loan.predictor.dto.LoanApplicationRequest;
import com.jia.mini.loan.predictor.entities.Loan;
import com.jia.mini.loan.predictor.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/jia/loans/")
public class LoanController {
    @Autowired
    LoanService loanService;
   //adding new loans
    @PostMapping("application")
        public ResponseEntity<ResponseDto> loanApplication(@RequestBody LoanApplicationRequest loanApplicationRequest){
            return loanService.addNewLoan(loanApplicationRequest);
        }
        //retrieving all existing loans
        @GetMapping("list")
        public ResponseEntity<List<Loan>> getLoans(){
        return loanService.getLoans();
        }
}
