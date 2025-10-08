package com.example.Test.DTO;

import com.example.Test.Models.Department;

public class DepartmentDTO {
    private Long id;
    private String name;
    private String description;
    private String building;
    private String headOfDepartment;
    private Integer studentCount;
    private Integer courseCount;
    
    // Constructors
    public DepartmentDTO() {}
    
    public DepartmentDTO(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.description = department.getDescription();
        this.building = department.getBuilding();
        this.headOfDepartment = department.getHeadOfDepartment();
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
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }
    
    public String getHeadOfDepartment() { return headOfDepartment; }
    public void setHeadOfDepartment(String headOfDepartment) { this.headOfDepartment = headOfDepartment; }
    
    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
    
    public Integer getCourseCount() { return courseCount; }
    public void setCourseCount(Integer courseCount) { this.courseCount = courseCount; }
}
