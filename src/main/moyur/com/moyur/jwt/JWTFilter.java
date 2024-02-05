package com.moyur.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // 인증이 필요하지 않은 경로에 대한 예외 처리
        if (!isAuthRequired(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 인증 로직
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && !jwtUtil.isExpired(token)) {
            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setRole(role);

            CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            // 인증이 실패하거나 토큰이 만료된 경우 처리
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthRequired(String requestURI) {
        return !requestURI.startsWith("/public/") &&
               !requestURI.startsWith("/login") &&
               !requestURI.startsWith("/join");
    }
}
