package com.example.Test.Controller;

import com.example.Test.Models.StudentCourse;
import com.example.Test.Services.StudentCourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-course")
public class StudentCourseController {
    private final StudentCourseService studentCourseService;

    public StudentCourseController(StudentCourseService studentCourseService) {
        this.studentCourseService = studentCourseService;
    }

    @PostMapping("/enroll")
    public StudentCourse enroll(@RequestParam Long studentId, @RequestParam Long courseId) {
        return studentCourseService.enrollStudentInCourse(studentId, courseId);
    }

    @DeleteMapping("/remove")
    public void remove(@RequestParam Long studentId, @RequestParam Long courseId) {
        studentCourseService.removeEnrollment(studentId, courseId);
    }

    @GetMapping("/student/{studentId}")
    public List<StudentCourse> getCoursesByStudent(@PathVariable Long studentId) {
        return studentCourseService.getCoursesByStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<StudentCourse> getStudentsByCourse(@PathVariable Long courseId) {
        return studentCourseService.getStudentsByCourse(courseId);
    }
}
