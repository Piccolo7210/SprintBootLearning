// src/main/java/com/example/user/Config/DataInitializer.java
package com.example.user.Config;

import com.example.user.Models.Users;
import com.example.user.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        String adminEmail = "admin@gmail.com";
        if (!userRepository.findByEmail(adminEmail).isPresent()) {
            Users admin = Users.builder()
                    .name("admin1")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123"))
                    .role("ADMIN")
                    .activated(true)
                    .build();
            userRepository.save(admin);
        }
    }
}
