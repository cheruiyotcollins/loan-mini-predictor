package com.jia.mini.loan.predictor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class GeneralResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object payload;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HttpStatus status;
}
