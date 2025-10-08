package com.example.Test.Services;

import com.example.Test.DTO.DepartmentDTO;
import com.example.Test.Models.Department;
import com.example.Test.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    // Get all departments as DTOs
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(DepartmentDTO::new)
                .collect(Collectors.toList());
    }
    
    // Get department by ID as DTO
    public Optional<DepartmentDTO> getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(DepartmentDTO::new);
    }
    
    // Create new department from DTO
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO);
        department = departmentRepository.save(department);
        return new DepartmentDTO(department);
    }
    
    // Update existing department from DTO
    public Optional<DepartmentDTO> updateDepartment(Long id, DepartmentDTO departmentDTO) {
        return departmentRepository.findById(id)
                .map(existingDepartment -> {
                    updateEntityFromDTO(existingDepartment, departmentDTO);
                    Department updated = departmentRepository.save(existingDepartment);
                    return new DepartmentDTO(updated);
                });
    }
    
    // Delete department by ID
    public boolean deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Convert DTO to Entity
    private Department convertToEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        department.setBuilding(dto.getBuilding());
        department.setHeadOfDepartment(dto.getHeadOfDepartment());
        return department;
    }
    
    // Update existing entity from DTO
    private void updateEntityFromDTO(Department department, DepartmentDTO dto) {
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        department.setBuilding(dto.getBuilding());
        department.setHeadOfDepartment(dto.getHeadOfDepartment());
    }
    
    // Get count for dashboard
    public long getDepartmentCount() {
        return departmentRepository.count();
    }
    
    // Search departments by name
    public List<DepartmentDTO> searchDepartments(String searchTerm) {
        return departmentRepository.findAll().stream()
                .filter(dept -> dept.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .map(DepartmentDTO::new)
                .collect(Collectors.toList());
    }
}
