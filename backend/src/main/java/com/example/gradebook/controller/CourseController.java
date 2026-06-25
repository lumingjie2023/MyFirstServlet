
package com.example.gradebook.controller;

import com.example.gradebook.dto.CourseGradeDTO;
import com.example.gradebook.entity.Course;
import com.example.gradebook.service.CourseService;
import com.example.gradebook.service.TeacherCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "*")
public class CourseController {
    private final CourseService courseService;
    private final TeacherCourseService teacherCourseService;
    
    public CourseController(CourseService courseService, TeacherCourseService teacherCourseService) {
        this.courseService = courseService;
        this.teacherCourseService = teacherCourseService;
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourses() {
        // 获取当前登录用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        
        // 如果是教师，只返回他负责的课程
        if ("ROLE_TEACHER".equals(role)) {
            List<Course> teacherCourses = teacherCourseService.getTeacherCourses(username);
            return ResponseEntity.ok(teacherCourses);
        }
        
        // 管理员和学生可以看到所有课程
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam String keyword) {
        List<Course> courses = courseService.searchCourses(keyword);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/{id}/grades")
    public ResponseEntity<List<CourseGradeDTO>> getCourseGrades(@PathVariable String id) {
        List<CourseGradeDTO> grades = courseService.getCourseGrades(id);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/department/{department}")
    public ResponseEntity<List<Course>> getCoursesByDepartment(@PathVariable String department) {
        List<Course> courses = courseService.getCoursesByDepartment(department);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<Course>> getCoursesBySemester(@PathVariable String semester) {
        List<Course> courses = courseService.getCoursesBySemester(semester);
        return ResponseEntity.ok(courses);
    }
    
    @PostMapping("/add")
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        Course savedCourse = courseService.saveCourse(course);
        return ResponseEntity.ok(savedCourse);
    }
    
    @PutMapping("/update")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
        Course updatedCourse = courseService.saveCourse(course);
        return ResponseEntity.ok(updatedCourse);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
}
