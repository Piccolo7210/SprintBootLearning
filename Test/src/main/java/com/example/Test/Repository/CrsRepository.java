package com.example.Test.Repository;
import com.example.Test.Models.crs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CrsRepository extends JpaRepository<crs,Long> {

}
