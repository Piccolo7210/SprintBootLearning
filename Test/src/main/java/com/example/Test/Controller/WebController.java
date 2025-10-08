package com.example.Test.Controller;

import com.example.Test.DTO.StudentDTO;
import com.example.Test.DTO.CourseDTO;
import com.example.Test.DTO.DepartmentDTO;
import com.example.Test.Services.StudentService;
import com.example.Test.Services.CourseService;
import com.example.Test.Services.DepartmentService;
import com.example.Test.Models.Student;
import com.example.Test.Models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/web")
public class WebController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private DepartmentService departmentService;
    
    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("studentCount", studentService.getStudentCount());
        model.addAttribute("courseCount", courseService.getCourseCount());
        model.addAttribute("departmentCount", departmentService.getDepartmentCount());
        return "dashboard";
    }
    
    // STUDENT CRUD OPERATIONS
    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "students/list";
    }
    
    @GetMapping("/students/new")
    public String newStudentForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("academicYears", Student.AcademicYear.values());
        model.addAttribute("statuses", Student.StudentStatus.values());
        return "students/form";
    }
    
    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Optional<StudentDTO> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("academicYears", Student.AcademicYear.values());
            model.addAttribute("statuses", Student.StudentStatus.values());
            return "students/form";
        }
        return "redirect:/web/students";
    }
    
    @PostMapping("/students/save")
    public String saveStudent(@ModelAttribute StudentDTO student, RedirectAttributes redirectAttributes) {
        try {
            if (student.getId() != null) {
                studentService.updateStudent(student.getId(), student);
                redirectAttributes.addFlashAttribute("success", "Student updated successfully!");
            } else {
                studentService.createStudent(student);
                redirectAttributes.addFlashAttribute("success", "Student created successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving student: " + e.getMessage());
        }
        return "redirect:/web/students";
    }
    
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (studentService.deleteStudent(id)) {
            redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error deleting student.");
        }
        return "redirect:/web/students";
    }
    
    @GetMapping("/students/view/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Optional<StudentDTO> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "students/view";
        }
        return "redirect:/web/students";
    }
    
    // COURSE CRUD OPERATIONS
    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "courses/list";
    }
    
    @GetMapping("/courses/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new CourseDTO());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("levels", Course.CourseLevel.values());
        model.addAttribute("semesters", Course.CourseSemester.values());
        return "courses/form";
    }
    
    @GetMapping("/courses/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Optional<CourseDTO> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("levels", Course.CourseLevel.values());
            model.addAttribute("semesters", Course.CourseSemester.values());
            return "courses/form";
        }
        return "redirect:/web/courses";
    }
    
    @PostMapping("/courses/save")
    public String saveCourse(@ModelAttribute CourseDTO course, RedirectAttributes redirectAttributes) {
        try {
            if (course.getId() != null) {
                courseService.updateCourse(course.getId(), course);
                redirectAttributes.addFlashAttribute("success", "Course updated successfully!");
            } else {
                courseService.createCourse(course);
                redirectAttributes.addFlashAttribute("success", "Course created successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving course: " + e.getMessage());
        }
        return "redirect:/web/courses";
    }
    
    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (courseService.deleteCourse(id)) {
            redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error deleting course.");
        }
        return "redirect:/web/courses";
    }
    
    @GetMapping("/courses/view/{id}")
    public String viewCourse(@PathVariable Long id, Model model) {
        Optional<CourseDTO> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            return "courses/view";
        }
        return "redirect:/web/courses";
    }
    
    // DEPARTMENT CRUD OPERATIONS
    @GetMapping("/departments")
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "departments/list";
    }
    
    @GetMapping("/departments/new")
    public String newDepartmentForm(Model model) {
        model.addAttribute("department", new DepartmentDTO());
        return "departments/form";
    }
    
    @GetMapping("/departments/edit/{id}")
    public String editDepartmentForm(@PathVariable Long id, Model model) {
        Optional<DepartmentDTO> department = departmentService.getDepartmentById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            return "departments/form";
        }
        return "redirect:/web/departments";
    }
    
    @PostMapping("/departments/save")
    public String saveDepartment(@ModelAttribute DepartmentDTO department, RedirectAttributes redirectAttributes) {
        try {
            if (department.getId() != null) {
                departmentService.updateDepartment(department.getId(), department);
                redirectAttributes.addFlashAttribute("success", "Department updated successfully!");
            } else {
                departmentService.createDepartment(department);
                redirectAttributes.addFlashAttribute("success", "Department created successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving department: " + e.getMessage());
        }
        return "redirect:/web/departments";
    }
    
    @GetMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (departmentService.deleteDepartment(id)) {
            redirectAttributes.addFlashAttribute("success", "Department deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error deleting department.");
        }
        return "redirect:/web/departments";
    }
    
    @GetMapping("/departments/view/{id}")
    public String viewDepartment(@PathVariable Long id, Model model) {
        Optional<DepartmentDTO> department = departmentService.getDepartmentById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            return "departments/view";
        }
        return "redirect:/web/departments";
    }
}
