package com.example.Test.Repository;

import com.example.Test.Models.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    List<StudentCourse> findByStudentId(Long studentId);
    List<StudentCourse> findByCourseId(Long courseId);
    void deleteByStudentIdAndCourseId(Long studentId, Long courseId);
}
