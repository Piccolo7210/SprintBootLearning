package com.example.Test.Controller;

import com.example.Test.Models.Course;
import com.example.Test.Models.StudentCourse;
import com.example.Test.Repository.StudentCourseRepository;
import com.example.Test.Repository.StudentRepository;
import com.example.Test.Services.CourseService;
import com.example.Test.Services.StudentCourseService;
import com.example.Test.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/studentcourse")
@CrossOrigin(origins = "*") // Allow all origins for development
public class StudentCourseController {
    private final StudentCourseService studentCourseService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;

    public StudentCourseController(StudentCourseService studentCourseService) {
        this.studentCourseService = studentCourseService;
    }

    @PostMapping("/enroll")
    public StudentCourse enroll(@RequestParam Long studentId, @RequestParam Long courseId) {
        return studentCourseService.enrollStudentInCourse(studentId, courseId);
    }

    @DeleteMapping("/remove")
    public void remove(@RequestBody StudentCourse studentCourse) {
        studentCourseService.removeEnrollment(studentCourse.getStudentId(), studentCourse.getCourseId());
    }

    @GetMapping("/student/{studentId}")
    public List<StudentCourse> getCoursesByStudent(@PathVariable Long studentId) {
        return studentCourseService.getCoursesByStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<StudentCourse> getStudentsByCourse(@PathVariable Long courseId) {
        return studentCourseService.getStudentsByCourse(courseId);
    }
//    @GetMapping("/courses/{studentId}")
//    public ResponseEntity<?> getCoursesByStudentId(@PathVariable Long studentId) {
//        System.out.println("Request received for studentId: " + studentId);
//        try{
//            Long id = studentId;
//            return ResponseEntity.ok(studentCourseService.getCoursesByStudentId(id));
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/all")
    public ResponseEntity<List<StudentCourse>> getAllEnrollments() {
        try {
            List<StudentCourse> enrollments = studentCourseService.getAllEnrollments();
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/all/{id}")
    public ResponseEntity<?> getCoursesNameById(@PathVariable Long id) {
        try {
            List<StudentCourse> courses = studentCourseService.getCoursesByStudent(id);
            List<String> courseList = new ArrayList<>();
            for(StudentCourse crs : courses){
                courseList.add(courseService.findCrsNameById(crs.getCourseId()));

            }


            return ResponseEntity.ok(courseList);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping ("/studentName/{student_id}")
    public ResponseEntity<?> getStudentNameById(@PathVariable Long student_id) {
        try {
            return ResponseEntity.ok(studentService.findStuNameById(student_id));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/courseName/{course_id}")
    public ResponseEntity<?> getCourseNameById(@PathVariable Long course_id) {
        try {
            return ResponseEntity.ok(courseService.findCrsNameById(course_id));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/grouped-by-student")
    public ResponseEntity<Map<String, List<String>>> getCoursesGroupedByStudentName() {
        try {
            Map<String, List<String>> groupedCourses = studentCourseService.getCoursesGroupedByStudentName();
            return ResponseEntity.ok(groupedCourses);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
