package com.mbi.study.service;

import com.mbi.study.common.exception.AuthenticationException;
import com.mbi.study.controller.dto.LoginRequest;
import com.mbi.study.controller.dto.LoginResponse;
import com.mbi.study.controller.dto.RegistrationRequest;
import com.mbi.study.repository.UserRepository;
import com.mbi.study.repository.entity.User;
import com.mbi.study.security.JWTUtil;
import jakarta.persistence.EntityNotFoundException;
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
        User user = User.builder()
                .roleName(ADMIN)
                .username(registrationRequest.name())
                .password(registrationRequest.password())
                .build();

        userRepository.save(user);
    }

    @Override
    public User findByUserId(String userId) {
        return userRepository.findById(Long.valueOf(userId)).orElseThrow(EntityNotFoundException::new);
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

}
