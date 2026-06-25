package com.example.gradebook.service;

import com.example.gradebook.entity.Course;
import com.example.gradebook.entity.TeacherCourse;
import com.example.gradebook.repository.CourseRepository;
import com.example.gradebook.repository.TeacherCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherCourseService {
    
    @Autowired
    private TeacherCourseRepository teacherCourseRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    // 获取教师负责的所有课程ID
    public List<String> getTeacherCourseIds(String teacherUsername) {
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findByTeacherUsername(teacherUsername);
        return teacherCourses.stream()
                .map(TeacherCourse::getCourseId)
                .collect(Collectors.toList());
    }
    
    // 获取教师负责的所有课程详情
    public List<Course> getTeacherCourses(String teacherUsername) {
        List<String> courseIds = getTeacherCourseIds(teacherUsername);
        List<Course> courses = new ArrayList<>();
        for (String courseId : courseIds) {
            courseRepository.findById(courseId).ifPresent(courses::add);
        }
        return courses;
    }
    
    // 为教师分配课程
    @Transactional
    public void assignCourseToTeacher(String teacherUsername, String courseId) {
        if (!teacherCourseRepository.existsByTeacherUsernameAndCourseId(teacherUsername, courseId)) {
            TeacherCourse teacherCourse = new TeacherCourse();
            teacherCourse.setTeacherUsername(teacherUsername);
            teacherCourse.setCourseId(courseId);
            teacherCourseRepository.save(teacherCourse);
        }
    }
    
    // 移除教师的课程
    @Transactional
    public void removeCourseFromTeacher(String teacherUsername, String courseId) {
        teacherCourseRepository.deleteByTeacherUsernameAndCourseId(teacherUsername, courseId);
    }
    
    // 获取某课程的所有负责教师
    public List<String> getCourseTeachers(String courseId) {
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findByCourseId(courseId);
        return teacherCourses.stream()
                .map(TeacherCourse::getTeacherUsername)
                .collect(Collectors.toList());
    }
    
    // 获取所有教师-课程关联
    public List<TeacherCourse> getAllTeacherCourses() {
        return teacherCourseRepository.findAll();
    }
}