package com.jia.mini.loan.predictor.service.impl;

import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.dto.LoanInstallmentStoreRequest;
import com.jia.mini.loan.predictor.entities.Loan;
import com.jia.mini.loan.predictor.entities.LoanInstallment;
import com.jia.mini.loan.predictor.repository.LoanInstallmentRepository;
import com.jia.mini.loan.predictor.repository.LoanRepository;
import com.jia.mini.loan.predictor.service.LoanInstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanInstallmentServiceImpl implements LoanInstallmentService {
    @Autowired
    LoanInstallmentRepository loanInstallmentRepository;
    @Autowired
    LoanRepository loanRepository;

    GeneralResponse generalResponse;

    @Override
    // Storing payable loan installments
    public void storeLoanInstallment(LoanInstallmentStoreRequest loanInstallmentStoreRequest){
        int len=loanInstallmentStoreRequest.getInstallmentDate().length;
        for(int i=0;i<len;i++){
            LoanInstallment loanInstallment= LoanInstallment.builder()
                    .installmentDate(loanInstallmentStoreRequest.getInstallmentDate()[i])
                    .installmentAmount(loanInstallmentStoreRequest.getInstallmentAmount()[i])
                    .loan(loanInstallmentStoreRequest.getLoan())
                   .build();
            loanInstallmentRepository.save(loanInstallment);
        }

    }

    @Override
    //getting payable loan installment fee by loan id
    public ResponseEntity<GeneralResponse> getLoanInstallmentByLoanId(Long id) {
        generalResponse=new GeneralResponse();
        Optional<Loan> loan=loanRepository.findById(id);
        if(loan.isPresent()) {
            List<LoanInstallment> loanInstallments = loanInstallmentRepository.findByLoan(loanRepository.findById(id).get()).get();
            generalResponse.setPayload(loanInstallments);
            generalResponse.setStatus(HttpStatus.FOUND);
            generalResponse.setDescription("Loan Installment List");
            return new ResponseEntity<>(generalResponse, generalResponse.getStatus());

        }else {
            generalResponse.setStatus(HttpStatus.OK);
            generalResponse.setDescription("Loan with provided id not found");
            return new ResponseEntity<>(generalResponse, generalResponse.getStatus());

        }
    }

}
