package com.jia.mini.loan.predictor.service.impl;

import com.jia.mini.loan.predictor.dto.ResponseDto;
import com.jia.mini.loan.predictor.dto.LoanApplicationRequest;
import com.jia.mini.loan.predictor.dto.ProjectedFeesStoreRequest;
import com.jia.mini.loan.predictor.entities.Loan;
import com.jia.mini.loan.predictor.enums.LoanType;
import com.jia.mini.loan.predictor.repository.CustomerRepository;
import com.jia.mini.loan.predictor.repository.LoanRepository;
import com.jia.mini.loan.predictor.service.LoanService;
import com.jia.mini.loan.predictor.service.ProjectedFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    ResponseDto responseDto;

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> addNewLoan(LoanApplicationRequest loanApplicationRequest) {
        responseDto =new ResponseDto();
        try {
            //processing weekly loan
            int i = 0;
            int j = 0;
            if (loanApplicationRequest.getLoanType().equalsIgnoreCase(weeklyLoan)) {

                int noOfWeeks = loanApplicationRequest.getLoanDuration() / 7;
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
                Loan newLoan = loanMapper(loanApplicationRequest,weeklyInterestSum,weeklyServiceFeeSum,LoanType.WEEKLY);

                return projectedFeeMapper(newLoan,weeklyInterest,weeklyServiceFeeArray);
            }
            //processing monthly loans
            else if (loanApplicationRequest.getLoanType().equalsIgnoreCase(monthlyLoan)) {
                int noOfMonths = loanApplicationRequest.getLoanDuration() / 30;
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
                Loan newLoan = loanMapper(loanApplicationRequest,monthlyInterestSum,monthlyServiceFeeSum,LoanType.MONTHLY);
                return projectedFeeMapper(newLoan,monthlyInterest,monthlyServiceFeeArray);
            }
            //return invalid loan type for any other loan type
            else {
                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                responseDto.setDescription("Invalid Loan Type");
                return new ResponseEntity<>(responseDto, responseDto.getStatus());
            }
        }catch (Exception e){
            responseDto.setDescription("Customer with provided id not found");
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(responseDto, responseDto.getStatus());
        }
    }

    private Loan loanMapper(LoanApplicationRequest loanApplicationRequest, double interestSum, double serviceFeeSum, LoanType loanType){
        Loan loan = Loan.builder()
                .loanType(loanType)
                .loanDuration(loanApplicationRequest.getLoanDuration())
                .interest(interestSum)
                .disbursementDate(LocalDateTime.now().toString())
                .principalAmount(loanApplicationRequest.getPrincipalAmount())
                .serviceFee(serviceFeeSum)
                .totalRepayable(loanApplicationRequest.getPrincipalAmount() + interestSum + serviceFeeSum)
                .customer(customerRepository.findById(loanApplicationRequest.getCustomerId()).get())
                .build();
       return loanRepository.save(loan);
    }

    private ResponseEntity<ResponseDto> projectedFeeMapper(Loan loan, double[] interest, double[] serviceFee){

        ProjectedFeesStoreRequest projectedFeesStoreRequest = ProjectedFeesStoreRequest.builder()
                .loanId(loan.getId())
                .projectedInterestAmount(interest)
                .projectedServiceFeeAmount(serviceFee)
                .build();
        //calling projectedFeeService Class to calculated and store projected fees
        projectedFeeService.storeMonthlyProjectedFees(projectedFeesStoreRequest);
        responseDto.setStatus(HttpStatus.CREATED);
        responseDto.setPayload(loan);
        responseDto.setDescription("Loan Created Successfully");
        return new ResponseEntity<>(responseDto, responseDto.getStatus());

    }


    @Override
    //listing all existing loans
    public ResponseEntity<List<Loan>> getLoans() {
        return new ResponseEntity<>(loanRepository.findAll(), HttpStatus.CREATED);
    }
}
