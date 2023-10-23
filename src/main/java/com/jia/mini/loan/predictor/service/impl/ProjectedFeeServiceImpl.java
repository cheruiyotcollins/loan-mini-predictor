package com.jia.mini.loan.predictor.service.impl;

import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.dto.LoanInstallmentStoreRequest;
import com.jia.mini.loan.predictor.dto.ProjectedFeesStoreRequest;
import com.jia.mini.loan.predictor.entities.Loan;
import com.jia.mini.loan.predictor.entities.LoanInstallment;
import com.jia.mini.loan.predictor.entities.ProjectedFee;
import com.jia.mini.loan.predictor.repository.LoanRepository;
import com.jia.mini.loan.predictor.repository.ProjectedFeeRepository;
import com.jia.mini.loan.predictor.service.LoanInstallmentService;
import com.jia.mini.loan.predictor.service.ProjectedFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectedFeeServiceImpl implements ProjectedFeeService {
    @Autowired
    ProjectedFeeRepository projectedFeeRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    LoanInstallmentService loanInstallmentService;

    GeneralResponse generalResponse;


    @Override
    public void storeWeeklyProjectedFees(ProjectedFeesStoreRequest projectedFeesStoreRequest) {
        int len= projectedFeesStoreRequest.getProjectedInterestAmount().length;
        int incurDays=0;
        int j=0;
        for(int i=0;i<len;i++){
            incurDays+=7;
            ProjectedFee projectedFee= ProjectedFee.builder()
                    .projectedInterestAmount(projectedFeesStoreRequest.getProjectedInterestAmount()[i])
                    .projectedFeeIncurDate(LocalDateTime.now().plusDays(incurDays).toString())
                    .loan(loanRepository.findById(projectedFeesStoreRequest.getLoanId()).get())
                    .projectedServiceFeeAmount(0)
                    .build();
            if(incurDays%14==0){

                projectedFee.setProjectedServiceFeeAmount(projectedFeesStoreRequest.getProjectedServiceFeeAmount()[j]);
                j++;
            }
            projectedFeeRepository.save(projectedFee);

        }
        loanInstallmentMapper(projectedFeesStoreRequest,  7);


    }

    @Override
    public void storeMonthlyProjectedFees(ProjectedFeesStoreRequest projectedFeesStoreRequest) {
        int len= projectedFeesStoreRequest.getProjectedInterestAmount().length;
        int incurDays=0;
        int j=0;
        for(int i=0;i<len;i++){
            incurDays+=30;
            ProjectedFee projectedFee= ProjectedFee.builder()
                    .projectedInterestAmount(projectedFeesStoreRequest.getProjectedInterestAmount()[i])
                    .projectedFeeIncurDate(LocalDateTime.now().plusDays(incurDays).toString())
                    .loan(loanRepository.findById(projectedFeesStoreRequest.getLoanId()).get())
                    .projectedServiceFeeAmount(0)
                    .build();
            if(incurDays%90==0){

                projectedFee.setProjectedServiceFeeAmount(projectedFeesStoreRequest.getProjectedServiceFeeAmount()[j]);
                j++;
            }
            projectedFeeRepository.save(projectedFee);

        }
        loanInstallmentMapper(projectedFeesStoreRequest,  30);
    }

    private void loanInstallmentMapper(ProjectedFeesStoreRequest projectedFeesStoreRequest,int incurFrequency){
        List<ProjectedFee> projectedFeeList= projectedFeeRepository.findByLoan(loanRepository.findById(projectedFeesStoreRequest.getLoanId()).get()).get();
        int projectedFeeLength=projectedFeeList.size();
        System.out.println(projectedFeeLength);
        String[]  installmentDueDate= new String[projectedFeeLength];
        double[]  installmentAmount= new double[projectedFeeLength];
        Loan loan=new Loan();
        for(int i=0;i<projectedFeeLength;i++){
            installmentDueDate[i]=projectedFeeList.get(i).getProjectedFeeIncurDate();
            loan= loanRepository.findById(projectedFeesStoreRequest.getLoanId()).get();
            double installment=loan.getPrincipalAmount()/(projectedFeeLength);
            System.out.println("principalAmount "+loan.getPrincipalAmount());
            System.out.println(":::::::::::"+(double) loan.getLoanDuration() /incurFrequency);
            installment+=projectedFeeList.get(i).getProjectedServiceFeeAmount()+projectedFeeList.get(i).getProjectedInterestAmount();
            System.out.println(projectedFeeList.get(i).getProjectedServiceFeeAmount()+projectedFeeList.get(i).getProjectedInterestAmount());
            installmentAmount[i]=installment;
        }
        LoanInstallmentStoreRequest loanInstallment=LoanInstallmentStoreRequest.builder()
                .installmentDate(installmentDueDate)
                .loan(loan)
                .installmentAmount(installmentAmount)
                .build();
        loanInstallmentService.storeLoanInstallment(loanInstallment);

    }

    @Override
    public ResponseEntity<GeneralResponse> getProjectedFee(Long id) {
        generalResponse= new GeneralResponse();
        Optional<Loan> loan=loanRepository.findById(id);
        if(loan.isPresent()) {
            List<ProjectedFee> projectedFeeList = projectedFeeRepository.findByLoan(loan.get()).get();
            generalResponse.setPayload(projectedFeeList);
            generalResponse.setStatus(HttpStatus.FOUND);
            generalResponse.setDescription("Projected Fee List");
            return new ResponseEntity<>(generalResponse, generalResponse.getStatus());
        }else{
            generalResponse.setStatus(HttpStatus.OK);
            generalResponse.setDescription("Loan with provided id not found");
            return  new ResponseEntity<>(generalResponse, generalResponse.getStatus());
        }
    }
}
