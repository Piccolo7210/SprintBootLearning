package com.example.Test.Services;

import com.example.Test.DTO.StudentDTO;
import com.example.Test.Models.Student;
import com.example.Test.Models.Department;
import com.example.Test.Repository.StudentRepository;
import com.example.Test.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    // Get all students as DTOs with error handling for orphaned references
    public List<StudentDTO> getAllStudents() {
        try {
            return studentRepository.findAll().stream()
                    .map(student -> {
                        try {
                            return new StudentDTO(student);
                        } catch (Exception e) {
                            // Log the error for debugging
                            System.err.println("Error loading student " + student.getStudentId() + ": " + e.getMessage());
                            // Return a StudentDTO with basic info, null department
                            StudentDTO dto = new StudentDTO();
                            dto.setStudentId(student.getStudentId());
                            dto.setFirstName(student.getFirstName());
                            dto.setLastName(student.getLastName());
                            dto.setEmail(student.getEmail());
                            dto.setGpa(student.getGpa());
                            dto.setDepartmentName("Unknown Department");
                            return dto;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error loading students: " + e.getMessage());
            return new ArrayList<>(); // Return empty list if there's a general error
        }
    }
    
    // Get student by ID as DTO
    public Optional<StudentDTO> getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(StudentDTO::new);
    }
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    public String gethigherGpaStudent(Double gpa) {
        Optional<Student[]> studentOpt = studentRepository.findByGpaGreaterThan(gpa);
        if (studentOpt.isPresent()) {
            Student[] students = studentOpt.get();
            StringBuilder names = new StringBuilder();
            for (Student student : students) {
                names.append(student.getFirstName()).append(" ").append(student.getLastName()).append(", ");
            }
            // Remove trailing comma and space
            if (names.length() > 0) {
                names.setLength(names.length() - 2);
            }
            return names.toString();
        } else {
            return "No students found with GPA greater than " + gpa;
        }
    }

    // Create new student from DTO
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = convertToEntity(studentDTO);
        student = studentRepository.save(student);
        return new StudentDTO(student);
    }
    
    // Update existing student from DTO
    public Optional<StudentDTO> updateStudent(Long id, StudentDTO studentDTO) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    updateEntityFromDTO(existingStudent, studentDTO);
                    Student updated = studentRepository.save(existingStudent);
                    return new StudentDTO(updated);
                });
    }
    
    // Delete student by ID with proper relationship cleanup
    public boolean deleteStudent(Long id) {
        try {
            Optional<Student> studentOpt = studentRepository.findById(id);
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();

                // Method 1: Clear the many-to-many relationships using entity management
                if (student.getCourses() != null && !student.getCourses().isEmpty()) {
                    student.getCourses().clear();
                    studentRepository.save(student);
                    studentRepository.flush(); // Force immediate execution
                }

                // Method 2: Use native SQL to ensure join table entries are deleted
                studentRepository.deleteStudentCourseRelationships(id);

                // Now delete the student
                studentRepository.deleteById(id);
                System.out.println("Successfully deleted student with ID: " + id);
                return true;
            } else {
                System.out.println("Student with ID " + id + " not found");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error deleting student with ID " + id + ": " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            return false;
        }
    }
    
    // Convert DTO to Entity with validation
    private Student convertToEntity(StudentDTO dto) {
        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setGpa(dto.getGpa());

        if (dto.getDepartmentId() != null) {
            Optional<Department> department = departmentRepository.findById(dto.getDepartmentId());
            if (department.isPresent()) {
                student.setDepartment(department.get());
            } else {
                throw new RuntimeException("Department with ID " + dto.getDepartmentId() + " does not exist. Please select a valid department.");
            }
        }
        
        return student;
    }
    
    // Update existing entity from DTO with validation
    private void updateEntityFromDTO(Student student, StudentDTO dto) {
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setGpa(dto.getGpa());

        if (dto.getDepartmentId() != null) {
            Optional<Department> department = departmentRepository.findById(dto.getDepartmentId());
            if (department.isPresent()) {
                student.setDepartment(department.get());
            } else {
                throw new RuntimeException("Department with ID " + dto.getDepartmentId() + " does not exist. Please select a valid department.");
            }
        } else {
            student.setDepartment(null);
        }
    }
    
    // Get count for dashboard
    public long getStudentCount() {
        return studentRepository.count();
    }
    
    // Search students by name
    public List<StudentDTO> searchStudentsByName(String name) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getFirstName().toLowerCase().contains(name.toLowerCase()) ||
                                 student.getLastName().toLowerCase().contains(name.toLowerCase()))
                .map(StudentDTO::new)
                .collect(Collectors.toList());
    }

    // Clean up orphaned student records that reference non-existent departments
    @Transactional
    public void cleanupOrphanedStudentDepartmentReferences() {
        try {
            // Get all students using native query to avoid JPA loading issues
            List<Student> allStudents = studentRepository.findAll();
            List<Department> allDepartments = departmentRepository.findAll();

            // Get valid department IDs
            List<Long> validDepartmentIds = allDepartments.stream()
                    .map(Department::getDepartmentId)
                    .collect(Collectors.toList());

            System.out.println("Valid department IDs: " + validDepartmentIds);

            // Find and fix students with invalid department references
            for (Student student : allStudents) {
                try {
                    if (student.getDepartment() != null) {
                        Long deptId = student.getDepartment().getDepartmentId();
                        if (!validDepartmentIds.contains(deptId)) {
                            System.out.println("Found student " + student.getStudentId() +
                                             " with invalid department ID: " + deptId);
                            // Set department to null for orphaned students
                            student.setDepartment(null);
                            studentRepository.save(student);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing student " + student.getStudentId() + ": " + e.getMessage());
                    // Force set department to null for problematic students
                    student.setDepartment(null);
                    studentRepository.save(student);
                }
            }
            System.out.println("Cleanup completed successfully!");
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }

    // Delete all student records
    @Transactional
    public void deleteAllStudents() {
        try {
            studentRepository.deleteAll();
            System.out.println("All student records deleted successfully!");
        } catch (Exception e) {
            System.err.println("Error deleting all students: " + e.getMessage());
            throw e;
        }
    }
}
