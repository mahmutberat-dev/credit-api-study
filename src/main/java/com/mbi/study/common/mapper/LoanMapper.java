package com.mbi.study.common.mapper;

import com.mbi.study.controller.dto.LoanResponse;
import com.mbi.study.repository.entity.Loan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    public LoanResponse toLoanResponse(Loan dto);
}
