package com.mbi.study.service;

import com.mbi.study.controller.dto.RegistrationRequest;
import com.mbi.study.repository.entity.User;

public interface UserService {
    void register(RegistrationRequest registrationRequest);

    User findByUserId(String userId);
    User findByUserName(String username);
}