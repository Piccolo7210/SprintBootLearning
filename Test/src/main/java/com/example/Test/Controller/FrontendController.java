package com.example.Test.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/frontend/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/management")
    public String management() {
        return "management";
    }

    @GetMapping("/statistics")
    public String statistics() {
        return "statistics";
    }
}
