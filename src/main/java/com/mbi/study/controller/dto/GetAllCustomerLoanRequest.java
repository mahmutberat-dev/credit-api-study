package com.mbi.study.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetAllCustomerLoanRequest {
    @NotNull
    private Long customerId;
    private int numberOfInstallments;
    private boolean isPaid;
}
