package com.jia.mini.loan.predictor.dto;

import com.jia.mini.loan.predictor.entities.Loan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectedFeesStoreRequest {
    private Long loanId;
    private double[] projectedInterestAmount;
    private double[] projectedServiceFeeAmount;
}
