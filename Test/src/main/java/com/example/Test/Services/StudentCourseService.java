package com.example.Test.Services;

import com.example.Test.Models.StudentCourse;
import com.example.Test.Repository.StudentCourseRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentCourseService {
    private final StudentCourseRepository studentCourseRepository;

    public StudentCourseService(StudentCourseRepository studentCourseRepository) {
        this.studentCourseRepository = studentCourseRepository;
    }

    public StudentCourse enrollStudentInCourse(Long studentId, Long courseId) {
        StudentCourse sc = new StudentCourse();
        sc.setStudentId(studentId);
        sc.setCourseId(courseId);
        return studentCourseRepository.save(sc);
    }

    public void removeEnrollment(Long studentId, Long courseId) {
        studentCourseRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }

    public List<StudentCourse> getCoursesByStudent(Long studentId) {
        return studentCourseRepository.findByStudentId(studentId);
    }

    public List<StudentCourse> getStudentsByCourse(Long courseId) {
        return studentCourseRepository.findByCourseId(courseId);
    }
}
