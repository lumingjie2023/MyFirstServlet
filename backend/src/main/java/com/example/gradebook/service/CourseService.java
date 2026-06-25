
package com.example.gradebook.service;

import com.example.gradebook.dto.CourseGradeDTO;
import com.example.gradebook.entity.Course;
import com.example.gradebook.entity.Grade;
import com.example.gradebook.entity.Student;
import com.example.gradebook.repository.CourseRepository;
import com.example.gradebook.repository.GradeRepository;
import com.example.gradebook.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    
    public CourseService(CourseRepository courseRepository, GradeRepository gradeRepository,
                        StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
    }
    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public Course getCourseById(String courseId) {
        return courseRepository.findById(courseId).orElseThrow();
    }
    
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }
    
    public void deleteCourse(String courseId) {
        courseRepository.deleteById(courseId);
    }
    
    public List<Course> searchCourses(String keyword) {
        List<Course> byId = courseRepository.findAll().stream()
                .filter(c -> c.getCourseId().contains(keyword))
                .collect(Collectors.toList());
        List<Course> byName = courseRepository.findByCourseNameContaining(keyword);
        List<Course> byTeacher = courseRepository.findByTeacherNameContaining(keyword);
        List<Course> byDept = courseRepository.findByDepartment(keyword);
        
        return byId.stream()
                .filter(c -> !byName.contains(c))
                .collect(Collectors.toList())
                .stream()
                .filter(c -> !byTeacher.contains(c))
                .collect(Collectors.toList())
                .stream()
                .filter(c -> !byDept.contains(c))
                .collect(Collectors.toList());
    }
    
    public List<CourseGradeDTO> getCourseGrades(String courseId) {
        List<Grade> grades = gradeRepository.findByCourseId(courseId);
        Map<String, Student> studentMap = studentRepository.findAll().stream()
                .collect(Collectors.toMap(Student::getStudentId, s -> s));
        
        List<CourseGradeDTO> result = new ArrayList<>();
        for (Grade grade : grades) {
            Student student = studentMap.get(grade.getStudentId());
            if (student != null) {
                result.add(new CourseGradeDTO(
                        student.getStudentId(),
                        student.getName(),
                        grade.getScore(),
                        grade.getGradeLevel()
                ));
            }
        }
        return result;
    }
    
    public List<Course> getCoursesByDepartment(String department) {
        return courseRepository.findByDepartment(department);
    }
    
    public List<Course> getCoursesBySemester(String semester) {
        return courseRepository.findBySemester(semester);
    }
}
