package com.moyur.join;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class JoinController {
    
    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        
        this.joinService = joinService;
    }
    
    @PostMapping("/join")
    public String join(@ModelAttribute JoinDTO joinDTO, RedirectAttributes redirectAttributes) {
        try {
            joinService.joinProcess(joinDTO);
        } catch (RuntimeException e) { 
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/join";
        }
        return "redirect:/login"; 
    }

}