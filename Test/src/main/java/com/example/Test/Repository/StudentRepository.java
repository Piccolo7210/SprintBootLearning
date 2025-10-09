package com.example.Test.Repository;

import com.example.Test.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);
    Optional<Student[]> findByGpaGreaterThan(Double gpa);

    @Query("SELECT s.firstName, s.lastName, d.name, COUNT(c) FROM Student s " +
           "JOIN s.department d " +
           "LEFT JOIN s.courses c " +
           "GROUP BY s.studentId, s.firstName, s.lastName, d.name")
    List<Object[]> findStudentsWithDepartmentAndCourseCount();

    @Query("SELECT s FROM Student s WHERE s.gpa > " +
           "(SELECT AVG(s2.gpa) FROM Student s2 WHERE s2.department = s.department AND s2.gpa IS NOT NULL)")
    List<Student> findStudentsAboveDepartmentAverageGPA();

    @Query("SELECT s.firstName, s.lastName, d.name, c.courseName FROM Student s " +
           "JOIN s.department d " +
           "JOIN s.courses c " +
           "WHERE s.gpa >= :minGpa")
    List<Object[]> findStudentsByMinimumGPA(@Param("minGpa") Double minGpa);

    @Query("SELECT s.firstName, s.lastName, s.gpa, " +
           "CASE " +
           "WHEN s.gpa >= 3.8 THEN 'Excellent' " +
           "WHEN s.gpa >= 3.5 THEN 'Very Good' " +
           "WHEN s.gpa >= 3.0 THEN 'Good' " +
           "WHEN s.gpa >= 2.5 THEN 'Satisfactory' " +
           "ELSE 'Needs Improvement' " +
           "END as gradeCategory " +
           "FROM Student s WHERE s.department.name = :departmentName AND s.gpa IS NOT NULL")
    List<Object[]> findStudentsWithGradeCategories(@Param("departmentName") String departmentName);


    @Query("SELECT s FROM Student s WHERE s.department.name = :departmentName")
    List<Student> findByDepartmentName(@Param("departmentName") String departmentName);

    @Modifying
    @Query(value = "DELETE FROM student_courses WHERE student_id = :studentId", nativeQuery = true)
    void deleteStudentCourseRelationships(@Param("studentId") Long studentId);

    @Query("SELECT d.name, COUNT(s) FROM Department d LEFT JOIN d.students s GROUP BY d.departmentId, d.name")
    List<Object[]> countStudentsByDepartment();

    @Query("SELECT DISTINCT s FROM Student s JOIN s.courses c WHERE c.department.name = :departmentName")
    List<Student> findStudentsEnrolledInDepartmentCourses(@Param("departmentName") String departmentName);

    @Query("SELECT d.name, " +
           "COUNT(DISTINCT s), " +
           "AVG(s.gpa), " +
           "MAX(s.gpa), " +
           "MIN(s.gpa) " +
           "FROM Department d " +
           "LEFT JOIN d.students s " +
           "WHERE s.gpa IS NOT NULL " +
           "GROUP BY d.departmentId, d.name " +
           "ORDER BY COUNT(DISTINCT s) DESC")
    List<Object[]> findDepartmentStatistics();

    @Query("SELECT s.firstName, s.lastName, s.gpa, d.name FROM Student s " +
           "JOIN s.department d " +
           "WHERE s.gpa > (SELECT AVG(s2.gpa) FROM Student s2 " +
           "                WHERE s2.department = s.department AND s2.gpa IS NOT NULL) " +
           "ORDER BY s.gpa DESC")
    List<Object[]> findTopPerformersInEachDepartment();
}
