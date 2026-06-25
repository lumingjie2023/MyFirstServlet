
package com.example.gradebook.repository;

import com.example.gradebook.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findByMajor(String major);
    List<Student> findByClassName(String className);
    List<Student> findByNameContaining(String name);
    List<Student> findByStudentIdContaining(String studentId);
}
