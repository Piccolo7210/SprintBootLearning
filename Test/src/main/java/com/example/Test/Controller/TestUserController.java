package com.example.Test.Controller;

import com.example.Test.User;
import com.example.Test.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test-db")
    public String testDb() {
        // Insert a test user
        User testUser = new User("Musta Doe", "john@example.com");
        userService.saveUser(testUser);

        // Query all users
        List<User> users = userService.getAllUsers();
        System.out.println("Users from DB: " + users.size());

        return "DB connected! Inserted and queried " + users.size() + " users.";
    }
}
