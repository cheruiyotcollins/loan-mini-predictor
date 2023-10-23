package com.jia.mini.loan.predictor.service.impl;

import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.dto.LoanApplicationRequest;
import com.jia.mini.loan.predictor.dto.ProjectedFeesStoreRequest;
import com.jia.mini.loan.predictor.entities.Loan;
import com.jia.mini.loan.predictor.enums.LoanType;
import com.jia.mini.loan.predictor.repository.CustomerRepository;
import com.jia.mini.loan.predictor.repository.LoanRepository;
import com.jia.mini.loan.predictor.service.CustomerService;
import com.jia.mini.loan.predictor.service.LoanService;
import com.jia.mini.loan.predictor.service.ProjectedFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanServiceImpl extends Thread implements LoanService  {
    @Value("${weekly-loan-interest}")
    double weeklyLoanInterest;
    @Value("${monthly-loan-interest}")
    double monthlyLoanInterest;
    @Value("${service-fee}")
    double  serviceFee;
    @Value("${weekly-cap}")
    int  weeklyCap;
    @Value("${monthly-cap}")
    int  monthlyCap;
    @Value("${monthly-service-fee-incur-frequency}")
    int  monthlyServiceFeeIncurFrequency;
    @Value("${weekly-service-fee-incur-frequency}")
    int  weeklyServiceFeeIncurFrequency;
    @Value("${weekly-loan}")
    String  weeklyLoan;
    @Value("${monthly-loan}")
    String  monthlyLoan;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProjectedFeeService projectedFeeService;

    GeneralResponse generalResponse;

    @Override
    @Transactional
    public ResponseEntity<GeneralResponse> addNewLoan(LoanApplicationRequest loanApplicationRequest) {
        generalResponse=new GeneralResponse();
        try {
            //processing weekly loan
            if (loanApplicationRequest.getLoanType().equalsIgnoreCase(weeklyLoan)) {

                int noOfWeeks = loanApplicationRequest.getLoanDuration() / 7;
                int i = 0;
                int j = 0;
                int startWeek = 1;
                double[] weeklyInterest = new double[noOfWeeks];
                double weeklyInterestSum = 0;
                double weeklyServiceFeeSum = 0;
                double[] weeklyServiceFeeArray = new double[Math.round((float) noOfWeeks / 2)];
                while (startWeek <= noOfWeeks) {
                    weeklyInterest[i] = loanApplicationRequest.getPrincipalAmount() * weeklyLoanInterest / 100;
                    weeklyInterestSum += loanApplicationRequest.getPrincipalAmount() * weeklyLoanInterest / 100;
                    i++;
                    if (startWeek % 2 == 0) {
                        weeklyServiceFeeArray[j] = loanApplicationRequest.getPrincipalAmount() * serviceFee / 100;
                        weeklyServiceFeeSum += loanApplicationRequest.getPrincipalAmount() * serviceFee / 100;
                        j++;
                    }
                    startWeek++;
                }
                Loan loan = Loan.builder()
                        .loanType(LoanType.WEEKLY)
                        .loanDuration(loanApplicationRequest.getLoanDuration())
                        .interest(weeklyInterestSum)
                        .disbursementDate(LocalDateTime.now().toString())
                        .principalAmount(loanApplicationRequest.getPrincipalAmount())
                        .serviceFee(weeklyServiceFeeSum)
                        .totalRepayable(loanApplicationRequest.getPrincipalAmount() + weeklyServiceFeeSum + weeklyInterestSum)
                        .customer(customerRepository.findById(loanApplicationRequest.getCustomerId()).get())
                        .build();
                Loan createdLoan = loanRepository.save(loan);

                ProjectedFeesStoreRequest projectedFeesStoreRequest = ProjectedFeesStoreRequest.builder()
                        .loanId(createdLoan.getId())
                        .projectedInterestAmount(weeklyInterest)
                        .projectedServiceFeeAmount(weeklyServiceFeeArray)
                        .build();
            //calling projectedFeeService Class to calculated and store projected fees
                projectedFeeService.storeWeeklyProjectedFees(projectedFeesStoreRequest);
                generalResponse.setStatus(HttpStatus.CREATED);
                generalResponse.setPayload(createdLoan);
                generalResponse.setDescription("Loan Created Successfully");
                return new ResponseEntity<>(generalResponse, generalResponse.getStatus());

            }
            //processing monthly loans
            else if (loanApplicationRequest.getLoanType().equalsIgnoreCase(monthlyLoan)) {
                int noOfMonths = loanApplicationRequest.getLoanDuration() / 30;
                int i = 0;
                int j = 0;
                int startMonth = 1;
                double[] monthlyInterest = new double[noOfMonths];
                double monthlyInterestSum = 0;
                double monthlyServiceFeeSum = 0;
                double[] monthlyServiceFeeArray = new double[Math.round((float) noOfMonths / 3)];
                while (startMonth <= noOfMonths) {
                    monthlyInterest[i] = loanApplicationRequest.getPrincipalAmount() * monthlyLoanInterest / 100;
                    monthlyInterestSum += loanApplicationRequest.getPrincipalAmount() * monthlyLoanInterest / 100;
                    i++;
                    if (startMonth % 3 == 0) {
                        monthlyServiceFeeArray[j] = loanApplicationRequest.getPrincipalAmount() * serviceFee / 100;
                        monthlyServiceFeeSum += loanApplicationRequest.getPrincipalAmount() * serviceFee / 100;
                        j++;
                    }
                    startMonth++;
                }
                Loan loan = Loan.builder()
                        .loanType(LoanType.MONTHLY)
                        .loanDuration(loanApplicationRequest.getLoanDuration())
                        .interest(monthlyInterestSum)
                        .disbursementDate(LocalDateTime.now().toString())
                        .principalAmount(loanApplicationRequest.getPrincipalAmount())
                        .serviceFee(monthlyServiceFeeSum)
                        .totalRepayable(loanApplicationRequest.getPrincipalAmount() + monthlyInterestSum + monthlyServiceFeeSum)
                        .customer(customerRepository.findById(loanApplicationRequest.getCustomerId()).get())
                        .build();
                Loan createdLoan = loanRepository.save(loan);

                ProjectedFeesStoreRequest projectedFeesStoreRequest = ProjectedFeesStoreRequest.builder()
                        .loanId(createdLoan.getId())
                        .projectedInterestAmount(monthlyInterest)
                        .projectedServiceFeeAmount(monthlyServiceFeeArray)
                        .build();
                 //calling projectedFeeService Class to calculated and store projected fees
                projectedFeeService.storeMonthlyProjectedFees(projectedFeesStoreRequest);
                generalResponse.setStatus(HttpStatus.CREATED);
                generalResponse.setPayload(createdLoan);
                generalResponse.setDescription("Loan Created Successfully");
                return new ResponseEntity<>(generalResponse, generalResponse.getStatus());
            } //return invalid loan type for any other loan type
            else {
                generalResponse.setStatus(HttpStatus.BAD_REQUEST);
                generalResponse.setDescription("Invalid Loan Type");
                return new ResponseEntity<>(generalResponse, generalResponse.getStatus());
            }
        }catch (Exception e){
            generalResponse.setDescription("Customer with provided id not found");
            generalResponse.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(generalResponse,generalResponse.getStatus());
        }
    }


    @Override
    //listing all existing loans
    public ResponseEntity<List<Loan>> getLoans() {
        return new ResponseEntity<>(loanRepository.findAll(), HttpStatus.CREATED);
    }
}
