# JPQL Learning Guide: Student Management System (Simplified)

## 📚 Table of Contents
1. [What is JPQL?](#what-is-jpql)
2. [Database Schema Overview](#database-schema-overview)
3. [JPQL vs SQL](#jpql-vs-sql)
4. [Basic JPQL Syntax](#basic-jpql-syntax)
5. [Query Categories](#query-categories)
6. [Hands-on Examples](#hands-on-examples)
7. [Testing Instructions](#testing-instructions)
8. [Common Patterns](#common-patterns)
9. [Best Practices](#best-practices)
10. [Troubleshooting](#troubleshooting)

## What is JPQL?

**JPQL (Java Persistence Query Language)** is an object-oriented query language used to perform database operations on JPA entities. Unlike SQL which operates on database tables and columns, JPQL works with Java objects and their properties.

### Key Benefits:
- ✅ **Database Independent**: Works with any database
- ✅ **Object-Oriented**: Uses entity names instead of table names
- ✅ **Type Safe**: Compile-time checking
- ✅ **Integrated**: Works seamlessly with JPA/Hibernate

## Database Schema Overview

Our simplified Student Management System contains three main entities with relationships:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Department    │    │     Student     │    │     Course      │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ departmentId(PK)│◄──┐│ studentId (PK)  │┌──►│ courseId (PK)   │
│ name            │   ││ firstName       ││   │ courseName      │
│ description     │   ││ lastName        ││   │ credits         │
└─────────────────┘   ││ email           ││   │ department_id(FK)│
                      ││ gpa             ││   └─────────────────┘
                      ││ department_id(FK)│           ▲
                      │└─────────────────┘           │
                      │         ▲                    │
                      │         │                    │
                      │    ┌─────────────────────────┘
                      │    │
                      │    │ ┌─────────────────┐
                      │    └─│ student_courses │ (Join Table)
                      │      ├─────────────────┤
                      └──────│ student_id (FK) │
                             │ course_id (FK)  │
                             └─────────────────┘

Relationships:
- Department (1) ←→ (Many) Student
- Department (1) ←→ (Many) Course  
- Student (Many) ←→ (Many) Course
```

### Entity Details:

**Department**: Academic departments with ID, name, and description
- `departmentId` (Long): Primary key
- `name` (String): Department name (unique)
- `description` (String): Department description

**Student**: University students with basic information
- `studentId` (Long): Primary key
- `firstName` (String): Student's first name
- `lastName` (String): Student's last name
- `email` (String): Student's email (unique)
- `gpa` (Double): Grade Point Average

**Course**: Individual courses offered by departments
- `courseId` (Long): Primary key
- `courseName` (String): Name of the course
- `credits` (Integer): Credit hours for the course

## JPQL vs SQL

### SQL Example:
```sql
SELECT s.first_name, s.last_name, d.name 
FROM students s 
JOIN departments d ON s.department_id = d.department_id 
WHERE s.gpa > 3.5;
```

### JPQL Equivalent:
```jpql
SELECT s.firstName, s.lastName, d.name 
FROM Student s 
JOIN s.department d 
WHERE s.gpa > 3.5
```

### Key Differences:
- **Entity Names**: `Student` instead of `students`
- **Property Names**: `firstName` instead of `first_name`
- **Relationship Navigation**: `s.department` instead of explicit JOIN conditions
- **Primary Key Names**: `departmentId` instead of `department_id`

## Basic JPQL Syntax

### 1. Simple SELECT
```jpql
SELECT s FROM Student s
```
- `Student` = Entity name (not table name)
- `s` = Alias (required for entity queries)

### 2. SELECT with WHERE
```jpql
SELECT s FROM Student s WHERE s.gpa > 3.0
```

### 3. SELECT Specific Fields
```jpql
SELECT s.firstName, s.lastName, s.gpa FROM Student s
```

### 4. Named Parameters
```jpql
SELECT s FROM Student s WHERE s.department.name = :departmentName
```

## Query Categories

### 1. **Basic Queries** 📝
Simple data retrieval with basic conditions.

### 2. **JOIN Queries** 🔗
Combining related entities using relationships.

### 3. **Aggregation Queries** 📊
COUNT, AVG, SUM, MAX, MIN with GROUP BY.

### 4. **Subqueries** 🎯
Nested queries for complex conditions.

### 5. **Advanced Queries** 🚀
CASE statements, complex conditions, multiple JOINs.

## Hands-on Examples

### Example 1: Basic Query
```java
@Query("SELECT s FROM Student s WHERE s.email = :email")
Student findByEmail(@Param("email") String email);
```
**Purpose**: Find a specific student by email address.

### Example 2: Simple JOIN
```java
@Query("SELECT s.firstName, s.lastName, d.name FROM Student s JOIN s.department d")
List<Object[]> findStudentsWithDepartmentNames();
```
**Purpose**: Get student names along with their department names.
**Result**: `[["Alice", "Johnson", "Computer Science"], ...]`

### Example 3: Aggregation with GROUP BY
```java
@Query("SELECT s.firstName, s.lastName, d.name, COUNT(c) FROM Student s " +
       "JOIN s.department d " +
       "LEFT JOIN s.courses c " +
       "GROUP BY s.studentId, s.firstName, s.lastName, d.name")
List<Object[]> findStudentsWithDepartmentAndCourseCount();
```
**Purpose**: Count how many courses each student is enrolled in.
**Result**: `[["Alice", "Johnson", "Computer Science", 3], ...]`

### Example 4: Complex Subquery
```java
@Query("SELECT s FROM Student s WHERE s.gpa > " +
       "(SELECT AVG(s2.gpa) FROM Student s2 WHERE s2.department = s.department AND s2.gpa IS NOT NULL)")
List<Student> findStudentsAboveDepartmentAverageGPA();
```
**Purpose**: Find students whose GPA is above their department's average.

### Example 5: Advanced CASE Statement
```java
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
```
**Purpose**: Categorize students by their GPA ranges.

## Testing Instructions

### 1. Start Your Application
```bash
mvn spring-boot:run
```

### 2. Initialize Sample Data
```bash
GET http://localhost:8080/api/student-jpql/initialize
```

### 3. Test Different Query Categories

#### Basic Queries:
```bash
GET http://localhost:8080/api/student-jpql/demo/basic
```

#### Aggregation Queries:
```bash
GET http://localhost:8080/api/student-jpql/demo/aggregation
```

#### Complex Queries:
```bash
GET http://localhost:8080/api/student-jpql/demo/complex
```

#### Advanced Queries:
```bash
GET http://localhost:8080/api/student-jpql/demo/advanced
```

#### JOIN Queries:
```bash
GET http://localhost:8080/api/student-jpql/demo/joins
```

#### All Queries:
```bash
GET http://localhost:8080/api/student-jpql/demo/all
```

## Sample Data Overview

The system includes:
- **4 Departments**: Computer Science, Mathematics, Physics, Business Administration
- **6 Students**: With different GPAs and department assignments
- **6 Courses**: Various credit values distributed across departments
- **Multiple Enrollments**: Students enrolled in different courses for realistic scenarios

This simplified dataset provides a clean environment to practice all JPQL concepts!

## Quick Start Guide

1. **Initialize Data**: `GET /api/student-jpql/initialize`
2. **Run All Examples**: `GET /api/student-jpql/demo/all`
3. **Check Console**: See formatted results with explanations
4. **Experiment**: Modify queries and test your understanding

---

**Happy Learning! 🎓 Start with the basic queries and gradually work your way up to complex ones.**
