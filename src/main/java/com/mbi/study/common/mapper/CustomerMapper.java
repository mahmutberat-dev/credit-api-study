package com.mbi.study.common.mapper;

import com.mbi.study.controller.dto.CustomerResponse;
import com.mbi.study.controller.dto.LoanResponse;
import com.mbi.study.repository.entity.Customer;
import com.mbi.study.repository.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toLoanResponse(Customer entity);
}
