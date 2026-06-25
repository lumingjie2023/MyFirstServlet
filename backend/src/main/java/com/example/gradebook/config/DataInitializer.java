
package com.example.gradebook.config;

import com.example.gradebook.entity.*;
import com.example.gradebook.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final GradeRepository gradeRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final PasswordEncoder passwordEncoder;
    
    public DataInitializer(UserRepository userRepository, StudentRepository studentRepository,
                          CourseRepository courseRepository, GradeRepository gradeRepository,
                          TeacherCourseRepository teacherCourseRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.gradeRepository = gradeRepository;
        this.teacherCourseRepository = teacherCourseRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin", passwordEncoder.encode("admin123"), "ADMIN", "管理员", "admin@university.edu"));
            userRepository.save(new User("teacher001", passwordEncoder.encode("teacher123"), "TEACHER", "张教授", "zhang@university.edu"));
            userRepository.save(new User("teacher002", passwordEncoder.encode("teacher123"), "TEACHER", "李老师", "li@university.edu"));
            userRepository.save(new User("2022001", passwordEncoder.encode("student123"), "STUDENT", "王明", "wangming@university.edu"));
            userRepository.save(new User("2022002", passwordEncoder.encode("student123"), "STUDENT", "李华", "lihua@university.edu"));
            userRepository.save(new User("2022003", passwordEncoder.encode("student123"), "STUDENT", "张伟", "zhangwei@university.edu"));
        }
        
        if (studentRepository.count() == 0) {
            studentRepository.save(new Student("2022001", "王明", "男", "计算机科学与技术", "计科2201班", "wangming@university.edu", "13800138001"));
            studentRepository.save(new Student("2022002", "李华", "女", "计算机科学与技术", "计科2201班", "lihua@university.edu", "13800138002"));
            studentRepository.save(new Student("2022003", "张伟", "男", "计算机科学与技术", "计科2202班", "zhangwei@university.edu", "13800138003"));
            studentRepository.save(new Student("2022004", "刘洋", "男", "软件工程", "软工2201班", "liuyang@university.edu", "13800138004"));
            studentRepository.save(new Student("2022005", "陈静", "女", "软件工程", "软工2201班", "chenjing@university.edu", "13800138005"));
            studentRepository.save(new Student("2022006", "杨帆", "男", "人工智能", "AI2201班", "yangfan@university.edu", "13800138006"));
            studentRepository.save(new Student("2022007", "赵雪", "女", "人工智能", "AI2201班", "zhaoxue@university.edu", "13800138007"));
            studentRepository.save(new Student("2022008", "孙磊", "男", "网络工程", "网工2201班", "sunlei@university.edu", "13800138008"));
        }
        
        if (courseRepository.count() == 0) {
            courseRepository.save(new Course("CS101", "高等数学", 4.0, "张教授", "数学学院", "2024-2025-1"));
            courseRepository.save(new Course("CS102", "大学英语", 3.0, "李老师", "外国语学院", "2024-2025-1"));
            courseRepository.save(new Course("CS103", "数据结构", 4.0, "王教授", "计算机学院", "2024-2025-1"));
            courseRepository.save(new Course("CS104", "操作系统", 4.0, "赵老师", "计算机学院", "2024-2025-2"));
            courseRepository.save(new Course("CS105", "计算机网络", 3.0, "刘教授", "计算机学院", "2024-2025-2"));
            courseRepository.save(new Course("CS106", "数据库原理", 3.0, "张教授", "计算机学院", "2024-2025-2"));
            courseRepository.save(new Course("CS107", "人工智能导论", 3.0, "陈教授", "人工智能学院", "2024-2025-1"));
            courseRepository.save(new Course("CS108", "软件工程", 4.0, "周老师", "计算机学院", "2024-2025-2"));
            courseRepository.save(new Course("CS109", "离散数学", 3.0, "张教授", "数学学院", "2024-2025-1"));
            courseRepository.save(new Course("CS110", "编译原理", 4.0, "王教授", "计算机学院", "2024-2025-2"));
            courseRepository.save(new Course("CS111", "算法设计", 4.0, "王教授", "计算机学院", "2024-2025-1"));
            courseRepository.save(new Course("CS112", "Web开发技术", 3.0, "刘教授", "计算机学院", "2024-2025-2"));
            courseRepository.save(new Course("CS113", "云计算基础", 3.0, "周老师", "计算机学院", "2024-2025-1"));
            courseRepository.save(new Course("CS114", "大数据导论", 3.0, "陈教授", "人工智能学院", "2024-2025-2"));
        }
        
        if (gradeRepository.count() == 0) {
            // 王明(2022001)的成绩 - 8门课程
            gradeRepository.save(new Grade(null, "2022001", "CS101", 85.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022001", "CS102", 92.0, "A", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022001", "CS103", 78.0, "C", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022001", "CS104", 88.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022001", "CS105", 95.0, "A", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022001", "CS106", 90.0, "A", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022001", "CS109", 82.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022001", "CS111", 87.0, "B", "2024-2025-1", "期末考试"));
            
            // 李华(2022002)的成绩 - 8门课程
            gradeRepository.save(new Grade(null, "2022002", "CS101", 90.0, "A", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022002", "CS102", 88.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022002", "CS103", 92.0, "A", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022002", "CS104", 85.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022002", "CS105", 80.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022002", "CS109", 86.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022002", "CS110", 91.0, "A", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022002", "CS112", 84.0, "B", "2024-2025-2", "期末考试"));
            
            // 张伟(2022003)的成绩 - 8门课程
            gradeRepository.save(new Grade(null, "2022003", "CS101", 75.0, "C", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022003", "CS102", 82.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022003", "CS103", 88.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022003", "CS106", 70.0, "C", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022003", "CS108", 85.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022003", "CS109", 78.0, "C", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022003", "CS111", 80.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022003", "CS113", 83.0, "B", "2024-2025-1", "期末考试"));
            
            // 刘洋(2022004)的成绩 - 8门课程
            gradeRepository.save(new Grade(null, "2022004", "CS101", 80.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022004", "CS102", 75.0, "C", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022004", "CS107", 92.0, "A", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022004", "CS106", 88.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022004", "CS108", 90.0, "A", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022004", "CS110", 85.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022004", "CS112", 87.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022004", "CS114", 91.0, "A", "2024-2025-2", "期末考试"));
            
            // 陈静(2022005)的成绩 - 8门课程
            gradeRepository.save(new Grade(null, "2022005", "CS101", 88.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022005", "CS102", 95.0, "A", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022005", "CS107", 85.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022005", "CS106", 92.0, "A", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022005", "CS108", 88.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022005", "CS109", 90.0, "A", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022005", "CS111", 86.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022005", "CS113", 89.0, "B", "2024-2025-1", "期末考试"));
            
            // 杨帆(2022006)的成绩 - 8门课程
            gradeRepository.save(new Grade(null, "2022006", "CS101", 92.0, "A", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022006", "CS102", 85.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022006", "CS107", 95.0, "A", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022006", "CS105", 90.0, "A", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022006", "CS108", 82.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022006", "CS110", 88.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022006", "CS112", 93.0, "A", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022006", "CS114", 87.0, "B", "2024-2025-2", "期末考试"));
            
            // 赵雪(2022007)的成绩 - 8门课程
            gradeRepository.save(new Grade(null, "2022007", "CS101", 85.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022007", "CS102", 90.0, "A", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022007", "CS107", 88.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022007", "CS105", 85.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022007", "CS108", 92.0, "A", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022007", "CS109", 84.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022007", "CS111", 91.0, "A", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022007", "CS113", 86.0, "B", "2024-2025-1", "期末考试"));
            
            // 孙磊(2022008)的成绩 - 8门课程
            gradeRepository.save(new Grade(null, "2022008", "CS101", 78.0, "C", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022008", "CS102", 80.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022008", "CS103", 85.0, "B", "2024-2025-1", "期末考试"));
            gradeRepository.save(new Grade(null, "2022008", "CS105", 90.0, "A", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022008", "CS106", 75.0, "C", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022008", "CS108", 82.0, "B", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022008", "CS110", 79.0, "C", "2024-2025-2", "期末考试"));
            gradeRepository.save(new Grade(null, "2022008", "CS112", 88.0, "B", "2024-2025-2", "期末考试"));
        }
        
        // 初始化教师-课程关联
        if (teacherCourseRepository.count() == 0) {
            // 张教授负责：高等数学、数据库原理、离散数学
            teacherCourseRepository.save(new TeacherCourse(null, "teacher001", "CS101"));
            teacherCourseRepository.save(new TeacherCourse(null, "teacher001", "CS106"));
            teacherCourseRepository.save(new TeacherCourse(null, "teacher001", "CS109"));
            
            // 李老师负责：大学英语、Web开发技术
            teacherCourseRepository.save(new TeacherCourse(null, "teacher002", "CS102"));
            teacherCourseRepository.save(new TeacherCourse(null, "teacher002", "CS112"));
        }
    }
}
