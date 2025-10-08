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

@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    // Get all students as DTOs
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentDTO::new)
                .collect(Collectors.toList());
    }
    
    // Get student by ID as DTO
    public Optional<StudentDTO> getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(StudentDTO::new);
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
    
    // Delete student by ID
    public boolean deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Convert DTO to Entity
    private Student convertToEntity(StudentDTO dto) {
        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setStudentNumber(dto.getStudentNumber());
        student.setGpa(dto.getGpa());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setEnrollmentDate(dto.getEnrollmentDate());
        student.setAcademicYear(dto.getAcademicYear());
        student.setStatus(dto.getStatus());
        
        if (dto.getDepartmentId() != null) {
            Optional<Department> department = departmentRepository.findById(dto.getDepartmentId());
            department.ifPresent(student::setDepartment);
        }
        
        return student;
    }
    
    // Update existing entity from DTO
    private void updateEntityFromDTO(Student student, StudentDTO dto) {
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setStudentNumber(dto.getStudentNumber());
        student.setGpa(dto.getGpa());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setEnrollmentDate(dto.getEnrollmentDate());
        student.setAcademicYear(dto.getAcademicYear());
        student.setStatus(dto.getStatus());
        
        if (dto.getDepartmentId() != null) {
            Optional<Department> department = departmentRepository.findById(dto.getDepartmentId());
            department.ifPresent(student::setDepartment);
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
}
