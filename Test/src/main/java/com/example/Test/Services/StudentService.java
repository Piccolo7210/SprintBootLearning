package com.example.Test.Services;
import com.example.Test.Models.Department;
import com.example.Test.Models.Student;
import com.example.Test.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private DepartmentService departmentService;
    public Student createStu(Student student) {
        if(student.getStudentId() == null) {
            System.out.println("Creating new student: " + student.getName());
            return studentRepository.save(student);
        } else {
            System.out.println("Student with ID " + student.getStudentId() + " already exists");
            throw new RuntimeException("Cannot create student: Student with ID " + student.getStudentId() + " already exists");
        }
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }
    public Optional<List<Student>> findByCgpaHigherThan(Double cgpa) {
        return studentRepository.findByCgpaGreaterThan(cgpa);
    }

    public Optional<Student> findStuById(Long id) {
        return studentRepository.findById(id);
    }

    public Student updateStu(Long id, Student updatedStudent) {
        if (studentRepository.existsById(id)) {
            updatedStudent.setStudentId(id); // Ensure the ID is set for update
            return studentRepository.save(updatedStudent);
        } else {
            throw new RuntimeException("Student with ID " + id + " not found for update");
        }
    }

    public void deleteStuById(Long id) {
        if(studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            System.out.println("Student with ID " + id + " deleted successfully");
        } else {
            throw new RuntimeException("Student with ID " + id + " not found for deletion");
        }
    }
    public String findStuNameById(Long id) {
       return studentRepository.findById(id)
                .map(Student::getName).orElse("Student not found");
    }
    public String findDepartNameById(Long Dept_id) {
        return departmentService.findDeptNameById(Dept_id);
    }
    public Map<Department, List<Student>> getStudentsByDepartment() {
        List<Student> students = findAllStudents();
        Map<Department, List<Student>> map = new LinkedHashMap<>();
        for (Student student : students) {
            Long deptId = student.getDeptId();
            Department department = departmentService.findDeptById(deptId).orElse(null);
            if (department != null) {
                map.putIfAbsent(department, new ArrayList<>());
                map.get(department).add(student);
            }
        }
        return map;
    }
    public long getStudentCountByDeptId() {
        List<Department> departments = departmentService.findAllDepts();


        List<List<Student>> list = new ArrayList<>();
        for(Department d : departments)
        {
            list.add(studentRepository.findStudentsByDeptId(d.getDeptId()).orElse(null));
        }// Example department ID
        long i=0;
        for(List<Student> l : list)
        {
            if(l != null)
            i += l.size();
        }
        return i;
    }
}
