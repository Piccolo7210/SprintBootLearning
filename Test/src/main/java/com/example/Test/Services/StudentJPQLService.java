package com.example.Test.Services;

import com.example.Test.Models.*;
import com.example.Test.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        // Clear existing data
        studentRepository.deleteAll();
        courseRepository.deleteAll();
        departmentRepository.deleteAll();

        // Create departments
        Department computerScience = new Department("Computer Science", "Advanced computing and software development");
        Department mathematics = new Department("Mathematics", "Pure and applied mathematics");
        Department physics = new Department("Physics", "Theoretical and experimental physics");
        Department business = new Department("Business Administration", "Management and entrepreneurship");

        List<Department> departments = new ArrayList<>();
        departments.add(computerScience);
        departments.add(mathematics);
        departments.add(physics);
        departments.add(business);
        departmentRepository.saveAll(departments);

        // Create students with realistic data
        Student alice = new Student("Alice", "Johnson", "alice.johnson@university.edu", 3.8);
        alice.setDepartment(computerScience);

        Student bob = new Student("Bob", "Smith", "bob.smith@university.edu", 3.2);
        bob.setDepartment(computerScience);

        Student charlie = new Student("Charlie", "Brown", "charlie.brown@university.edu", 2.9);
        charlie.setDepartment(mathematics);

        Student diana = new Student("Diana", "Wilson", "diana.wilson@university.edu", 3.7);
        diana.setDepartment(mathematics);

        Student eve = new Student("Eve", "Davis", "eve.davis@university.edu", 3.5);
        eve.setDepartment(physics);

        Student frank = new Student("Frank", "Miller", "frank.miller@university.edu", 3.1);
        frank.setDepartment(business);

        List<Student> students = new ArrayList<>();
        students.add(alice);
        students.add(bob);
        students.add(charlie);
        students.add(diana);
        students.add(eve);
        students.add(frank);
        studentRepository.saveAll(students);

        // Create courses
        Course dataStructures = new Course("Data Structures", 3);
        dataStructures.setDepartment(computerScience);

        Course algorithms = new Course("Algorithms", 4);
        algorithms.setDepartment(computerScience);

        Course calculus = new Course("Calculus I", 4);
        calculus.setDepartment(mathematics);

        Course linearAlgebra = new Course("Linear Algebra", 3);
        linearAlgebra.setDepartment(mathematics);

        Course quantumPhysics = new Course("Quantum Physics", 4);
        quantumPhysics.setDepartment(physics);

        Course marketing = new Course("Marketing Principles", 3);
        marketing.setDepartment(business);

        List<Course> courses = new ArrayList<>();
        courses.add(dataStructures);
        courses.add(algorithms);
        courses.add(calculus);
        courses.add(linearAlgebra);
        courses.add(quantumPhysics);
        courses.add(marketing);
        courseRepository.saveAll(courses);

        // Enroll students in courses
        alice.setCourses(new ArrayList<>());
        alice.getCourses().add(dataStructures);
        alice.getCourses().add(algorithms);
        alice.getCourses().add(calculus);

        bob.setCourses(new ArrayList<>());
        bob.getCourses().add(dataStructures);
        bob.getCourses().add(linearAlgebra);

        charlie.setCourses(new ArrayList<>());
        charlie.getCourses().add(calculus);
        charlie.getCourses().add(linearAlgebra);

        diana.setCourses(new ArrayList<>());
        diana.getCourses().add(calculus);
        diana.getCourses().add(linearAlgebra);
        diana.getCourses().add(quantumPhysics);

        eve.setCourses(new ArrayList<>());
        eve.getCourses().add(quantumPhysics);
        eve.getCourses().add(calculus);

        frank.setCourses(new ArrayList<>());
        frank.getCourses().add(marketing);

        studentRepository.saveAll(students);

        System.out.println("Sample data initialized successfully!");
        System.out.println("- " + departments.size() + " departments created");
        System.out.println("- " + students.size() + " students created");
        System.out.println("- " + courses.size() + " courses created");
    }

