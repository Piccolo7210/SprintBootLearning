package com.example.Test.Services;

import com.example.Test.Models.crs;
import com.example.Test.Repository.CrsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrsService {

    @Autowired
    private CrsRepository crsRepository;

    public crs createCrs(crs course) {
        return crsRepository.save(course);
    }

    public List<crs> findAllCrs() {
        return crsRepository.findAll();
    }

    public Optional<crs> findCrsById(Long id) {
        return crsRepository.findById(id);
    }

    public crs updateCrs(Long id, crs updatedCourse) {
        if (crsRepository.existsById(id)) {
            updatedCourse.setCourseId(id); // Ensure the ID is set for update
            return crsRepository.save(updatedCourse);
        } else {
            throw new RuntimeException("Course with ID " + id + " not found for update");
        }
    }

    public void deleteCrsById(Long id) {
        if (crsRepository.existsById(id)) {
            crsRepository.deleteById(id);
        } else {
            throw new RuntimeException("Course with ID " + id + " not found for deletion");
        }
    }
}
