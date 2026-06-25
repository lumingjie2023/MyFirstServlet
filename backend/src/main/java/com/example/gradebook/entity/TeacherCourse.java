package com.example.gradebook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teacher_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "teacher_username", nullable = false, length = 50)
    private String teacherUsername;
    
    @Column(name = "course_id", nullable = false, length = 20)
    private String courseId;
}