package com.example.Test.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
    
    @GetMapping("/users")
    public String usersPage() {
        return "users/interactive";
    }
    
    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }
}
