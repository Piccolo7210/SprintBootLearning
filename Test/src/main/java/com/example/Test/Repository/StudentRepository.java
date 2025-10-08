package com.example.Test.Repository;

import com.example.Test.Models.Student;
import com.example.Test.Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Basic JPQL Query - Find by email
    @Query("SELECT s FROM Student s WHERE s.email = :email")
    Student findByEmail(@Param("email") String email);

    // JPQL with JOIN - Find students with their department and course count
    @Query("SELECT s.firstName, s.lastName, d.name, COUNT(c) FROM Student s " +
           "JOIN s.department d " +
           "LEFT JOIN s.courses c " +
           "GROUP BY s.id, s.firstName, s.lastName, d.name")
    List<Object[]> findStudentsWithDepartmentAndCourseCount();

    // Complex JPQL with aggregation - Find students with GPA above department average
    @Query("SELECT s FROM Student s WHERE s.gpa > " +
           "(SELECT AVG(s2.gpa) FROM Student s2 WHERE s2.department = s.department AND s2.gpa IS NOT NULL)")
    List<Student> findStudentsAboveDepartmentAverageGPA();

    // JPQL with multiple JOINs and conditions
    @Query("SELECT s.firstName, s.lastName, d.name, c.courseName FROM Student s " +
           "JOIN s.department d " +
           "JOIN s.courses c " +
           "WHERE c.level = :level AND s.academicYear = :year")
    List<Object[]> findStudentsByCourseLevelAndAcademicYear(@Param("level") Course.CourseLevel level,
                                                           @Param("year") Student.AcademicYear year);

    // Advanced JPQL with CASE statement and aggregation
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

    // JPQL with date functions and grouping
    @Query("SELECT YEAR(s.enrollmentDate), MONTH(s.enrollmentDate), COUNT(s) FROM Student s " +
           "WHERE s.enrollmentDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(s.enrollmentDate), MONTH(s.enrollmentDate) " +
           "ORDER BY YEAR(s.enrollmentDate), MONTH(s.enrollmentDate)")
    List<Object[]> findEnrollmentStatsByMonth(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    // Complex subquery - Find students enrolled in courses with highest enrollment
    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.id IN " +
           "(SELECT c2.id FROM Course c2 " +
           "WHERE SIZE(c2.enrolledStudents) = " +
           "(SELECT MAX(SIZE(c3.enrolledStudents)) FROM Course c3))")
    List<Student> findStudentsInMostPopularCourses();

    // JPQL with HAVING clause
    @Query("SELECT d.name, COUNT(s), AVG(s.gpa) FROM Student s " +
           "JOIN s.department d " +
           "WHERE s.status = :status " +
           "GROUP BY d.id, d.name " +
           "HAVING COUNT(s) >= :minStudents AND AVG(s.gpa) > :minGPA")
    List<Object[]> findDepartmentsWithHighPerformingStudents(@Param("status") Student.StudentStatus status,
                                                             @Param("minStudents") Long minStudents,
                                                             @Param("minGPA") Double minGPA);

    // Advanced JPQL with multiple conditions and sorting
    @Query("SELECT s FROM Student s WHERE " +
           "s.academicYear IN (:years) AND " +
           "s.gpa BETWEEN :minGPA AND :maxGPA AND " +
           "SIZE(s.courses) >= :minCourses " +
           "ORDER BY s.gpa DESC, s.lastName ASC")
    List<Student> findQualifiedStudents(@Param("years") List<Student.AcademicYear> years,
                                       @Param("minGPA") Double minGPA,
                                       @Param("maxGPA") Double maxGPA,
                                       @Param("minCourses") Integer minCourses);
}
