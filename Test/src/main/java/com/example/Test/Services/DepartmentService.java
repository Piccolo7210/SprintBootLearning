// src/main/java/com/example/Test/Services/deptService.java
package com.example.Test.Services;

import com.example.Test.Models.Department;
import com.example.Test.Models.Student;
import com.example.Test.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;


    public Department createDept(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> findAllDepts() {
        return departmentRepository.findAll();
    }

    public Optional<Department> findDeptById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department updateDept(Long id, Department updatedDepartment) {
        Optional<Department> existingDept = departmentRepository.findById(id);
        if (existingDept.isPresent()) {
            Department dept = existingDept.get();
            dept.setName(updatedDepartment.getName());
            dept.setDescription(updatedDepartment.getDescription());
            return departmentRepository.save(dept);
        } else {
            throw new RuntimeException("Department with ID " + id + " not found for update");
        }
    }

    public void deleteDeptById(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Department with ID " + id + " not found for deletion");
        }
    }

    public String findDeptNameById(Long deptId) {
        return departmentRepository.findById(deptId)
                .map(Department::getName).orElse("Department not found");
    }

}
