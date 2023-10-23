package com.jia.mini.loan.predictor.controller;

import com.jia.mini.loan.predictor.dto.GeneralResponse;
import com.jia.mini.loan.predictor.entities.ProjectedFee;
import com.jia.mini.loan.predictor.service.ProjectedFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/api/projected/fees/")
public class ProjectedFeeController {
   @Autowired
   ProjectedFeeService projectedFeeService;

   //Getting Projected fee for a particular loan
   @GetMapping("list")
   public ResponseEntity<GeneralResponse> getProjectedFee(@RequestParam Long id){
       return projectedFeeService.getProjectedFee(id);
   }


}