//     JPQL Demo Methods
    public void demonstrateBasicQueries() {
        System.out.println("\n=== BASIC JPQL QUERIES ===");

        // Find student by email
        Optional<Student> studentOpt = studentRepository.findByEmail("alice.johnson@university.edu");
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            System.out.println("Student found by email: " + student);
        } else {
            System.out.println("No student found with email: alice.johnson@university.edu");
        }
    }

    public void demonstrateJoinQueries() {
        System.out.println("\n=== JOIN QUERIES ===");

        // Students with department and course count
        List<Object[]> results = studentRepository.findStudentsWithDepartmentAndCourseCount();
        System.out.println("Students with Department and Course Count:");
        for (Object[] row : results) {
            System.out.printf("Student: %s %s, Department: %s, Courses: %d%n",
                            row[0], row[1], row[2], row[3]);
        }
    }

    public void demonstrateAggregationQueries() {
        System.out.println("\n=== AGGREGATION QUERIES ===");

        // Department statistics
        List<Object[]> deptStats = studentRepository.findDepartmentStatistics();
        System.out.println("Department Statistics:");
        for (Object[] row : deptStats) {
            System.out.printf("Department: %s, Students: %d, Avg GPA: %.2f, Max GPA: %.2f, Min GPA: %.2f%n",
                            row[0], row[1], row[2], row[3], row[4]);
        }
    }

    public void demonstrateComplexQueries() {
        System.out.println("\n=== COMPLEX QUERIES ===");

        // Students above department average GPA
        List<Student> topStudents = studentRepository.findStudentsAboveDepartmentAverageGPA();
        System.out.println("Students above department average GPA:");
        topStudents.forEach(s -> System.out.println("- " + s.getFirstName() + " " + s.getLastName() + " (GPA: " + s.getGpa() + ")"));
    }

    public void demonstrateAdvancedQueries() {
        System.out.println("\n=== ADVANCED QUERIES ===");

        // Students with grade categories
        List<Object[]> gradeCategories = studentRepository.findStudentsWithGradeCategories("Computer Science");
        System.out.println("Computer Science Students with Grade Categories:");
        for (Object[] row : gradeCategories) {
            System.out.printf("Student: %s %s, GPA: %.2f, Category: %s%n",
                            row[0], row[1], row[2], row[3]);
        }
    }

    public void demonstrateJoinsAndConditions() {
        System.out.println("\n=== JOINS AND CONDITIONS QUERIES ===");

        // Students with minimum GPA
        List<Object[]> highGpaStudents = studentRepository.findStudentsByMinimumGPA(3.5);
        System.out.println("Students with GPA >= 3.5:");
        for (Object[] row : highGpaStudents) {
            System.out.printf("Student: %s %s, Department: %s, Course: %s%n",
                            row[0], row[1], row[2], row[3]);
        }

        // Count students by department
        List<Object[]> studentCounts = studentRepository.countStudentsByDepartment();
        System.out.println("\nStudent counts by department:");
        for (Object[] row : studentCounts) {
            System.out.printf("Department: %s, Students: %d%n", row[0], row[1]);
        }
    }

    public void showSummary() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📊 JPQL DEMONSTRATION SUMMARY");
        System.out.println("=".repeat(50));
        System.out.println("✅ All JPQL query categories demonstrated:");
        System.out.println("   📝 Basic Queries - Simple data retrieval");
        System.out.println("   🔗 JOIN Queries - Relationship navigation");
        System.out.println("   📊 Aggregation - COUNT, AVG, GROUP BY");
        System.out.println("   🎯 Complex Queries - Subqueries and conditions");
        System.out.println("   🚀 Advanced Features - CASE statements");
        System.out.println("\n💡 Key JPQL Concepts Covered:");
        System.out.println("   • Entity-based queries (not table-based)");
        System.out.println("   • Navigation through relationships");
        System.out.println("   • Aggregation functions and grouping");
        System.out.println("   • Subqueries for complex conditions");
        System.out.println("   • CASE statements for conditional logic");
        System.out.println("   • Named parameters for safe queries");
        System.out.println("\n🎓 Learning Complete! Check the README.md for detailed explanations.");
        System.out.println("=".repeat(50));
    }
}
