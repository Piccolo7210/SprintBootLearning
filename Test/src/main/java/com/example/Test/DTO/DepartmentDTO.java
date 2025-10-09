package com.example.Test.DTO;

import com.example.Test.Models.Department;

public class DepartmentDTO {
    private Long departmentId;
    private String name;
    private String description;
    private Integer studentCount;
    private Integer courseCount;
    
    // Constructors
    public DepartmentDTO() {}
    
    public DepartmentDTO(Department department) {
        this.departmentId = department.getDepartmentId();
        this.name = department.getName();
        this.description = department.getDescription();
        if (department.getStudents() != null) {
            this.studentCount = department.getStudents().size();
        } else {
            this.studentCount = 0;
        }
        if (department.getCourses() != null) {
            this.courseCount = department.getCourses().size();
        } else {
            this.courseCount = 0;
        }
    }
    
    public DepartmentDTO(Long departmentId, String name, String description) {
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
    
    public Integer getCourseCount() { return courseCount; }
    public void setCourseCount(Integer courseCount) { this.courseCount = courseCount; }

    @Override
    public String toString() {
        return "DepartmentDTO{" +
                "departmentId=" + departmentId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", studentCount=" + studentCount +
                ", courseCount=" + courseCount +
                '}';
    }
}
