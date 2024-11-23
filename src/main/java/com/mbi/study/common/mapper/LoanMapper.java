package com.mbi.study.common.mapper;

import com.mbi.study.controller.dto.LoanResponse;
import com.mbi.study.repository.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, LoanInstallmentMapper.class})
public interface LoanMapper {

    @Mapping(target = "loanId", source = "entity.id")
    @Mapping(target = "customerId", source = "entity.customer.id")
    LoanResponse toLoanResponse(Loan entity);

}
