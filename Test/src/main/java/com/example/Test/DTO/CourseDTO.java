package com.example.Test.DTO;

import com.example.Test.Models.Course;

public class CourseDTO {
    private Long courseId;
    private String courseName;
    private Integer credits;
    private Long departmentId;
    private String departmentName;
    private Integer enrolledStudentsCount;
    
    // Constructors
    public CourseDTO() {}
    
    public CourseDTO(Course course) {
        this.courseId = course.getCourseId();
        this.courseName = course.getCourseName();
        this.credits = course.getCredits();
        if (course.getDepartment() != null) {
            this.departmentId = course.getDepartment().getDepartmentId();
            this.departmentName = course.getDepartment().getName();
        }
        if (course.getEnrolledStudents() != null) {
            this.enrolledStudentsCount = course.getEnrolledStudents().size();
        } else {
            this.enrolledStudentsCount = 0;
        }
    }
    
    public CourseDTO(Long courseId, String courseName, Integer credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
    }

    // Getters and Setters
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
    
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    
    public Integer getEnrolledStudentsCount() { return enrolledStudentsCount; }
    public void setEnrolledStudentsCount(Integer enrolledStudentsCount) { this.enrolledStudentsCount = enrolledStudentsCount; }
    
    @Override
    public String toString() {
        return "CourseDTO{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", credits=" + credits +
                ", departmentName='" + departmentName + '\'' +
                ", enrolledStudentsCount=" + enrolledStudentsCount +
                '}';
    }
}
