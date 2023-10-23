package com.jia.mini.loan.predictor.service;

import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.dto.LoanInstallmentStoreRequest;
import com.jia.mini.loan.predictor.entities.LoanInstallment;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoanInstallmentService {
    public void storeLoanInstallment(LoanInstallmentStoreRequest loanInstallmentStoreRequest);
    public ResponseEntity<GeneralResponse>  getLoanInstallmentByLoanId(Long id);
}
