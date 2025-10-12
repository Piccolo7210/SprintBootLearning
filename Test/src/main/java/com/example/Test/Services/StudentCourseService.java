package com.example.Test.Services;

import com.example.Test.Models.Course;
import com.example.Test.Models.Student;
import com.example.Test.Models.StudentCourse;
import com.example.Test.Services.CourseService;
import com.example.Test.Services.StudentService;
import com.example.Test.Repository.StudentCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentCourseService {

    @Autowired
    private final StudentCourseRepository studentCourseRepository;
    @Autowired
    private final CourseService courseService;
    @Autowired
    private final StudentService studentService;

    public StudentCourseService(StudentCourseRepository studentCourseRepository, CourseService courseService, StudentService studentService) {
        this.studentCourseRepository = studentCourseRepository;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    public StudentCourse enrollStudentInCourse(Long studentId, Long courseId) {
        StudentCourse sc = new StudentCourse();
        sc.setStudentId(studentId);
        sc.setCourseId(courseId);
        return studentCourseRepository.save(sc);
    }

    @Transactional
    public void removeEnrollment(Long studentId, Long courseId) {
        studentCourseRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }

    public List<StudentCourse> getCoursesByStudent(Long studentId) {
        return studentCourseRepository.findByStudentId(studentId);
    }

    public List<StudentCourse> getStudentsByCourse(Long courseId) {
        return studentCourseRepository.findByCourseId(courseId);
    }
    public Optional<List<Course>> getCoursesByStudentId(Long studentId) {
        List<Course> courseList = new ArrayList<>();
        List<StudentCourse> obj = studentCourseRepository.findByStudentId(studentId);
        for (StudentCourse o : obj) {
            Optional<Course> course = courseService.findCrsById(o.getCourseId());
            courseList.add(course.orElse(null));
        }
        return Optional.of(courseList);
    }


    public List<StudentCourse> getAllEnrollments() {
        return studentCourseRepository.findAll();
    }

    public Map<String, List<String>> getCoursesGroupedByStudentName() {
        // Get all enrollments
        List<StudentCourse> allEnrollments = studentCourseRepository.findAll();

        Map<String, List<String>> groupedResults = new LinkedHashMap<>();

        for (StudentCourse enrollment : allEnrollments) {
            // Get student name using existing service method
            String studentName = studentService.findStuNameById(enrollment.getStudentId());

            // Get course name using existing service method
            String courseName = courseService.findCrsNameById(enrollment.getCourseId());

            // Group courses by student name
            groupedResults.computeIfAbsent(studentName, k -> new ArrayList<>()).add(courseName);
        }

        return groupedResults;
    }
    public Optional<Map<Student,Long>> getStudentsHavingCoursesMoreThan(Long numberOfCourses) {
        List<Student> allStudents = studentService.findAllStudents();
        Map<Student,Long> studentToCourseCount   = new LinkedHashMap<>();
        List<StudentCourse> list = studentCourseRepository.findAll();

        for (StudentCourse  o: list) {
            Student student = studentService.findStuById(o.getStudentId()).orElse(null);
            if (student != null) {
                studentToCourseCount.put(student, studentToCourseCount.getOrDefault(student, 0L) + 1);
            }
        }
        Map<Student,Long> filteredStudents = new LinkedHashMap<>();
        for (Map.Entry<Student, Long> entry : studentToCourseCount.entrySet()) {
            if (entry.getValue() > numberOfCourses) {
                filteredStudents.put(entry.getKey(), entry.getValue());
            }
        }

        return Optional.of(filteredStudents);
    }
}
