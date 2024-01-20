package com.moyur.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class LogoutController {

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // 쿠키 삭제
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 쿠키 만료
        response.addCookie(cookie);

        // 필요한 경우 세션 무효화
        // request.getSession().invalidate();

        return ResponseEntity.ok().build();
    }
}
