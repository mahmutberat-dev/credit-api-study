package com.mbi.study.security;

import com.mbi.study.common.exception.AuthorizationTokenException;
import com.mbi.study.repository.entity.User;
import com.mbi.study.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorizationToken = request.getHeader("Authorization");
            log.debug("JWTFilter invoked for {}", request.getRequestURL());

            if (authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
                validateToken(authorizationToken);
            }
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }

        filterChain.doFilter(request, response);
    }

    private void validateToken(String authorizationToken) {
        final String jwt = authorizationToken.substring(7);
        final String userNameJWT = jwtUtil.extractUsername(jwt);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            User user = userService.getByUserName(userNameJWT);
            if (jwtUtil.isTokenValid(jwt, user)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(user.getRoleName())
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } else {
            throw new AuthorizationTokenException("Cannot validate user token");
        }
    }
}


