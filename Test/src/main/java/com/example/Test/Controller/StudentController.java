package com.example.Test.Controller;
import com.example.Test.Models.Department;
import com.example.Test.Models.Student;
import com.example.Test.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*") // Allow all origins for development
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student st = studentService.createStu(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(st);
    }

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentService.findAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStuById(@PathVariable Long id) {
        Optional<Student> student = studentService.findStuById(id);
        return student.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/deptName/{dept_id}")
    public ResponseEntity<?> getDepartmentName(@PathVariable Long dept_id) {
        try {
            Long id = dept_id;
            return ResponseEntity.ok(studentService.findDepartNameById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStu(@PathVariable Long id, @RequestBody Student student) {
        try {
            Student updated = studentService.updateStu(id, student);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/higherCgpa")
    public ResponseEntity<?> getHigherCgpa(@RequestBody Map<String, Double> request) {
        try {
            Double cgpa = request.get("cgpa");
            return ResponseEntity.ok(studentService.findByCgpaHigherThan(cgpa));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStu(@PathVariable Long id) {
        try {
            studentService.deleteStuById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listByDept")
    public ResponseEntity<?> getStudentsByDepartment() {
        try {
            Map<Department, List<Student>> map = studentService.getStudentsByDepartment();
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/countByDept")
    public ResponseEntity<?> getStudentCountByDept() {
        try {
            Map<String, Long> map = studentService.getStudentCountByDept();
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("Top3perDept" )
    public ResponseEntity<?> getTop3StudentsPerDept() {
        try {
            Map<String, List<Student>> map = studentService.Top3StudentsinDepts();
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("Top3")
    public ResponseEntity<?> getTop3Students() {
        try {
            Optional<List<Student>> list = studentService.getTop3Students();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
