package com.example.Test.Controller;
import com.example.Test.Models.stu;
import com.example.Test.Services.stuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/check/api/stu")
public class StuController {
    @Autowired
    private stuService stuService;

    @PostMapping
    public ResponseEntity<stu> createStudent(@RequestBody stu student) {
        stu st = stuService.createStu(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(st);
    }

    @GetMapping("/all")
    public List<stu> getAllStudents() {
        return stuService.findAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<stu> getStuById(@PathVariable Long id) {
        Optional<stu> student = stuService.findStuById(id);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<stu> updateStu(@PathVariable Long id, @RequestBody stu student) {
        try {
            stu updated = stuService.updateStu(id, student);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStu(@PathVariable Long id) {
        try {
            stuService.deleteStuById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
