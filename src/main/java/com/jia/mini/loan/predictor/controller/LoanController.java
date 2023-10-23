package com.jia.mini.loan.predictor.controller;

import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.dto.LoanApplicationRequest;
import com.jia.mini.loan.predictor.entities.Loan;
import com.jia.mini.loan.predictor.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/loans/")
public class LoanController {
    @Autowired
    LoanService loanService;

    @PostMapping("new")
        public ResponseEntity<GeneralResponse> loanApplication(@RequestBody LoanApplicationRequest loanApplicationRequest){
            return loanService.addNewLoan(loanApplicationRequest);
        }
        @GetMapping("list")
        public ResponseEntity<List<Loan>> getLoans(){
        return loanService.getLoans();
        }
}
