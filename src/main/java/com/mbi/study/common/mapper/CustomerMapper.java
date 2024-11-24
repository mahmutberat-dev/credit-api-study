package com.mbi.study.common.mapper;

import com.mbi.study.controller.dto.CustomerResponse;
import com.mbi.study.repository.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toLoanResponse(User entity);
}
