package com.example.Test.Repository;

import com.example.Test.Models.Course;
import com.example.Test.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Basic JPQL Query - Find course by code
    @Query("SELECT c FROM Course c WHERE c.courseCode = :courseCode")
    Course findByCourseCode(@Param("courseCode") String courseCode);
    
    // JPQL with JOIN and aggregation - Find courses with enrollment statistics
    @Query("SELECT c.courseCode, c.courseName, d.name, COUNT(s), c.maxCapacity FROM Course c " +
           "JOIN c.department d " +
           "LEFT JOIN c.enrolledStudents s " +
           "GROUP BY c.id, c.courseCode, c.courseName, d.name, c.maxCapacity")
    List<Object[]> findCoursesWithEnrollmentStats();
    
    // Complex JPQL - Find courses with capacity utilization above threshold
    @Query("SELECT c FROM Course c WHERE " +
           "CAST(SIZE(c.enrolledStudents) AS double) / c.maxCapacity > :threshold")
    List<Course> findCoursesAboveCapacityThreshold(@Param("threshold") Double threshold);
    
    // JPQL with subquery - Find courses with above average enrollment
    @Query("SELECT c FROM Course c WHERE SIZE(c.enrolledStudents) > " +
           "(SELECT AVG(SIZE(c2.enrolledStudents)) FROM Course c2)")
    List<Course> findCoursesAboveAverageEnrollment();
    
    // Advanced JPQL with multiple JOINs and conditions
    @Query("SELECT c.courseCode, c.courseName, d.name, AVG(s.gpa) FROM Course c " +
           "JOIN c.department d " +
           "JOIN c.enrolledStudents s " +
           "WHERE s.gpa IS NOT NULL AND c.level = :level " +
           "GROUP BY c.id, c.courseCode, c.courseName, d.name " +
           "HAVING COUNT(s) >= :minStudents " +
           "ORDER BY AVG(s.gpa) DESC")
    List<Object[]> findCoursesWithHighPerformingStudents(@Param("level") Course.CourseLevel level,
                                                         @Param("minStudents") Long minStudents);
    
    // JPQL with CASE statement for capacity analysis
    @Query("SELECT c.courseCode, c.courseName, SIZE(c.enrolledStudents), c.maxCapacity, " +
           "CASE " +
           "WHEN SIZE(c.enrolledStudents) = c.maxCapacity THEN 'Full' " +
           "WHEN CAST(SIZE(c.enrolledStudents) AS double) / c.maxCapacity > 0.8 THEN 'Almost Full' " +
           "WHEN CAST(SIZE(c.enrolledStudents) AS double) / c.maxCapacity > 0.5 THEN 'Half Full' " +
           "ELSE 'Available' " +
           "END as capacityStatus " +
           "FROM Course c WHERE c.department.name = :departmentName")
    List<Object[]> findCoursesCapacityStatus(@Param("departmentName") String departmentName);
    
    // Complex query with multiple aggregations
    @Query("SELECT d.name, " +
           "COUNT(c), " +
           "SUM(c.credits), " +
           "AVG(SIZE(c.enrolledStudents)), " +
           "MAX(SIZE(c.enrolledStudents)) " +
           "FROM Course c " +
           "JOIN c.department d " +
           "GROUP BY d.id, d.name " +
           "ORDER BY COUNT(c) DESC")
    List<Object[]> findDepartmentCourseStatistics();
    
    // Find courses by instructor with student performance
    @Query("SELECT c.courseCode, c.instructor, COUNT(s), AVG(s.gpa) FROM Course c " +
           "LEFT JOIN c.enrolledStudents s " +
           "WHERE c.instructor = :instructor AND s.gpa IS NOT NULL " +
           "GROUP BY c.id, c.courseCode, c.instructor")
    List<Object[]> findCoursesByInstructorWithPerformance(@Param("instructor") String instructor);
}
