package com.example.user.Service;

import com.example.user.Models.Users;
import com.example.user.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;
    @Value("${app.base-url}")
    private String baseUrl;

    public Users registerUser(String name, String email, String password) {
        Users user = Users.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role("USER")
                .activationToken(UUID.randomUUID().toString())
                .build();

        Users savedUser = userRepository.save(user);
        sendActivationEmail(savedUser);
        return savedUser;
    }

    public void activateUser(String token) {
        Users user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid activation token"));
        user.setActivated(true);
        user.setActivationToken(null);
        userRepository.save(user);
    }

    public void resetPasswordRequest(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user);
        sendResetEmail(user);
    }

    public void resetPassword(String token, String newPassword) {
        Users user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
    }

    private void sendActivationEmail(Users user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Account Activation");
//        message.setText("Click the link to activate your account: http://localhost:8080/activate?token=" + user.getActivationToken());
//        mailSender.send(message);
        String activationLink = baseUrl + "/activate?token=" + user.getActivationToken();
        message.setText("Click the link to activate your account: " + activationLink);
        mailSender.send(message);
    }

    private void sendResetEmail(Users user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset");
        String resetLink = baseUrl + "/reset?token=" + user.getResetToken();
        message.setText("Click the link to reset your password: " + resetLink);
        mailSender.send(message);

    }
}