package com.example.Test.DTO;

import com.example.Test.Models.Course;
import java.time.LocalDate;

public class CourseDTO {
    private Long id;
    private String courseCode;
    private String courseName;
    private String description;
    private Integer credits;
    private String instructor;
    private Integer maxCapacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private Course.CourseLevel level;
    private Course.CourseSemester semester;
    private Long departmentId;
    private String departmentName;
    private Integer enrolledStudentsCount;
    
    // Constructors
    public CourseDTO() {}
    
    public CourseDTO(Course course) {
        this.id = course.getId();
        this.courseCode = course.getCourseCode();
        this.courseName = course.getCourseName();
        this.description = course.getDescription();
        this.credits = course.getCredits();
        this.instructor = course.getInstructor();
        this.maxCapacity = course.getMaxCapacity();
        this.startDate = course.getStartDate();
        this.endDate = course.getEndDate();
        this.level = course.getLevel();
        this.semester = course.getSemester();
        if (course.getDepartment() != null) {
            this.departmentId = course.getDepartment().getId();
            this.departmentName = course.getDepartment().getName();
        }
        if (course.getEnrolledStudents() != null) {
            this.enrolledStudentsCount = course.getEnrolledStudents().size();
        } else {
            this.enrolledStudentsCount = 0;
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
    
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    
    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public Course.CourseLevel getLevel() { return level; }
    public void setLevel(Course.CourseLevel level) { this.level = level; }
    
    public Course.CourseSemester getSemester() { return semester; }
    public void setSemester(Course.CourseSemester semester) { this.semester = semester; }
    
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    
    public Integer getEnrolledStudentsCount() { return enrolledStudentsCount; }
    public void setEnrolledStudentsCount(Integer enrolledStudentsCount) { this.enrolledStudentsCount = enrolledStudentsCount; }
    
    // Helper methods
    public String getCapacityStatus() {
        if (maxCapacity == null || enrolledStudentsCount == null) return "Unknown";
        double utilization = (double) enrolledStudentsCount / maxCapacity;
        if (utilization >= 1.0) return "Full";
        if (utilization >= 0.8) return "Almost Full";
        if (utilization >= 0.5) return "Half Full";
        return "Available";
    }
    
    public int getCapacityPercentage() {
        if (maxCapacity == null || enrolledStudentsCount == null || maxCapacity == 0) return 0;
        return (int) ((double) enrolledStudentsCount / maxCapacity * 100);
    }
}
