package com.example.Test.DTO;

import com.example.Test.Models.Student;
import java.time.LocalDate;

public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String studentNumber;
    private Double gpa;
    private LocalDate dateOfBirth;
    private LocalDate enrollmentDate;
    private Student.AcademicYear academicYear;
    private Student.StudentStatus status;
    private Long departmentId;
    private String departmentName;
    
    // Constructors
    public StudentDTO() {}
    
    public StudentDTO(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.studentNumber = student.getStudentNumber();
        this.gpa = student.getGpa();
        this.dateOfBirth = student.getDateOfBirth();
        this.enrollmentDate = student.getEnrollmentDate();
        this.academicYear = student.getAcademicYear();
        this.status = student.getStatus();
        if (student.getDepartment() != null) {
            this.departmentId = student.getDepartment().getId();
            this.departmentName = student.getDepartment().getName();
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
    
    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public Student.AcademicYear getAcademicYear() { return academicYear; }
    public void setAcademicYear(Student.AcademicYear academicYear) { this.academicYear = academicYear; }
    
    public Student.StudentStatus getStatus() { return status; }
    public void setStatus(Student.StudentStatus status) { this.status = status; }
    
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    
    // Helper method
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
