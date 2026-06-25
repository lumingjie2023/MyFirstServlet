
package com.example.gradebook.controller;

import com.example.gradebook.dto.GradeDTO;
import com.example.gradebook.dto.StudentGradeDTO;
import com.example.gradebook.entity.Student;
import com.example.gradebook.service.GradeService;
import com.example.gradebook.service.StudentService;
import com.example.gradebook.service.TeacherCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentController {
    private final StudentService studentService;
    private final TeacherCourseService teacherCourseService;
    private final GradeService gradeService;
    
    public StudentController(StudentService studentService, TeacherCourseService teacherCourseService, GradeService gradeService) {
        this.studentService = studentService;
        this.teacherCourseService = teacherCourseService;
        this.gradeService = gradeService;
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        // 获取当前登录用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        
        List<Student> allStudents = studentService.getAllStudents();
        
        // 如果是教师，只返回选修了他负责课程的学生
        if ("ROLE_TEACHER".equals(role)) {
            List<String> teacherCourseIds = teacherCourseService.getTeacherCourseIds(username);
            List<GradeDTO> allGrades = gradeService.getAllGrades();
            
            // 找出选修了教师负责课程的学生ID
            List<String> studentIds = allGrades.stream()
                    .filter(g -> teacherCourseIds.contains(g.getCourseId()))
                    .map(GradeDTO::getStudentId)
                    .distinct()
                    .collect(Collectors.toList());
            
            // 过滤学生列表
            List<Student> filteredStudents = allStudents.stream()
                    .filter(s -> studentIds.contains(s.getStudentId()))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(filteredStudents);
        }
        
        // 管理员和学生可以看到所有学生
        return ResponseEntity.ok(allStudents);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String keyword) {
        List<Student> students = studentService.searchStudents(keyword);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/{id}/grades")
    public ResponseEntity<List<StudentGradeDTO>> getStudentGrades(@PathVariable String id) {
        List<StudentGradeDTO> grades = studentService.getStudentGrades(id);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/major/{major}")
    public ResponseEntity<List<Student>> getStudentsByMajor(@PathVariable String major) {
        List<Student> students = studentService.getStudentsByMajor(major);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/class/{className}")
    public ResponseEntity<List<Student>> getStudentsByClass(@PathVariable String className) {
        List<Student> students = studentService.getStudentsByClass(className);
        return ResponseEntity.ok(students);
    }
    
    @PostMapping("/add")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student savedStudent = studentService.saveStudent(student);
        return ResponseEntity.ok(savedStudent);
    }
    
    @PutMapping("/update")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.saveStudent(student);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}
