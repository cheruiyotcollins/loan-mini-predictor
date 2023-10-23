package com.jia.mini.loan.predictor.service;

import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.dto.LoanApplicationRequest;
import com.jia.mini.loan.predictor.entities.Loan;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoanService {
    public ResponseEntity<GeneralResponse> addNewLoan(LoanApplicationRequest loanApplicationRequest);
    public ResponseEntity<List<Loan>> getLoans();
}
