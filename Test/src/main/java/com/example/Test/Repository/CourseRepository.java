package com.example.Test.Repository;

import com.example.Test.Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Basic JPQL Query - Find course by name
    @Query("SELECT c FROM Course c WHERE c.courseName = :courseName")
    Course findByCourseName(@Param("courseName") String courseName);

    // JPQL with JOIN and aggregation - Find courses with enrollment statistics
    @Query("SELECT c.courseName, d.name, COUNT(s), c.credits FROM Course c " +
           "JOIN c.department d " +
           "LEFT JOIN c.enrolledStudents s " +
           "GROUP BY c.courseId, c.courseName, d.name, c.credits")
    List<Object[]> findCoursesWithEnrollmentStats();
    
    // Complex JPQL - Find courses with enrollment above threshold
    @Query("SELECT c FROM Course c WHERE SIZE(c.enrolledStudents) > :threshold")
    List<Course> findCoursesAboveEnrollmentThreshold(@Param("threshold") Integer threshold);

    // JPQL with subquery - Find courses with above average enrollment
    @Query("SELECT c FROM Course c WHERE SIZE(c.enrolledStudents) > " +
           "(SELECT AVG(SIZE(c2.enrolledStudents)) FROM Course c2)")
    List<Course> findCoursesAboveAverageEnrollment();
    
    // Advanced JPQL with multiple JOINs and conditions
    @Query("SELECT c.courseName, d.name, AVG(s.gpa), COUNT(s) FROM Course c " +
           "JOIN c.department d " +
           "JOIN c.enrolledStudents s " +
           "WHERE s.gpa IS NOT NULL AND c.credits >= :minCredits " +
           "GROUP BY c.courseId, c.courseName, d.name " +
           "HAVING COUNT(s) >= :minStudents " +
           "ORDER BY AVG(s.gpa) DESC")
    List<Object[]> findCoursesWithHighPerformingStudents(@Param("minCredits") Integer minCredits,
                                                         @Param("minStudents") Long minStudents);
    
    // JPQL with CASE statement for enrollment analysis
    @Query("SELECT c.courseName, SIZE(c.enrolledStudents), c.credits, " +
           "CASE " +
           "WHEN SIZE(c.enrolledStudents) >= 20 THEN 'High Enrollment' " +
           "WHEN SIZE(c.enrolledStudents) >= 10 THEN 'Medium Enrollment' " +
           "WHEN SIZE(c.enrolledStudents) >= 5 THEN 'Low Enrollment' " +
           "ELSE 'Very Low Enrollment' " +
           "END as enrollmentStatus " +
           "FROM Course c WHERE c.department.name = :departmentName")
    List<Object[]> findCoursesWithEnrollmentStatus(@Param("departmentName") String departmentName);

    // Department course statistics
    @Query("SELECT d.name, " +
           "COUNT(c), " +
           "SUM(c.credits), " +
           "AVG(SIZE(c.enrolledStudents)), " +
           "MAX(SIZE(c.enrolledStudents)) " +
           "FROM Course c " +
           "JOIN c.department d " +
           "GROUP BY d.departmentId, d.name " +
           "ORDER BY COUNT(c) DESC")
    List<Object[]> findDepartmentCourseStatistics();
    
    // Find courses by department
    @Query("SELECT c FROM Course c WHERE c.department.name = :departmentName")
    List<Course> findByDepartmentName(@Param("departmentName") String departmentName);

    // Find courses with specific credit range
    @Query("SELECT c FROM Course c WHERE c.credits BETWEEN :minCredits AND :maxCredits")
    List<Course> findByCreditRange(@Param("minCredits") Integer minCredits,
                                   @Param("maxCredits") Integer maxCredits);

    // Native SQL query to delete course-student relationships
    @Modifying
    @Query(value = "DELETE FROM student_courses WHERE course_id = :courseId", nativeQuery = true)
    void deleteCourseStudentRelationships(@Param("courseId") Long courseId);
}
