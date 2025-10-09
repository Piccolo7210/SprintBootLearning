package com.example.Test.Services;
import com.example.Test.Models.stu;
import com.example.Test.Repository.StuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class stuService {

    @Autowired
    private StuRepository stuRepository;

    public stu createStu(stu student) {
        if(student.getStudentId() == null) {
            System.out.println("Creating new student: " + student.getName());
            return stuRepository.save(student);
        } else {
            System.out.println("Student with ID " + student.getStudentId() + " already exists");
            throw new RuntimeException("Cannot create student: Student with ID " + student.getStudentId() + " already exists");
        }
    }

    public List<stu> findAllStudents() {
        return stuRepository.findAll();
    }

    public Optional<stu> findStuById(Long id) {
        return stuRepository.findById(id);
    }

    public stu updateStu(Long id, stu updatedStudent) {
        if (stuRepository.existsById(id)) {
            updatedStudent.setStudentId(id); // Ensure the ID is set for update
            return stuRepository.save(updatedStudent);
        } else {
            throw new RuntimeException("Student with ID " + id + " not found for update");
        }
    }

    public void deleteStuById(Long id) {
        if(stuRepository.existsById(id)) {
            stuRepository.deleteById(id);
            System.out.println("Student with ID " + id + " deleted successfully");
        } else {
            throw new RuntimeException("Student with ID " + id + " not found for deletion");
        }
    }
}
