package com.jia.mini.loan.predictor.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jia.mini.loan.predictor.enums.LoanType;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@Table(name="loans")
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double principalAmount;
    private double interest;
    private double serviceFee;
    private double totalRepayable;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Customer customer;
    private LoanType loanType;
    private String disbursementDate;
    private int loanDuration;

}
