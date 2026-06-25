
package com.example.gradebook.service;

import com.example.gradebook.dto.StudentGradeDTO;
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
public class StudentService {
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final CourseRepository courseRepository;
    
    public StudentService(StudentRepository studentRepository, GradeRepository gradeRepository, 
                         CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.courseRepository = courseRepository;
    }
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Student getStudentById(String studentId) {
        return studentRepository.findById(studentId).orElseThrow();
    }
    
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
    
    public void deleteStudent(String studentId) {
        studentRepository.deleteById(studentId);
    }
    
    public List<Student> searchStudents(String keyword) {
        List<Student> byId = studentRepository.findByStudentIdContaining(keyword);
        List<Student> byName = studentRepository.findByNameContaining(keyword);
        List<Student> byMajor = studentRepository.findByMajor(keyword);
        List<Student> byClass = studentRepository.findByClassName(keyword);
        
        return byId.stream()
                .filter(s -> !byName.contains(s))
                .collect(Collectors.toList())
                .stream()
                .filter(s -> !byMajor.contains(s))
                .collect(Collectors.toList())
                .stream()
                .filter(s -> !byClass.contains(s))
                .collect(Collectors.toList());
    }
    
    public List<StudentGradeDTO> getStudentGrades(String studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        Map<String, Course> courseMap = courseRepository.findAll().stream()
                .collect(Collectors.toMap(Course::getCourseId, c -> c));
        
        List<StudentGradeDTO> result = new ArrayList<>();
        for (Grade grade : grades) {
            Course course = courseMap.get(grade.getCourseId());
            if (course != null) {
                result.add(new StudentGradeDTO(
                        course.getCourseName(),
                        grade.getScore(),
                        grade.getGradeLevel(),
                        course.getCredit(),
                        grade.getSemester()
                ));
            }
        }
        return result;
    }
    
    public List<Student> getStudentsByMajor(String major) {
        return studentRepository.findByMajor(major);
    }
    
    public List<Student> getStudentsByClass(String className) {
        return studentRepository.findByClassName(className);
    }
}
