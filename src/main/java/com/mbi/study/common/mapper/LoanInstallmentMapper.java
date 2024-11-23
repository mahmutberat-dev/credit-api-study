package com.mbi.study.common.mapper;

import com.mbi.study.controller.dto.LoanInstallmentResponse;
import com.mbi.study.repository.entity.LoanInstallment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, LoanMapper.class})
public interface LoanInstallmentMapper {

    @Mapping(target = "loanId", source = "entity.loan.id")
    @Mapping(target = "nextPaymentDate", source = "entity.dueDate")
    LoanInstallmentResponse fromLoanInstallment(LoanInstallment entity);
}
