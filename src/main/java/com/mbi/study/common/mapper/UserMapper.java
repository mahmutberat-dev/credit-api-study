package com.mbi.study.common.mapper;

import com.mbi.study.controller.dto.UserResponse;
import com.mbi.study.repository.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User entity);
}
