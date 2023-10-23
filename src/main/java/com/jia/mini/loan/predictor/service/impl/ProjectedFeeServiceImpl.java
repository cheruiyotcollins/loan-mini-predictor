package com.jia.mini.loan.predictor.service.impl;

import com.jia.mini.loan.predictor.dto.ResponseDto;
import com.jia.mini.loan.predictor.dto.LoanInstallmentStoreRequest;
import com.jia.mini.loan.predictor.dto.ProjectedFeesStoreRequest;
import com.jia.mini.loan.predictor.entities.Loan;
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

    ResponseDto responseDto;


    @Override
    //weekly loans projected fees calculation and storage
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
        loanInstallmentMapper(projectedFeesStoreRequest);


    }

    @Override
    //monthly loans projected fees calculation and storage
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
        loanInstallmentMapper(projectedFeesStoreRequest);
    }
    //custom method for setting up LoanInstallmentDto and calling LoanInstallmentService
    private void loanInstallmentMapper(ProjectedFeesStoreRequest projectedFeesStoreRequest){
        List<ProjectedFee> projectedFeeList= projectedFeeRepository.findByLoan(loanRepository.findById(projectedFeesStoreRequest.getLoanId()).get()).get();
        int projectedFeeLength=projectedFeeList.size();
        String[]  installmentDueDate= new String[projectedFeeLength];
        double[]  installmentAmount= new double[projectedFeeLength];
        Loan loan=new Loan();
        for(int i=0;i<projectedFeeLength;i++){
            installmentDueDate[i]=projectedFeeList.get(i).getProjectedFeeIncurDate();
            loan= loanRepository.findById(projectedFeesStoreRequest.getLoanId()).get();
            double installment=loan.getPrincipalAmount()/(projectedFeeLength);
            installment+=projectedFeeList.get(i).getProjectedServiceFeeAmount()+projectedFeeList.get(i).getProjectedInterestAmount();
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
    //getting projected fee by loan id
    public ResponseEntity<ResponseDto> getProjectedFee(Long id) {
        responseDto = new ResponseDto();
        Optional<Loan> loan=loanRepository.findById(id);
        if(loan.isPresent()) {
            List<ProjectedFee> projectedFeeList = projectedFeeRepository.findByLoan(loan.get()).get();
            responseDto.setPayload(projectedFeeList);
            responseDto.setStatus(HttpStatus.FOUND);
            responseDto.setDescription("Projected Fee List");
            return new ResponseEntity<>(responseDto, responseDto.getStatus());
        }else{
            responseDto.setStatus(HttpStatus.OK);
            responseDto.setDescription("Loan with provided id not found");
            return  new ResponseEntity<>(responseDto, responseDto.getStatus());
        }
    }
}
