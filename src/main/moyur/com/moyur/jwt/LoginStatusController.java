package com.moyur.jwt;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class LoginStatusController {
    @GetMapping("/check")
    public ResponseEntity<?> checkLoginStatus(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization") && !cookie.getValue().isEmpty()) {
                    return ResponseEntity.ok(Collections.singletonMap("status", "OK"));  // 로그인 상태임을 응답
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("status", "UNAUTHORIZED"));  // 로그인 상태가 아님을 응답
    }
}