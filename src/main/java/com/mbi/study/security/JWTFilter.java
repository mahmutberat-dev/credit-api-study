package com.mbi.study.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorizationToken = request.getHeader("Authorization");
            log.info("JWTFilter invoked for {}", request.getRequestURL());
            if (authorizationToken == null || !authorizationToken.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            validateToken(authorizationToken);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }

        filterChain.doFilter(request, response);
    }

    private void validateToken(String authorizationToken) {
        final String jwt = authorizationToken.substring(7);
        final String userEmail = "jwtService.extractUsername(jwt)";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        if (true) {
//            throw new AuthorizationTokenException("");
//        }

        if (userEmail != null && authentication == null) {
//                                UserDetails userDetails = this.userService.loadUserByUsername(userEmail);
//
//                                if (jwtService.isTokenValid(jwt, userDetails)) {
//                                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                                            userDetails,
//                                            null,
//                                            userDetails.getAuthorities()
//                                    );
//
//                                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                                }
        }
    }
}


