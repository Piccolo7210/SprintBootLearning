package com.example.Test.DTO;

import com.example.Test.Models.Student;

public class StudentDTO {
    private Long studentId;
    private String firstName;
    private String lastName;
    private String email;
    private Double gpa;
    private Long departmentId;
    private String departmentName;
    
    // Constructors
    public StudentDTO() {}
    
    public StudentDTO(Student student) {
        this.studentId = student.getStudentId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.gpa = student.getGpa();
        if (student.getDepartment() != null) {
            this.departmentId = student.getDepartment().getDepartmentId();
            this.departmentName = student.getDepartment().getName();
        }
    }
    
    public StudentDTO(Long studentId, String firstName, String lastName, String email, Double gpa) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gpa = gpa;
    }

    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }
    
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    
    @Override
    public String toString() {
        return "StudentDTO{" +
                "studentId=" + studentId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gpa=" + gpa +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
