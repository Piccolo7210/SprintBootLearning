package com.example.Test.Controller;

import com.example.Test.Models.crs;
import com.example.Test.Services.CrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/check/api/crs")
public class CrsController {

    @Autowired
    private CrsService crsService;

    @PostMapping
    public ResponseEntity<crs> createCrs(@RequestBody crs course) {
        crs saved = crsService.createCrs(course);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<crs> getAllCrs() {
        return crsService.findAllCrs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<crs> getCrsById(@PathVariable Long id) {
        Optional<crs> course = crsService.findCrsById(id);
        return course.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<crs> updateCrs(@PathVariable Long id, @RequestBody crs course) {
        try {
            crs updated = crsService.updateCrs(id, course);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCrs(@PathVariable Long id) {
        try {
            crsService.deleteCrsById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
