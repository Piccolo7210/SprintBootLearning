package com.example.Test.Models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(unique = true)
    private String studentNumber;
    
    @Column
    private LocalDate enrollmentDate;
    
    @Column
    private LocalDate dateOfBirth;
    
    @Column
    private Double gpa;
    
    @Enumerated(EnumType.STRING)
    private StudentStatus status;
    
    @Enumerated(EnumType.STRING)
    private AcademicYear academicYear;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    @ManyToMany(mappedBy = "enrolledStudents", fetch = FetchType.LAZY)
    private List<Course> courses;
    
    // Enums
    public enum StudentStatus {
        ACTIVE, INACTIVE, GRADUATED, SUSPENDED, ON_LEAVE
    }
    
    public enum AcademicYear {
        FRESHMAN, SOPHOMORE, JUNIOR, SENIOR
    }
    
    // Constructors
    public Student() {
        this.enrollmentDate = LocalDate.now();
    }
    
    public Student(String firstName, String lastName, String email, String studentNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentNumber = studentNumber;
        this.enrollmentDate = LocalDate.now();
        this.status = StudentStatus.ACTIVE;
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
    
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }
    
    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }
    
    public AcademicYear getAcademicYear() { return academicYear; }
    public void setAcademicYear(AcademicYear academicYear) { this.academicYear = academicYear; }
    
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    
    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }
    
    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
