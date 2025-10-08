# JPQL Learning Guide: Student Management System

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

Our Student Management System contains three main entities with relationships:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Department    │    │     Student     │    │     Course      │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ id (PK)         │◄──┐│ id (PK)         │┌──►│ id (PK)         │
│ name            │   ││ firstName       ││   │ courseCode      │
│ description     │   ││ lastName        ││   │ courseName      │
│ building        │   ││ email           ││   │ credits         │
│ headOfDepartment│   ││ studentNumber   ││   │ instructor      │
└─────────────────┘   ││ gpa             ││   │ maxCapacity     │
                      ││ academicYear    ││   │ level           │
                      ││ status          ││   │ semester        │
                      ││ enrollmentDate  ││   │ department_id(FK)│
                      ││ department_id(FK)│   └─────────────────┘
                      │└─────────────────┘           ▲
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

**Student**: Represents university students with academic information
**Department**: Academic departments that contain students and offer courses
**Course**: Individual courses that students can enroll in

## JPQL vs SQL

### SQL Example:
```sql
SELECT s.first_name, s.last_name, d.name 
FROM students s 
JOIN departments d ON s.department_id = d.id 
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
       "GROUP BY s.id, s.firstName, s.lastName, d.name")
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
**Explanation**: 
- For each student, calculate the average GPA in their department
- Return students who exceed this average

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

### Example 6: Multiple Aggregations
```java
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
```
**Purpose**: Get comprehensive statistics about courses per department.

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

### 4. Observe Console Output
Check your application console for detailed results and explanations.

## Common Patterns

### Pattern 1: Navigation through Relationships
```jpql
SELECT s.department.building FROM Student s WHERE s.gpa > 3.5
```
**Use**: Access related entity properties directly.

### Pattern 2: LEFT JOIN for Optional Data
```jpql
SELECT d.name, COUNT(s) FROM Department d LEFT JOIN d.students s GROUP BY d.name
```
**Use**: Include departments even if they have no students.

### Pattern 3: SIZE() Function for Collections
```jpql
SELECT c FROM Course c WHERE SIZE(c.enrolledStudents) > 10
```
**Use**: Count elements in a collection relationship.

### Pattern 4: Subquery in WHERE
```jpql
SELECT s FROM Student s WHERE s.id IN (SELECT DISTINCT s2.id FROM Student s2 JOIN s2.courses c WHERE c.level = 'ADVANCED')
```
**Use**: Filter based on complex conditions involving related entities.

### Pattern 5: Multiple Conditions with AND/OR
```jpql
SELECT s FROM Student s WHERE s.gpa > 3.0 AND s.academicYear IN ('JUNIOR', 'SENIOR') AND SIZE(s.courses) >= 3
```
**Use**: Complex filtering with multiple criteria.

## Best Practices

### ✅ DO:
1. **Use meaningful aliases**: `Student s`, `Department d`
2. **Use named parameters**: `:paramName` instead of `?1`
3. **Handle NULL values**: Add `IS NOT NULL` checks for calculations
4. **Use appropriate JOINs**: LEFT JOIN when data might be missing
5. **Group properly**: Include all non-aggregated fields in GROUP BY
6. **Order results**: Use ORDER BY for consistent results

### ❌ DON'T:
1. **Use table names**: Use `Student` not `students`
2. **Forget aliases**: Always use aliases for entities
3. **Mix SQL syntax**: Don't use SQL-specific functions
4. **Ignore performance**: Avoid N+1 queries, use proper JOINs
5. **Skip NULL handling**: Can cause unexpected results in aggregations

## JPQL Keywords Reference

| Keyword | Purpose | Example |
|---------|---------|---------|
| `SELECT` | Choose what to return | `SELECT s FROM Student s` |
| `FROM` | Specify entity | `FROM Student s` |
| `WHERE` | Filter conditions | `WHERE s.gpa > 3.0` |
| `JOIN` | Inner join entities | `JOIN s.department d` |
| `LEFT JOIN` | Left outer join | `LEFT JOIN s.courses c` |
| `GROUP BY` | Group results | `GROUP BY d.name` |
| `HAVING` | Filter groups | `HAVING COUNT(s) > 5` |
| `ORDER BY` | Sort results | `ORDER BY s.gpa DESC` |
| `DISTINCT` | Remove duplicates | `SELECT DISTINCT d.name` |
| `IN` | Match any in list | `WHERE s.year IN (:years)` |
| `BETWEEN` | Range condition | `WHERE s.gpa BETWEEN 3.0 AND 4.0` |
| `LIKE` | Pattern matching | `WHERE s.name LIKE '%John%'` |
| `IS NULL` | Check for null | `WHERE s.gpa IS NOT NULL` |
| `SIZE()` | Collection size | `WHERE SIZE(s.courses) > 3` |
| `COUNT()` | Count records | `SELECT COUNT(s)` |
| `AVG()` | Average value | `SELECT AVG(s.gpa)` |
| `SUM()` | Sum values | `SELECT SUM(c.credits)` |
| `MAX()` | Maximum value | `SELECT MAX(s.gpa)` |
| `MIN()` | Minimum value | `SELECT MIN(s.gpa)` |

## Troubleshooting

### Common Errors and Solutions:

#### 1. "Unknown entity" Error
```
❌ Error: SELECT u FROM User u
✅ Solution: SELECT s FROM Student s (use correct entity name)
```

#### 2. "Path expected for join" Error
```
❌ Error: JOIN Department d ON s.department_id = d.id
✅ Solution: JOIN s.department d (use relationship navigation)
```

#### 3. "Non-aggregate field in GROUP BY" Error
```
❌ Error: SELECT s.name, COUNT(c) FROM Student s GROUP BY s.id
✅ Solution: SELECT s.name, COUNT(c) FROM Student s GROUP BY s.id, s.name
```

#### 4. "Parameter not bound" Error
```
❌ Error: WHERE s.gpa > :minGpa (parameter name mismatch)
✅ Solution: WHERE s.gpa > :minGPA (match @Param name exactly)
```

## Learning Path

### Phase 1: Basics (Week 1)
- [ ] Understand entity relationships
- [ ] Practice basic SELECT queries
- [ ] Learn WHERE conditions
- [ ] Master parameter binding

### Phase 2: JOINs (Week 2)
- [ ] Inner JOINs with related entities
- [ ] LEFT JOINs for optional data
- [ ] Multiple JOINs
- [ ] Relationship navigation

### Phase 3: Aggregations (Week 3)
- [ ] COUNT, AVG, SUM functions
- [ ] GROUP BY clauses
- [ ] HAVING conditions
- [ ] Complex aggregations

### Phase 4: Advanced (Week 4)
- [ ] Subqueries
- [ ] CASE statements
- [ ] Complex conditions
- [ ] Performance optimization

## Sample Data Overview

The system includes:
- **4 Departments**: Computer Science, Mathematics, Physics, Business Administration
- **6 Students**: With different GPAs, academic years, and enrollment dates
- **5 Courses**: Various levels and enrollment capacities
- **Multiple Enrollments**: Students enrolled in different courses

This provides a rich dataset to practice all JPQL concepts!

## Quick Start Guide

1. **Initialize Data**: `GET /api/student-jpql/initialize`
2. **Run All Examples**: `GET /api/student-jpql/demo/all`
3. **Check Console**: See formatted results with explanations
4. **Experiment**: Modify queries and test your understanding

---

**Happy Learning! 🎓 Start with the basic queries and gradually work your way up to complex ones.**
