// src/main/java/com/example/Test/Services/deptService.java
package com.example.Test.Services;

import com.example.Test.Models.dept;
import com.example.Test.Repository.DeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class deptService {

    @Autowired
    private DeptRepository deptRepository;

    public dept createDept(dept department) {
        return deptRepository.save(department);
    }

    public List<dept> findAllDepts() {
        return deptRepository.findAll();
    }

    public Optional<dept> findDeptById(Long id) {
        return deptRepository.findById(id);
    }

    public dept updateDept(Long id, dept updatedDepartment) {
        if (deptRepository.existsById(id)) {
            updatedDepartment.setDeptId(id); // Ensure the ID is set for update
            return deptRepository.save(updatedDepartment);
        } else {
            throw new RuntimeException("Department with ID " + id + " not found for update");
        }
    }

    public void deleteDeptById(Long id) {
        if(deptRepository.existsById(id)) {
            deptRepository.deleteById(id);
        } else {
            throw new RuntimeException("Department with ID " + id + " not found for deletion");
        }
    }
}
