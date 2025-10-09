package com.example.Test.Models;
import lombok.*;
import jakarta.persistence.*;
@Entity
@Table(name ="dept")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class dept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deptID")
    private Long deptId;
    @Column(name ="Name")
    private String name;
    @Column(name = "description")
    private String description;
}
