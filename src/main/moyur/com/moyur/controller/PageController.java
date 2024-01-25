package com.moyur.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String mainPage() {
        return "mainpage"; // 메인 페이지 템플릿 이름
    }

    @GetMapping("/recommend")
    public String recommendPage() {
        // 추천 페이지 로직
        return "recommend"; // 추천 페이지 템플릿 이름
    }
    
    @GetMapping("/login")
    public String loginPage() {
        // 로그인 페이지 로직
        return "login"; // 로그인 페이지 템플릿 이름
    }

    @GetMapping("/join")
    public String joinPage() {
        // 회원 가입 페이지 로직
        return "join"; // 회원 가입 페이지 템플릿 이름
    }

    // 다른 페이지에 대한 핸들러도 추가 가능
}
