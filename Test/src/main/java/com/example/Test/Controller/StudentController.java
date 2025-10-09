package com.example.Test.Controller;
import com.example.Test.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Test.Models.Student;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/{email}")
    public ResponseEntity<?> getStudentByEmail(@PathVariable String email) {
        Optional<Student> st = studentService.getStudentByEmail(email);
        return st.isPresent() ? ResponseEntity.ok("Found") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
    }

    @PostMapping("/highCGPA")
    public ResponseEntity<?> getStudentsWithHighGPA(@RequestBody Map<String, Object> request) {
        try {
            if (!request.containsKey("gpa")) {
                return ResponseEntity.badRequest().body("GPA parameter is required");
            }

            Double gpa = Double.valueOf(request.get("gpa").toString());
            return ResponseEntity.ok(studentService.gethigherGpaStudent(gpa));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid GPA format. Please provide a valid number.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request: " + e.getMessage());
        }
    }
}
