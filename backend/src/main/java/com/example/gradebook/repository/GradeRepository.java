
package com.example.gradebook.repository;

import com.example.gradebook.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(String studentId);
    List<Grade> findByCourseId(String courseId);
    List<Grade> findBySemester(String semester);
    List<Grade> findByStudentIdAndSemester(String studentId, String semester);
    List<Grade> findByCourseIdAndSemester(String courseId, String semester);
}
