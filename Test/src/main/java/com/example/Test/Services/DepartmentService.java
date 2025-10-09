package com.example.Test.Services;

import com.example.Test.DTO.DepartmentDTO;
import com.example.Test.Models.Department;
import com.example.Test.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
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
    
    // Get department by ID as DTO with better error handling
    public Optional<DepartmentDTO> getDepartmentById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return departmentRepository.findById(id)
                .map(DepartmentDTO::new);
    }
    
    // Create new department from DTO
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO);
        department = departmentRepository.save(department);
        return new DepartmentDTO(department);
    }
    
    // Update existing department from DTO with better error handling
    public Optional<DepartmentDTO> updateDepartment(Long id, DepartmentDTO departmentDTO) {
        if (id == null || departmentDTO == null) {
            return Optional.empty();
        }
        return departmentRepository.findById(id)
                .map(existingDepartment -> {
                    updateEntityFromDTO(existingDepartment, departmentDTO);
                    Department updated = departmentRepository.save(existingDepartment);
                    return new DepartmentDTO(updated);
                });
    }
    
    // Delete department by ID with better error handling
    public boolean deleteDepartment(Long id) {
        if (id == null) {
            return false;
        }
        if (departmentRepository.existsById(id)) {
            try {
                departmentRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                // Log the error (in a real application, use proper logging)
                System.err.println("Error deleting department with ID " + id + ": " + e.getMessage());
                return false;
            }
        }
        return false;
    }
    
    // Delete all department records
    @Transactional
    public void deleteAllDepartments() {
        try {
            departmentRepository.deleteAll();
            System.out.println("All department records deleted successfully!");
        } catch (Exception e) {
            System.err.println("Error deleting all departments: " + e.getMessage());
            throw e;
        }
    }

    // Convert DTO to Entity
    private Department convertToEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        return department;
    }
    
    // Update existing entity from DTO
    private void updateEntityFromDTO(Department department, DepartmentDTO dto) {
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
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

    // Initialize fresh departments to ensure valid references
    @Transactional
    public void initializeFreshDepartments() {
        try {
            System.out.println("Starting fresh department initialization...");

            // First, check if departments already exist
            List<Department> existingDepartments = departmentRepository.findAll();
            System.out.println("Found " + existingDepartments.size() + " existing departments");

            // If we already have departments, just return them
            if (!existingDepartments.isEmpty()) {
                System.out.println("Departments already exist:");
                for (Department dept : existingDepartments) {
                    System.out.println("- ID: " + dept.getDepartmentId() + ", Name: " + dept.getName());
                }
                return;
            }

            // Only create new departments if none exist
            System.out.println("No departments found, creating fresh ones...");

            // Create new departments with known structure
            Department computerScience = new Department("Computer Science", "Advanced computing and software development");
            Department mathematics = new Department("Mathematics", "Pure and applied mathematics");
            Department physics = new Department("Physics", "Theoretical and experimental physics");
            Department business = new Department("Business Administration", "Management and entrepreneurship");

            List<Department> departments = new ArrayList<>();
            departments.add(computerScience);
            departments.add(mathematics);
            departments.add(physics);
            departments.add(business);

            List<Department> savedDepartments = departmentRepository.saveAll(departments);

            System.out.println("Fresh departments created successfully:");
            for (Department dept : savedDepartments) {
                System.out.println("- ID: " + dept.getDepartmentId() + ", Name: " + dept.getName());
            }
        } catch (Exception e) {
            System.err.println("Error initializing fresh departments: " + e.getMessage());

            // If there's an error, just show what departments currently exist
            try {
                List<Department> currentDepartments = departmentRepository.findAll();
                System.out.println("Current departments in database:");
                for (Department dept : currentDepartments) {
                    System.out.println("- ID: " + dept.getDepartmentId() + ", Name: " + dept.getName());
                }
            } catch (Exception e2) {
                System.err.println("Error listing current departments: " + e2.getMessage());
            }
        }
    }
}
