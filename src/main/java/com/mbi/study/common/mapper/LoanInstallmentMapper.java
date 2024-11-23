package com.mbi.study.common.mapper;

import com.mbi.study.controller.dto.LoanInstallmentResponse;
import com.mbi.study.repository.entity.LoanInstallment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanInstallmentMapper {
    LoanInstallmentResponse fromLoanInstallment(LoanInstallment dto);
}
