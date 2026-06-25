
package com.example.gradebook.repository;

import com.example.gradebook.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    List<Course> findByDepartment(String department);
    List<Course> findBySemester(String semester);
    List<Course> findByCourseNameContaining(String courseName);
    List<Course> findByTeacherNameContaining(String teacherName);
}
