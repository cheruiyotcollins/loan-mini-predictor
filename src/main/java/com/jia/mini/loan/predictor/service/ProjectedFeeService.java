package com.jia.mini.loan.predictor.service;

import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.dto.ProjectedFeesStoreRequest;
import com.jia.mini.loan.predictor.entities.ProjectedFee;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectedFeeService{
    public void storeWeeklyProjectedFees(ProjectedFeesStoreRequest projectedFeesStoreRequest);
    public void storeMonthlyProjectedFees(ProjectedFeesStoreRequest projectedFeesStoreRequest);

    public ResponseEntity<GeneralResponse> getProjectedFee(Long id);

}
