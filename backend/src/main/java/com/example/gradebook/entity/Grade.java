
package com.example.gradebook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id", nullable = false, length = 20)
    private String studentId;
    
    @Column(name = "course_id", nullable = false, length = 20)
    private String courseId;
    
    @Column(name = "score")
    private Double score;
    
    @Column(name = "grade_level", length = 10)
    private String gradeLevel;
    
    @Column(name = "semester", length = 20)
    private String semester;
    
    @Column(name = "exam_type", length = 20)
    private String examType;
}
