package com.example.Test.Services;

import com.example.Test.Models.*;
import com.example.Test.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class StudentJPQLService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Initialize comprehensive sample data
    public void initializeSampleData() {
        System.out.println("=== INITIALIZING SAMPLE DATA ===");

        // Create departments
        Department computerScience = new Department("Computer Science", "Advanced computing and software development", "Tech Building", "Dr. Alan Turing");
        Department mathematics = new Department("Mathematics", "Pure and applied mathematics", "Science Hall", "Dr. Emmy Noether");
        Department physics = new Department("Physics", "Theoretical and experimental physics", "Physics Lab", "Dr. Marie Curie");
        Department business = new Department("Business Administration", "Management and entrepreneurship", "Business Center", "Dr. Peter Drucker");

        departmentRepository.saveAll(Arrays.asList(computerScience, mathematics, physics, business));

        // Create students with realistic data
        Student alice = new Student("Alice", "Johnson", "alice.johnson@university.edu", "CS2021001");
        alice.setDepartment(computerScience);
        alice.setGpa(3.8);
        alice.setAcademicYear(Student.AcademicYear.JUNIOR);
        alice.setDateOfBirth(LocalDate.of(2002, 3, 15));
        alice.setStatus(Student.StudentStatus.ACTIVE);

        Student bob = new Student("Bob", "Smith", "bob.smith@university.edu", "CS2021002");
        bob.setDepartment(computerScience);
        bob.setGpa(3.2);
        bob.setAcademicYear(Student.AcademicYear.SOPHOMORE);
        bob.setDateOfBirth(LocalDate.of(2003, 7, 22));
        bob.setStatus(Student.StudentStatus.ACTIVE);

        Student charlie = new Student("Charlie", "Brown", "charlie.brown@university.edu", "MATH2020001");
        charlie.setDepartment(mathematics);
        charlie.setGpa(3.9);
        charlie.setAcademicYear(Student.AcademicYear.SENIOR);
        charlie.setDateOfBirth(LocalDate.of(2001, 11, 8));
        charlie.setStatus(Student.StudentStatus.ACTIVE);

        Student diana = new Student("Diana", "Williams", "diana.williams@university.edu", "PHYS2021001");
        diana.setDepartment(physics);
        diana.setGpa(3.6);
        diana.setAcademicYear(Student.AcademicYear.JUNIOR);
        diana.setDateOfBirth(LocalDate.of(2002, 5, 30));
        diana.setStatus(Student.StudentStatus.ACTIVE);

        Student eve = new Student("Eve", "Davis", "eve.davis@university.edu", "BUS2020002");
        eve.setDepartment(business);
        eve.setGpa(2.8);
        eve.setAcademicYear(Student.AcademicYear.SOPHOMORE);
        eve.setDateOfBirth(LocalDate.of(2003, 1, 12));
        eve.setStatus(Student.StudentStatus.ACTIVE);

        Student frank = new Student("Frank", "Miller", "frank.miller@university.edu", "CS2019001");
        frank.setDepartment(computerScience);
        frank.setGpa(3.4);
        frank.setAcademicYear(Student.AcademicYear.SENIOR);
        frank.setDateOfBirth(LocalDate.of(2001, 9, 18));
        frank.setStatus(Student.StudentStatus.ACTIVE);

        // Save students first
        studentRepository.saveAll(Arrays.asList(alice, bob, charlie, diana, eve, frank));

        // Create courses
        Course dataStructures = new Course("CS201", "Data Structures and Algorithms", 4, "Prof. Donald Knuth");
        dataStructures.setDepartment(computerScience);
        dataStructures.setLevel(Course.CourseLevel.INTERMEDIATE);
        dataStructures.setSemester(Course.CourseSemester.FALL);
        dataStructures.setMaxCapacity(30);
        dataStructures.setStartDate(LocalDate.of(2024, 9, 1));
        dataStructures.setEndDate(LocalDate.of(2024, 12, 15));

        Course calculus = new Course("MATH101", "Calculus I", 4, "Prof. Isaac Newton");
        calculus.setDepartment(mathematics);
        calculus.setLevel(Course.CourseLevel.BEGINNER);
        calculus.setSemester(Course.CourseSemester.FALL);
        calculus.setMaxCapacity(50);
        calculus.setStartDate(LocalDate.of(2024, 9, 1));
        calculus.setEndDate(LocalDate.of(2024, 12, 15));

        Course physics101 = new Course("PHYS101", "Introduction to Physics", 3, "Prof. Albert Einstein");
        physics101.setDepartment(physics);
        physics101.setLevel(Course.CourseLevel.BEGINNER);
        physics101.setSemester(Course.CourseSemester.FALL);
        physics101.setMaxCapacity(40);
        physics101.setStartDate(LocalDate.of(2024, 9, 1));
        physics101.setEndDate(LocalDate.of(2024, 12, 15));

        Course management = new Course("BUS301", "Business Management", 3, "Prof. Henry Ford");
        management.setDepartment(business);
        management.setLevel(Course.CourseLevel.ADVANCED);
        management.setSemester(Course.CourseSemester.FALL);
        management.setMaxCapacity(25);
        management.setStartDate(LocalDate.of(2024, 9, 1));
        management.setEndDate(LocalDate.of(2024, 12, 15));

        Course database = new Course("CS301", "Database Systems", 4, "Prof. Edgar Codd");
        database.setDepartment(computerScience);
        database.setLevel(Course.CourseLevel.ADVANCED);
        database.setSemester(Course.CourseSemester.SPRING);
        database.setMaxCapacity(35);
        database.setStartDate(LocalDate.of(2025, 1, 15));
        database.setEndDate(LocalDate.of(2025, 5, 15));

        // Save courses first without student enrollments
        courseRepository.saveAll(Arrays.asList(dataStructures, calculus, physics101, management, database));

        // Now handle student enrollments by managing from the Student side (owning side)
        // Use mutable ArrayLists instead of Arrays.asList() to avoid UnsupportedOperationException
        alice.setCourses(new ArrayList<>(Arrays.asList(dataStructures, calculus, physics101, database)));
        bob.setCourses(new ArrayList<>(Arrays.asList(dataStructures, calculus)));
        charlie.setCourses(new ArrayList<>(Arrays.asList(calculus)));
        diana.setCourses(new ArrayList<>(Arrays.asList(calculus, physics101)));
        eve.setCourses(new ArrayList<>(Arrays.asList(management)));
        frank.setCourses(new ArrayList<>(Arrays.asList(dataStructures, database)));

        // Save students again to persist the course relationships
        studentRepository.saveAll(Arrays.asList(alice, bob, charlie, diana, eve, frank));

        System.out.println("✅ Sample data initialized successfully!");
        System.out.println("📊 Created: 4 Departments, 6 Students, 5 Courses");
    }

    // Demonstrate basic JPQL queries
    public void demonstrateBasicQueries() {
        System.out.println("\n=== BASIC JPQL QUERIES ===");

        // Find students with department and course count
        List<Object[]> studentsWithInfo = studentRepository.findStudentsWithDepartmentAndCourseCount();
        System.out.println("Students with Department and Course Count:");
        for (Object[] row : studentsWithInfo) {
            System.out.printf("📚 Student: %s %s, Department: %s, Courses: %d%n",
                row[0], row[1], row[2], row[3]);
        }
    }

    // Demonstrate aggregation queries
    public void demonstrateAggregationQueries() {
        System.out.println("\n=== AGGREGATION JPQL QUERIES ===");

        // Department statistics
        List<Object[]> deptStats = departmentRepository.findDepartmentStatistics();
        System.out.println("📈 Department Statistics:");
        for (Object[] row : deptStats) {
            System.out.printf("🏢 Department: %s, Building: %s, Students: %d, Courses: %d, Avg GPA: %.2f%n",
                row[0], row[1], row[2], row[3], row[4] != null ? (Double)row[4] : 0.0);
        }

        // Course enrollment statistics
        List<Object[]> courseStats = courseRepository.findCoursesWithEnrollmentStats();
        System.out.println("\n📊 Course Enrollment Statistics:");
        for (Object[] row : courseStats) {
            System.out.printf("📚 Course: %s - %s, Department: %s, Enrolled: %d, Capacity: %d%n",
                row[0], row[1], row[2], row[3], row[4]);
        }
    }

    // Demonstrate complex queries with subqueries
    public void demonstrateComplexQueries() {
        System.out.println("\n=== COMPLEX JPQL QUERIES WITH SUBQUERIES ===");

        // Students above department average GPA
        List<Student> aboveAverage = studentRepository.findStudentsAboveDepartmentAverageGPA();
        System.out.println("🏆 Students Above Department Average GPA:");
        for (Student student : aboveAverage) {
            System.out.printf("⭐ %s %s - GPA: %.2f, Department: %s%n",
                student.getFirstName(), student.getLastName(), student.getGpa(),
                student.getDepartment().getName());
        }

        // Courses above average enrollment
        List<Course> popularCourses = courseRepository.findCoursesAboveAverageEnrollment();
        System.out.println("\n🔥 Courses Above Average Enrollment:");
        for (Course course : popularCourses) {
            System.out.printf("📈 %s - %s, Enrolled: %d%n",
                course.getCourseCode(), course.getCourseName(),
                course.getEnrolledStudents() != null ? course.getEnrolledStudents().size() : 0);
        }
    }

    // Demonstrate advanced queries
    public void demonstrateAdvancedQueries() {
        System.out.println("\n=== ADVANCED JPQL QUERIES ===");

        // Students with grade categories
        List<Object[]> gradeCategories = studentRepository.findStudentsWithGradeCategories("Computer Science");
        System.out.println("🎓 Computer Science Students with Grade Categories:");
        for (Object[] row : gradeCategories) {
            System.out.printf("📝 %s %s - GPA: %.2f, Category: %s%n",
                row[0], row[1], row[2], row[3]);
        }

        // Course capacity status
        List<Object[]> capacityStatus = courseRepository.findCoursesCapacityStatus("Computer Science");
        System.out.println("\n🏫 Computer Science Courses Capacity Status:");
        for (Object[] row : capacityStatus) {
            System.out.printf("📊 %s - %s, Enrolled: %d/%d, Status: %s%n",
                row[0], row[1], row[2], row[3], row[4]);
        }

        // Department performance metrics
        List<Object[]> performance = departmentRepository.findDepartmentPerformanceMetrics();
        System.out.println("\n📈 Department Performance Metrics:");
        for (Object[] row : performance) {
            System.out.printf("🏆 Department: %s, Total Students: %d, High Performers: %d, Seniors: %d%n",
                row[0], row[1], row[2], row[3]);
        }
    }

    // Demonstrate joins and conditions
    public void demonstrateJoinsAndConditions() {
        System.out.println("\n=== JOINS AND CONDITIONS ===");

        // Students by course level and academic year
        List<Object[]> students = studentRepository.findStudentsByCourseLevelAndAcademicYear(
            Course.CourseLevel.INTERMEDIATE, Student.AcademicYear.JUNIOR);
        System.out.println("🎯 Junior Students in Intermediate Courses:");
        for (Object[] row : students) {
            System.out.printf("👨‍🎓 Student: %s %s, Department: %s, Course: %s%n",
                row[0], row[1], row[2], row[3]);
        }

        // Top performing departments
        List<Object[]> topDepts = departmentRepository.findTopPerformingDepartments(3.0, 2L);
        System.out.println("\n🏅 Top Performing Departments (GPA > 3.0, Students >= 2):");
        for (Object[] row : topDepts) {
            System.out.printf("🥇 Department: %s, Avg GPA: %.2f, Student Count: %d%n",
                row[0], row[1], row[2]);
        }
    }

    // Summary demonstration
    public void showSummary() {
        System.out.println("\n=== JPQL DEMONSTRATION SUMMARY ===");
        System.out.println("✅ Successfully demonstrated:");
        System.out.println("   🔹 Basic JPQL queries with JOINs");
        System.out.println("   🔹 Aggregation queries (COUNT, AVG, SUM)");
        System.out.println("   🔹 Complex subqueries");
        System.out.println("   🔹 Advanced CASE statements");
        System.out.println("   🔹 Multiple conditions and filters");
        System.out.println("   🔹 Group BY and HAVING clauses");
        System.out.println("\n🎓 Your JPQL learning system is ready!");
        System.out.println("📖 Check the README.md for detailed explanations");
    }
}
