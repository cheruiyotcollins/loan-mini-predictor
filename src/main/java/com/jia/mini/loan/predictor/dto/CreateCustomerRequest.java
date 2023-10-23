package com.jia.mini.loan.predictor.dto;

import com.jia.mini.loan.predictor.enums.CustomerCreditStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerRequest {
    private String customerName;
    private String customerMobileNo;
    private String email;
    private String customerStatus;
}
