package com.jia.mini.loan.predictor.repository;

import com.jia.mini.loan.predictor.entities.Loan;
import com.jia.mini.loan.predictor.entities.ProjectedFee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
