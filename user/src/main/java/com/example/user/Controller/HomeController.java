package com.example.user.Controller;

import com.example.user.Service.UserService;
import com.example.user.Models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/activate")
    public String activateAccount(@RequestParam String token, Model model) {
        try {
            userService.activateUser(token);
            model.addAttribute("message", "Account activated successfully! You can now log in.");
            model.addAttribute("messageType", "success");
        } catch (Exception e) {
            model.addAttribute("message", "Activation failed: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        return "activation-result";
    }
    @GetMapping("/admin-home")
    public String showAdminHomePage() {
        return "admin-home";
    }

    @GetMapping("/{userId}")
    public String showUserHomePage(@PathVariable Long userId, Model model) {
        Users user = userService.findByUserId(userId);
        model.addAttribute("user", user);
        return "User/home";
    }
    @GetMapping("/admin/{userId}")
    public String showAdminHomePage(@PathVariable Long userId, Model model) {
        Users user = userService.findByUserId(userId);
        model.addAttribute("user", user);
        return "Admin/home";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }
}