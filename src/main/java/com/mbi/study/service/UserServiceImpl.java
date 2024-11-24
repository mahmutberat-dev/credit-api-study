package com.mbi.study.service;

import com.mbi.study.controller.dto.RegistrationRequest;
import com.mbi.study.repository.UserRepository;
import com.mbi.study.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.mbi.study.common.UserRoleEnum.ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void register(RegistrationRequest registrationRequest) {
        if (!ADMIN.equals(registrationRequest.customerRole())) {
            throw new IllegalArgumentException("The register API can only create ADMIN users!");
        }

        User user = User.builder()
                .roleName(ADMIN)
                .username(registrationRequest.name())
                .password(registrationRequest.password())
                .build();

        userRepository.save(user);
    }
}
