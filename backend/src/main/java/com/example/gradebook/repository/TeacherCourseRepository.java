package com.example.gradebook.repository;

import com.example.gradebook.entity.TeacherCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, Long> {
    List<TeacherCourse> findByTeacherUsername(String teacherUsername);
    List<TeacherCourse> findByCourseId(String courseId);
    void deleteByTeacherUsernameAndCourseId(String teacherUsername, String courseId);
    boolean existsByTeacherUsernameAndCourseId(String teacherUsername, String courseId);
}