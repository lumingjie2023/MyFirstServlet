
package com.example.gradebook.service;

import com.example.gradebook.dto.GradeDTO;
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
public class GradeService {
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    
    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository,
                       CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }
    
    public List<GradeDTO> getAllGrades() {
        List<Grade> grades = gradeRepository.findAll();
        return convertToDTOList(grades);
    }
    
    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id).orElseThrow();
    }
    
    public Grade saveGrade(Grade grade) {
        grade.setGradeLevel(calculateGradeLevel(grade.getScore()));
        return gradeRepository.save(grade);
    }
    
    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }
    
    public List<GradeDTO> getGradesByStudent(String studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        return convertToDTOList(grades);
    }
    
    public List<GradeDTO> getGradesByCourse(String courseId) {
        List<Grade> grades = gradeRepository.findByCourseId(courseId);
        return convertToDTOList(grades);
    }
    
    public List<GradeDTO> getGradesBySemester(String semester) {
        List<Grade> grades = gradeRepository.findBySemester(semester);
        return convertToDTOList(grades);
    }
    
    private List<GradeDTO> convertToDTOList(List<Grade> grades) {
        Map<String, Student> studentMap = studentRepository.findAll().stream()
                .collect(Collectors.toMap(Student::getStudentId, s -> s));
        Map<String, Course> courseMap = courseRepository.findAll().stream()
                .collect(Collectors.toMap(Course::getCourseId, c -> c));
        
        List<GradeDTO> result = new ArrayList<>();
        for (Grade grade : grades) {
            Student student = studentMap.get(grade.getStudentId());
            Course course = courseMap.get(grade.getCourseId());
            
            if (student != null && course != null) {
                result.add(new GradeDTO(
                        grade.getId(),
                        student.getStudentId(),
                        student.getName(),
                        course.getCourseId(),
                        course.getCourseName(),
                        grade.getScore(),
                        grade.getGradeLevel(),
                        grade.getSemester(),
                        grade.getExamType(),
                        course.getCredit()
                ));
            }
        }
        return result;
    }
    
    private String calculateGradeLevel(Double score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }
    
    public Double getGPA(String studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        Map<String, Course> courseMap = courseRepository.findAll().stream()
                .collect(Collectors.toMap(Course::getCourseId, c -> c));
        
        double totalCredit = 0;
        double totalPoints = 0;
        
        for (Grade grade : grades) {
            Course course = courseMap.get(grade.getCourseId());
            if (course != null && grade.getScore() >= 60) {
                totalCredit += course.getCredit();
                totalPoints += course.getCredit() * getGradePoints(grade.getGradeLevel());
            }
        }
        
        return totalCredit > 0 ? totalPoints / totalCredit : 0.0;
    }
    
    private double getGradePoints(String gradeLevel) {
        return switch (gradeLevel) {
            case "A" -> 4.0;
            case "B" -> 3.0;
            case "C" -> 2.0;
            case "D" -> 1.0;
            default -> 0.0;
        };
    }
}
