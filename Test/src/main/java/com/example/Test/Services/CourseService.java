package com.example.Test.Services;

import com.example.Test.DTO.CourseDTO;
import com.example.Test.Models.Course;
import com.example.Test.Models.Department;
import com.example.Test.Models.Student;
import com.example.Test.Repository.CourseRepository;
import com.example.Test.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    // Get all courses as DTOs with error handling for orphaned references
    public List<CourseDTO> getAllCourses() {
        try {
            return courseRepository.findAll().stream()
                    .map(course -> {
                        try {
                            return new CourseDTO(course);
                        } catch (Exception e) {
                            // Log the error for debugging
                            System.err.println("Error loading course " + course.getCourseId() + ": " + e.getMessage());
                            // Return a CourseDTO with basic info, null department
                            CourseDTO dto = new CourseDTO();
                            dto.setCourseId(course.getCourseId());
                            dto.setCourseName(course.getCourseName());
                            dto.setCredits(course.getCredits());
                            dto.setDepartmentName("Unknown Department");
                            dto.setEnrolledStudentsCount(0);
                            return dto;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error loading courses: " + e.getMessage());
            return new ArrayList<>(); // Return empty list if there's a general error
        }
    }
    
    // Get course by ID as DTO
    public Optional<CourseDTO> getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(CourseDTO::new);
    }
    
    // Create new course from DTO
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = convertToEntity(courseDTO);
        course = courseRepository.save(course);
        return new CourseDTO(course);
    }
    
    // Update existing course from DTO
    public Optional<CourseDTO> updateCourse(Long id, CourseDTO courseDTO) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                    updateEntityFromDTO(existingCourse, courseDTO);
                    Course updated = courseRepository.save(existingCourse);
                    return new CourseDTO(updated);
                });
    }
    
    // Delete course by ID with proper relationship cleanup
    public boolean deleteCourse(Long id) {
        try {
            Optional<Course> courseOpt = courseRepository.findById(id);
            if (courseOpt.isPresent()) {
                Course course = courseOpt.get();

                // Method 1: Clear the many-to-many relationships using entity management
                if (course.getEnrolledStudents() != null && !course.getEnrolledStudents().isEmpty()) {
                    course.getEnrolledStudents().clear();
                    courseRepository.save(course);
                    courseRepository.flush(); // Force immediate execution
                }

                // Method 2: Use native SQL to ensure join table entries are deleted
                courseRepository.deleteCourseStudentRelationships(id);

                // Now delete the course
                courseRepository.deleteById(id);
                System.out.println("Successfully deleted course with ID: " + id);
                return true;
            } else {
                System.out.println("Course with ID " + id + " not found");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error deleting course with ID " + id + ": " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            return false;
        }
    }
    
    // Convert DTO to Entity
    private Course convertToEntity(CourseDTO dto) {
        Course course = new Course();
        course.setCourseName(dto.getCourseName());
        course.setCredits(dto.getCredits());

        if (dto.getDepartmentId() != null) {
            Optional<Department> department = departmentRepository.findById(dto.getDepartmentId());
            department.ifPresent(course::setDepartment);
        }

        return course;
    }

    // Update existing entity from DTO
    private void updateEntityFromDTO(Course course, CourseDTO dto) {
        course.setCourseName(dto.getCourseName());
        course.setCredits(dto.getCredits());

        if (dto.getDepartmentId() != null) {
            Optional<Department> department = departmentRepository.findById(dto.getDepartmentId());
            department.ifPresent(course::setDepartment);
        } else {
            course.setDepartment(null);
        }
    }

    // Get count for dashboard
    public long getCourseCount() {
        return courseRepository.count();
    }
    
    // Search courses by name
    public List<CourseDTO> searchCoursesByName(String name) {
        return courseRepository.findAll().stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(name.toLowerCase()))
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    // Clean up orphaned course records that reference non-existent departments
    @Transactional
    public void cleanupOrphanedCourseDepartmentReferences() {
        try {
            // Get all courses and departments
            List<Course> allCourses = courseRepository.findAll();
            List<Department> allDepartments = departmentRepository.findAll();

            // Get valid department IDs
            List<Long> validDepartmentIds = allDepartments.stream()
                    .map(Department::getDepartmentId)
                    .collect(Collectors.toList());

            System.out.println("Valid department IDs for courses: " + validDepartmentIds);

            // Find and fix courses with invalid department references
            for (Course course : allCourses) {
                try {
                    if (course.getDepartment() != null) {
                        Long deptId = course.getDepartment().getDepartmentId();
                        if (!validDepartmentIds.contains(deptId)) {
                            System.out.println("Found course " + course.getCourseId() +
                                             " with invalid department ID: " + deptId);
                            // Set department to null for orphaned courses
                            course.setDepartment(null);
                            courseRepository.save(course);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing course " + course.getCourseId() + ": " + e.getMessage());
                    // Force set department to null for problematic courses
                    course.setDepartment(null);
                    courseRepository.save(course);
                }
            }
            System.out.println("Course cleanup completed successfully!");
        } catch (Exception e) {
            System.err.println("Error during course cleanup: " + e.getMessage());
        }
    }

    // Delete all course records
    @Transactional
    public void deleteAllCourses() {
        try {
            courseRepository.deleteAll();
            System.out.println("All course records deleted successfully!");
        } catch (Exception e) {
            System.err.println("Error deleting all courses: " + e.getMessage());
            throw e;
        }
    }
}
