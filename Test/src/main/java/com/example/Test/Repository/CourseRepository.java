package com.example.Test.Repository;

import com.example.Test.Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByDeptId(Long deptId);
    Optional<Course> findByCourseId(Long courseId);
}
