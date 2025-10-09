package com.example.Test.Models;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "crs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class crs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseid")
    private Long courseId;
    @Column(name ="coursename")
    private String courseName;
    @Column(name = "credits")
    private Integer credits;
    @Column(name = "deptID" )
    private Long deptId;
}
