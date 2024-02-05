package com.moyur.main;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @GetMapping("/debug")
    public String debug() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            return "인증된 사용자: " + auth.getName() + ", 권한: " + auth.getAuthorities();
        } else {
            return "인증되지 않음";
        }
    }
}