package com.jia.mini.loan.predictor.repository;

import com.jia.mini.loan.predictor.entities.Customer;
import com.jia.mini.loan.predictor.entities.ProjectedFee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
