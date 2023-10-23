package com.jia.mini.loan.predictor.repository;

import com.jia.mini.loan.predictor.entities.Loan;
import com.jia.mini.loan.predictor.entities.ProjectedFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectedFeeRepository extends JpaRepository<ProjectedFee, Long> {
    Optional<List<ProjectedFee>> findByLoan(Loan loan);
}
