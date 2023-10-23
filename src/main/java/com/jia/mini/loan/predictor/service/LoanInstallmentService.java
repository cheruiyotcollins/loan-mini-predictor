package com.jia.mini.loan.predictor.service;

import com.jia.mini.loan.predictor.dto.ResponseDto;
import com.jia.mini.loan.predictor.dto.LoanInstallmentStoreRequest;
import org.springframework.http.ResponseEntity;

public interface LoanInstallmentService {
    public void storeLoanInstallment(LoanInstallmentStoreRequest loanInstallmentStoreRequest);
    public ResponseEntity<ResponseDto>  getLoanInstallmentByLoanId(Long id);
}
