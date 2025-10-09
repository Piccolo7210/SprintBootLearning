// src/main/java/com/example/Test/Controller/DeptController.java
package com.example.Test.Controller;

import com.example.Test.Models.dept;
import com.example.Test.Services.deptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/check/api/dept")
public class DeptController {

    @Autowired
    private deptService deptService;

    @PostMapping
    public ResponseEntity<dept> createDept(@RequestBody dept department) {
        dept saved = deptService.createDept(department);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<dept> getAllDepts() {
        return deptService.findAllDepts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<dept> getDeptById(@PathVariable Long id) {
        Optional<dept> department = deptService.findDeptById(id);
        return department.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<dept> updateDept(@PathVariable Long id, @RequestBody dept department) {
        try {
            dept updated = deptService.updateDept(id, department);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDept(@PathVariable Long id) {
        try {
            deptService.deleteDeptById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
