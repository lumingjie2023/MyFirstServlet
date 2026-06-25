
package com.example.gradebook.controller;

import com.example.gradebook.dto.GradeDTO;
import com.example.gradebook.entity.Grade;
import com.example.gradebook.service.GradeService;
import com.example.gradebook.service.TeacherCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grade")
@CrossOrigin(origins = "*")
public class GradeController {
    private static final Logger logger = LoggerFactory.getLogger(GradeController.class);
    
    private final GradeService gradeService;
    private final TeacherCourseService teacherCourseService;
    
    public GradeController(GradeService gradeService, TeacherCourseService teacherCourseService) {
        this.gradeService = gradeService;
        this.teacherCourseService = teacherCourseService;
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<GradeDTO>> getAllGrades() {
        // 获取当前登录用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        
        logger.info("用户 {} 请求成绩列表，角色: {}", username, role);
        
        List<GradeDTO> allGrades = gradeService.getAllGrades();
        
        // 如果是教师，只返回他负责的课程的成绩
        if ("ROLE_TEACHER".equals(role)) {
            List<String> teacherCourseIds = teacherCourseService.getTeacherCourseIds(username);
            logger.info("教师 {} 负责的课程IDs: {}", username, teacherCourseIds);
            
            List<GradeDTO> filteredGrades = allGrades.stream()
                    .filter(g -> teacherCourseIds.contains(g.getCourseId()))
                    .collect(Collectors.toList());
            
            logger.info("过滤后成绩数量: {}", filteredGrades.size());
            return ResponseEntity.ok(filteredGrades);
        }
        
        // 管理员和学生可以看到所有成绩
        logger.info("返回所有成绩，数量: {}", allGrades.size());
        return ResponseEntity.ok(allGrades);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        Grade grade = gradeService.getGradeById(id);
        return ResponseEntity.ok(grade);
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeDTO>> getGradesByStudent(@PathVariable String studentId) {
        List<GradeDTO> grades = gradeService.getGradesByStudent(studentId);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<GradeDTO>> getGradesByCourse(@PathVariable String courseId) {
        List<GradeDTO> grades = gradeService.getGradesByCourse(courseId);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<GradeDTO>> getGradesBySemester(@PathVariable String semester) {
        List<GradeDTO> grades = gradeService.getGradesBySemester(semester);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/gpa/{studentId}")
    public ResponseEntity<Double> getGPA(@PathVariable String studentId) {
        Double gpa = gradeService.getGPA(studentId);
        return ResponseEntity.ok(gpa);
    }
    
    @PostMapping("/add")
    public ResponseEntity<Grade> addGrade(@RequestBody Grade grade) {
        Grade savedGrade = gradeService.saveGrade(grade);
        return ResponseEntity.ok(savedGrade);
    }
    
    @PutMapping("/update")
    public ResponseEntity<Grade> updateGrade(@RequestBody Grade grade) {
        Grade updatedGrade = gradeService.saveGrade(grade);
        return ResponseEntity.ok(updatedGrade);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.ok().build();
    }
}
