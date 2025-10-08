package com.example.Test.Services;

import com.example.Test.DTO.CourseDTO;
import com.example.Test.Models.Course;
import com.example.Test.Models.Department;
import com.example.Test.Repository.CourseRepository;
import com.example.Test.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    
    // Get all courses as DTOs
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
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
    
    // Delete course by ID
    public boolean deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Convert DTO to Entity
    private Course convertToEntity(CourseDTO dto) {
        Course course = new Course();
        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setDescription(dto.getDescription());
        course.setCredits(dto.getCredits());
        course.setInstructor(dto.getInstructor());
        course.setMaxCapacity(dto.getMaxCapacity());
        course.setStartDate(dto.getStartDate());
        course.setEndDate(dto.getEndDate());
        course.setLevel(dto.getLevel());
        course.setSemester(dto.getSemester());
        
        if (dto.getDepartmentId() != null) {
            Optional<Department> department = departmentRepository.findById(dto.getDepartmentId());
            department.ifPresent(course::setDepartment);
        }
        
        return course;
    }
    
    // Update existing entity from DTO
    private void updateEntityFromDTO(Course course, CourseDTO dto) {
        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setDescription(dto.getDescription());
        course.setCredits(dto.getCredits());
        course.setInstructor(dto.getInstructor());
        course.setMaxCapacity(dto.getMaxCapacity());
        course.setStartDate(dto.getStartDate());
        course.setEndDate(dto.getEndDate());
        course.setLevel(dto.getLevel());
        course.setSemester(dto.getSemester());
        
        if (dto.getDepartmentId() != null) {
            Optional<Department> department = departmentRepository.findById(dto.getDepartmentId());
            department.ifPresent(course::setDepartment);
        }
    }
    
    // Get count for dashboard
    public long getCourseCount() {
        return courseRepository.count();
    }
    
    // Search courses by name or code
    public List<CourseDTO> searchCourses(String searchTerm) {
        return courseRepository.findAll().stream()
                .filter(course -> course.getCourseCode().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                course.getCourseName().toLowerCase().contains(searchTerm.toLowerCase()))
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }
}
