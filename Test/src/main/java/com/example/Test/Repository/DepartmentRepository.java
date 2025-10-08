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
    @Query("SELECT d.name, d.building, COUNT(DISTINCT s), COUNT(DISTINCT c), AVG(s.gpa) FROM Department d " +
           "LEFT JOIN d.students s " +
           "LEFT JOIN d.courses c " +
           "WHERE s.gpa IS NOT NULL " +
           "GROUP BY d.id, d.name, d.building")
    List<Object[]> findDepartmentStatistics();

    // Complex JPQL with subquery - Find departments with above average student count
    @Query("SELECT d FROM Department d WHERE SIZE(d.students) > " +
           "(SELECT AVG(SIZE(d2.students)) FROM Department d2)")
    List<Department> findDepartmentsAboveAverageStudentCount();

    // Advanced aggregation with CASE statements and conditions
    @Query("SELECT d.name, " +
           "COUNT(s), " +
           "COUNT(CASE WHEN s.gpa >= 3.5 THEN 1 END) as highPerformers, " +
           "COUNT(CASE WHEN s.academicYear = 'SENIOR' THEN 1 END) as seniors " +
           "FROM Department d " +
           "LEFT JOIN d.students s " +
           "GROUP BY d.id, d.name " +
           "HAVING COUNT(s) > 0")
    List<Object[]> findDepartmentPerformanceMetrics();

    // Find departments with course capacity analysis
    @Query("SELECT d.name, " +
           "SUM(c.maxCapacity) as totalCapacity, " +
           "SUM(SIZE(c.enrolledStudents)) as totalEnrolled, " +
           "CAST(SUM(SIZE(c.enrolledStudents)) AS double) / SUM(c.maxCapacity) * 100 as utilizationRate " +
           "FROM Department d " +
           "JOIN d.courses c " +
           "GROUP BY d.id, d.name " +
           "ORDER BY utilizationRate DESC")
    List<Object[]> findDepartmentCapacityUtilization();

    // Complex query with multiple conditions and rankings
    @Query("SELECT d.name, AVG(s.gpa), COUNT(s) FROM Department d " +
           "JOIN d.students s " +
           "WHERE s.status = 'ACTIVE' AND s.gpa IS NOT NULL " +
           "GROUP BY d.id, d.name " +
           "HAVING AVG(s.gpa) > :minGPA AND COUNT(s) >= :minStudents " +
           "ORDER BY AVG(s.gpa) DESC")
    List<Object[]> findTopPerformingDepartments(@Param("minGPA") Double minGPA,
                                               @Param("minStudents") Long minStudents);

    // Find departments with their course and student counts
    @Query("SELECT d.name, COUNT(DISTINCT s), COUNT(DISTINCT c) FROM Department d " +
           "LEFT JOIN d.students s " +
           "LEFT JOIN d.courses c " +
           "GROUP BY d.id, d.name")
    List<Object[]> findDepartmentsWithCounts();

    // Find departments by building location
    @Query("SELECT d FROM Department d WHERE d.building = :building")
    List<Department> findByBuilding(@Param("building") String building);

    // Advanced query - Find departments with specific student status distribution
    @Query("SELECT d.name, s.status, COUNT(s) FROM Department d " +
           "JOIN d.students s " +
           "GROUP BY d.id, d.name, s.status " +
           "ORDER BY d.name, s.status")
    List<Object[]> findDepartmentStudentStatusDistribution();

    // Find departments with average GPA above threshold
    @Query("SELECT d FROM Department d WHERE " +
           "(SELECT AVG(s.gpa) FROM Student s WHERE s.department = d AND s.gpa IS NOT NULL) > :threshold")
    List<Department> findDepartmentsAboveAverageGPA(@Param("threshold") Double threshold);
}
