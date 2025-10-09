package com.example.Test.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "studentcourse")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCourse {
    @Column(name = "studentid", nullable = false)
    private Long studentId;

    @Column(name = "courseid", nullable = false)
    private Long courseId;
}