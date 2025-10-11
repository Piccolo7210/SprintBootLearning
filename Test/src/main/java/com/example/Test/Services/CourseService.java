package com.example.Test.Services;

import com.example.Test.Models.Course;
import com.example.Test.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentService departmentService;

    public Course createCrs(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> findAllCrs() {
        return courseRepository.findAll();
    }

    public Optional<Course> findCrsById(Long id) {
        return courseRepository.findById(id);
    }

    public Course updateCrs(Long id, Course updatedCourse) {
        Optional<Course> existingCourse = courseRepository.findById(id);
        if (existingCourse.isPresent()) {
            Course course = existingCourse.get();
            course.setCourseName(updatedCourse.getCourseName());
            course.setCredits(updatedCourse.getCredits());
            course.setDeptId(updatedCourse.getDeptId());
            return courseRepository.save(course);
        } else {
            throw new RuntimeException("Course with ID " + id + " not found for update");
        }
    }

    public void deleteCrsById(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Course with ID " + id + " not found for deletion");
        }
    }

    public String findDepartNameById(Long deptId) {
        return departmentService.findDeptNameById(deptId);
    }

    public String findCrsNameById(Long id) {
        return courseRepository.findById(id)
                .map(Course::getCourseName).orElse("Course not found");
    }
}
