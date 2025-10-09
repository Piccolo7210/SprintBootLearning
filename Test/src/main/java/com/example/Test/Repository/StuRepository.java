package com.example.Test.Repository;
import com.example.Test.Models.stu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StuRepository extends JpaRepository<stu, Long> {

}
