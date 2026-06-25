package com.example.gradebook.controller;

import com.example.gradebook.entity.Course;
import com.example.gradebook.entity.TeacherCourse;
import com.example.gradebook.service.TeacherCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher-course")
public class TeacherCourseController {
    
    @Autowired
    private TeacherCourseService teacherCourseService;
    
    // 获取教师负责的所有课程
    @GetMapping("/teacher/{username}")
    public ResponseEntity<List<Course>> getTeacherCourses(@PathVariable String username) {
        List<Course> courses = teacherCourseService.getTeacherCourses(username);
        return ResponseEntity.ok(courses);
    }
    
    // 为教师分配课程
    @PostMapping("/assign")
    public ResponseEntity<?> assignCourse(@RequestBody Map<String, String> request) {
        String teacherUsername = request.get("teacherUsername");
        String courseId = request.get("courseId");
        
        if (teacherUsername == null || courseId == null) {
            return ResponseEntity.badRequest().body("教师用户名和课程ID不能为空");
        }
        
        teacherCourseService.assignCourseToTeacher(teacherUsername, courseId);
        return ResponseEntity.ok("课程分配成功");
    }
    
    // 移除教师的课程
    @PostMapping("/remove")
    public ResponseEntity<?> removeCourse(@RequestBody Map<String, String> request) {
        String teacherUsername = request.get("teacherUsername");
        String courseId = request.get("courseId");
        
        if (teacherUsername == null || courseId == null) {
            return ResponseEntity.badRequest().body("教师用户名和课程ID不能为空");
        }
        
        teacherCourseService.removeCourseFromTeacher(teacherUsername, courseId);
        return ResponseEntity.ok("课程移除成功");
    }
    
    // 获取所有教师-课程关联
    @GetMapping("/all")
    public ResponseEntity<List<TeacherCourse>> getAllTeacherCourses() {
        List<TeacherCourse> teacherCourses = teacherCourseService.getAllTeacherCourses();
        return ResponseEntity.ok(teacherCourses);
    }
}