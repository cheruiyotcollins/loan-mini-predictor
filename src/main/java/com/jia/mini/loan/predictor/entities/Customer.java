package com.jia.mini.loan.predictor.entities;

import com.jia.mini.loan.predictor.enums.CustomerCreditStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String customerMobileNo;
    private String email;
    private CustomerCreditStatus customerStatus;
}
