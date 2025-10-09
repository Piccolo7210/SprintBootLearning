package com.example.Test.Models;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
@Entity
@Table(name ="stu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class stu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentid")
    private Long studentId;
    @Column(name ="Name")
    private String name;
    @Column(name = "age")
    private int age;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "Cgpa")
    private Double cgpa;
    @Column(name = "deptID" )
    private Long deptId;
}
