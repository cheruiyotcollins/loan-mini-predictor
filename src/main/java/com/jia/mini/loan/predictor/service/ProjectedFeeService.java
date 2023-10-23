package com.jia.mini.loan.predictor.service;

import com.jia.mini.loan.predictor.dto.ResponseDto;
import com.jia.mini.loan.predictor.dto.ProjectedFeesStoreRequest;
import org.springframework.http.ResponseEntity;

public interface ProjectedFeeService{
    public void storeWeeklyProjectedFees(ProjectedFeesStoreRequest projectedFeesStoreRequest);
    public void storeMonthlyProjectedFees(ProjectedFeesStoreRequest projectedFeesStoreRequest);

    public ResponseEntity<ResponseDto> getProjectedFee(Long id);

}
