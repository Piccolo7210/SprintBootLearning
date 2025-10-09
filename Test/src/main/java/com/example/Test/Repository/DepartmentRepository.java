package com.example.Test.Repository;

import com.example.Test.Models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Basic JPQL Query - Find department by name
    @Query("SELECT d FROM Department d WHERE d.name = :name")
    Department findByName(@Param("name") String name);

    // JPQL with multiple JOINs and aggregations - Complete department statistics
    @Query("SELECT d.name, d.description, COUNT(DISTINCT s), COUNT(DISTINCT c), AVG(s.gpa) FROM Department d " +
           "LEFT JOIN d.students s " +
           "LEFT JOIN d.courses c " +
           "WHERE s.gpa IS NOT NULL " +
           "GROUP BY d.departmentId, d.name, d.description")
    List<Object[]> findDepartmentStatistics();

    // Complex JPQL with subquery - Find departments with above average student count
    @Query("SELECT d FROM Department d WHERE SIZE(d.students) > " +
           "(SELECT AVG(SIZE(d2.students)) FROM Department d2)")
    List<Department> findDepartmentsAboveAverageStudentCount();

    // Advanced aggregation with CASE statements and conditions
    @Query("SELECT d.name, " +
           "COUNT(DISTINCT s), " +
           "COUNT(DISTINCT c), " +
           "CASE " +
           "WHEN COUNT(DISTINCT s) >= 50 THEN 'Large Department' " +
           "WHEN COUNT(DISTINCT s) >= 20 THEN 'Medium Department' " +
           "WHEN COUNT(DISTINCT s) >= 5 THEN 'Small Department' " +
           "ELSE 'Very Small Department' " +
           "END as departmentSize, " +
           "AVG(s.gpa) as averageGPA " +
           "FROM Department d " +
           "LEFT JOIN d.students s " +
           "LEFT JOIN d.courses c " +
           "GROUP BY d.departmentId, d.name " +
           "ORDER BY COUNT(DISTINCT s) DESC")
    List<Object[]> findDepartmentAnalysis();

    // Find departments with students having high GPA
    @Query("SELECT DISTINCT d FROM Department d " +
           "JOIN d.students s " +
           "WHERE s.gpa >= :minGpa")
    List<Department> findDepartmentsWithHighPerformingStudents(@Param("minGpa") Double minGpa);

    // Department course and credit statistics
    @Query("SELECT d.name, COUNT(c), SUM(c.credits), AVG(c.credits) FROM Department d " +
           "LEFT JOIN d.courses c " +
           "GROUP BY d.departmentId, d.name " +
           "ORDER BY SUM(c.credits) DESC")
    List<Object[]> findDepartmentCourseCredits();
}
