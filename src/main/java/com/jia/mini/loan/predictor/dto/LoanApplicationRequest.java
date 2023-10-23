package com.jia.mini.loan.predictor.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jia.mini.loan.predictor.entities.Customer;
import com.jia.mini.loan.predictor.enums.LoanType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
public class LoanApplicationRequest {
    private double principalAmount;
   private int loanDuration;
    private Long  customerId;
    private String loanType;
}
