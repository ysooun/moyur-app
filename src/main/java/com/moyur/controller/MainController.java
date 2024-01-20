package com.moyur.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String header() {
    	return "header";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }
    
    @GetMapping("/join")
    public String joinPage() {
        return "join";  
    }
}
