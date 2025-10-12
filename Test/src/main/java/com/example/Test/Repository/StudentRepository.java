package com.example.Test.Repository;
import com.example.Test.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<List<Student>> findByCgpaGreaterThan(Double cgpa);
    Optional<List<Student>> findStudentsByDeptId(Long deptId);
    List<Student> findTop3ByDeptIdOrderByCgpaDesc(Long deptId);
    Optional<List<Student>> findTop3ByOrderByCgpaDesc();
}
