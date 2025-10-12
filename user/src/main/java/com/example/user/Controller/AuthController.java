package com.example.user.Controller;

import com.example.user.Service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestParam @NotBlank(message = "Name is required") String name,
            @RequestParam @NotBlank @Email(message = "Email is required") String email,
            @RequestParam @NotBlank(message = "Password is required") String password) {
        userService.registerUser(name, email, password);
        return ResponseEntity.ok("Registration successful. Check your email for activation.");
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam String token) {
        userService.activateUser(token);
        return ResponseEntity.ok("Account activated successfully. Please login.");
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<String> resetPasswordRequest(@RequestParam @Email String email) {
        userService.resetPasswordRequest(email);
        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully.");
    }
}