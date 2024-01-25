package com.moyur.cafe;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recommend")
public class CafeController {

    private final CafeService cafeService;

    public CafeController(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @GetMapping("/cafe")
    public String getAllCafes(Model model) {
        List<CafeDTO> cafes = cafeService.getAllCafes();
        model.addAttribute("cafes", cafes);
        return "cafe";
    }
}

