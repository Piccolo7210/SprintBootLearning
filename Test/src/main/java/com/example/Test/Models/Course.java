package com.example.Test.Models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"department", "enrolledStudents"})
@EqualsAndHashCode(exclude = {"department", "enrolledStudents"})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseid")
    private Long courseId;

    @Column(name = "coursename", nullable = false)
    private String courseName;

    @Column(name = "credits")
    private Integer credits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany(mappedBy = "courses")
    private List<Student> enrolledStudents;
    
    // Custom constructor for basic fields
    public Course(String courseName, Integer credits) {
        this.courseName = courseName;
        this.credits = credits;
    }
}
