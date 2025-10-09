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
        return "students/form";
    }
    @GetMapping("/students/{email}")
    public String findthorughmail(@PathVariable  String email) {
        Optional<Student> student = studentService.getStudentByEmail(email);
        return student.isPresent() ? "Found" : "Not Found";
    }

    
    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<StudentDTO> student = studentService.getStudentById(id);
            if (student.isPresent()) {
                model.addAttribute("student", student.get());
                model.addAttribute("departments", departmentService.getAllDepartments());
                return "students/form";
            } else {
                redirectAttributes.addFlashAttribute("error", "Student with ID " + id + " not found!");
                return "redirect:/web/students";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error accessing student: " + e.getMessage());
            return "redirect:/web/students";
        }
    }
    
    @PostMapping("/students/save")
    public String saveStudent(@ModelAttribute StudentDTO student, RedirectAttributes redirectAttributes) {
        try {
            if (student.getStudentId() != null) {
                studentService.updateStudent(student.getStudentId(), student);
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
        try {
            if (studentService.deleteStudent(id)) {
                redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Student not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/web/students";
    }

    @PostMapping("/students/delete/{id}")
    public String deleteStudentPost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (studentService.deleteStudent(id)) {
                redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Student not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/web/students";
    }

    // Course Management
    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "courses/list";
    }
    
    @GetMapping("/courses/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new CourseDTO());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "courses/form";
    }
    
    @GetMapping("/courses/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<CourseDTO> course = courseService.getCourseById(id);
            if (course.isPresent()) {
                model.addAttribute("course", course.get());
                model.addAttribute("departments", departmentService.getAllDepartments());
                return "courses/form";
            } else {
                redirectAttributes.addFlashAttribute("error", "Course with ID " + id + " not found!");
                return "redirect:/web/courses";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error accessing course: " + e.getMessage());
            return "redirect:/web/courses";
        }
    }
    
    @PostMapping("/courses/save")
    public String saveCourse(@ModelAttribute CourseDTO course, RedirectAttributes redirectAttributes) {
        try {
            if (course.getCourseId() != null) {
                courseService.updateCourse(course.getCourseId(), course);
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
        try {
            if (courseService.deleteCourse(id)) {
                redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Course not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting course: " + e.getMessage());
        }
        return "redirect:/web/courses";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCoursePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (courseService.deleteCourse(id)) {
                redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Course not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting course: " + e.getMessage());
        }
        return "redirect:/web/courses";
    }

    // Department Management
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
    public String editDepartmentForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<DepartmentDTO> department = departmentService.getDepartmentById(id);
            if (department.isPresent()) {
                model.addAttribute("department", department.get());
                return "departments/form";
            } else {
                redirectAttributes.addFlashAttribute("error", "Department with ID " + id + " not found!");
                return "redirect:/web/departments";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error accessing department: " + e.getMessage());
            return "redirect:/web/departments";
        }
    }
    
    @PostMapping("/departments/save")
    public String saveDepartment(@ModelAttribute DepartmentDTO department, RedirectAttributes redirectAttributes) {
        try {
            if (department.getDepartmentId() != null) {
                Optional<DepartmentDTO> updated = departmentService.updateDepartment(department.getDepartmentId(), department);
                if (updated.isPresent()) {
                    redirectAttributes.addFlashAttribute("success", "Department updated successfully!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Department not found for update!");
                }
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
        try {
            if (departmentService.deleteDepartment(id)) {
                redirectAttributes.addFlashAttribute("success", "Department deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Department not found or could not be deleted!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting department: " + e.getMessage());
        }
        return "redirect:/web/departments";
    }

    // Cleanup endpoint for fixing orphaned student-department references
    @GetMapping("/cleanup/students")
    public String cleanupStudentDepartmentReferences(RedirectAttributes redirectAttributes) {
        try {
            studentService.cleanupOrphanedStudentDepartmentReferences();
            redirectAttributes.addFlashAttribute("success", "Successfully cleaned up orphaned student-department references!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error during student cleanup: " + e.getMessage());
        }
        return "redirect:/web/students";
    }

    // Cleanup endpoint for fixing orphaned course-department references
    @GetMapping("/cleanup/courses")
    public String cleanupCourseDepartmentReferences(RedirectAttributes redirectAttributes) {
        try {
            courseService.cleanupOrphanedCourseDepartmentReferences();
            redirectAttributes.addFlashAttribute("success", "Successfully cleaned up orphaned course-department references!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error during course cleanup: " + e.getMessage());
        }
        return "redirect:/web/courses";
    }

    // Comprehensive cleanup for all entities
    @GetMapping("/cleanup/all")
    public String cleanupAllOrphanedReferences(RedirectAttributes redirectAttributes) {
        try {
            studentService.cleanupOrphanedStudentDepartmentReferences();
            courseService.cleanupOrphanedCourseDepartmentReferences();
            redirectAttributes.addFlashAttribute("success", "Successfully cleaned up all orphaned references! Students and courses with invalid department references have been fixed.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error during comprehensive cleanup: " + e.getMessage());
        }
        return "redirect:/web/dashboard";
    }

    // Initialize fresh departments endpoint
    @GetMapping("/initialize/departments")
    public String initializeFreshDepartments(RedirectAttributes redirectAttributes) {
        try {
            departmentService.initializeFreshDepartments();
            redirectAttributes.addFlashAttribute("success", "Fresh departments initialized successfully! You can now create students with valid department references.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error initializing departments: " + e.getMessage());
        }
        return "redirect:/web/departments";
    }

    // Delete all records endpoints
    @GetMapping("/delete/all-students")
    public String deleteAllStudents(RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteAllStudents();
            redirectAttributes.addFlashAttribute("success", "All student records deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting students: " + e.getMessage());
        }
        return "redirect:/web/students";
    }

    @GetMapping("/delete/all-courses")
    public String deleteAllCourses(RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteAllCourses();
            redirectAttributes.addFlashAttribute("success", "All course records deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting courses: " + e.getMessage());
        }
        return "redirect:/web/courses";
    }

    @GetMapping("/delete/all-departments")
    public String deleteAllDepartments(RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteAllDepartments();
            redirectAttributes.addFlashAttribute("success", "All department records deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting departments: " + e.getMessage());
        }
        return "redirect:/web/departments";
    }

    // Complete system reset - deletes all data in proper order
    @GetMapping("/reset/complete")
    public String completeSystemReset(RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== STARTING COMPLETE SYSTEM RESET ===");

            // Delete in proper order to avoid foreign key constraints
            // 1. First delete students (they reference departments and courses)
            studentService.deleteAllStudents();
            System.out.println("✓ All students deleted");

            // 2. Then delete courses (they reference departments)
            courseService.deleteAllCourses();
            System.out.println("✓ All courses deleted");

            // 3. Finally delete departments (no foreign key dependencies)
            departmentService.deleteAllDepartments();
            System.out.println("✓ All departments deleted");

            System.out.println("=== COMPLETE SYSTEM RESET FINISHED ===");
            redirectAttributes.addFlashAttribute("success",
                "Complete system reset successful! All students, courses, and departments have been deleted. " +
                "You can now start fresh with clean data.");

        } catch (Exception e) {
            System.err.println("Error during complete system reset: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error during system reset: " + e.getMessage());
        }
        return "redirect:/web/dashboard";
    }

    // Reset and initialize fresh system
    @GetMapping("/reset/fresh-start")
    public String resetAndInitializeFreshSystem(RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== STARTING FRESH SYSTEM INITIALIZATION ===");

            // 1. Complete reset first
            studentService.deleteAllStudents();
            courseService.deleteAllCourses();
            departmentService.deleteAllDepartments();
            System.out.println("✓ All existing data cleared");

            // 2. Initialize fresh departments
            departmentService.initializeFreshDepartments();
            System.out.println("✓ Fresh departments created");

            System.out.println("=== FRESH SYSTEM INITIALIZATION COMPLETE ===");
            redirectAttributes.addFlashAttribute("success",
                "Fresh system initialization complete! All old data deleted and fresh departments created. " +
                "You can now create students and courses with valid department references.");

        } catch (Exception e) {
            System.err.println("Error during fresh system initialization: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error during fresh initialization: " + e.getMessage());
        }
        return "redirect:/web/dashboard";
    }
}
