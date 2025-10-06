package com.example.Test.Controller;


import com.example.Test.User;
import com.example.Test.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // User List – Display all users
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";  // users/list.html
    }

    // User Details – View individual user
    @GetMapping("/{id}")
    public String viewUser(@PathVariable String id, Model model) {
        userService.getUserById(id).ifPresentOrElse(
                user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("mode", "view");
                },
                () -> model.addAttribute("error", "User not found")
        );
        return "users/form";  // Reuse form for view/edit
    }

    // Create User – Show form
    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("mode", "create");
        return "users/form";
    }

    // Save User (Create or Update)
    @PostMapping
    public String saveUser(@ModelAttribute User user, Model model) {
        userService.saveUser(user);
        return "redirect:/users";  // Redirect to list
    }

    // Delete User
    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
