
package com.example.gradebook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @Column(name = "course_id", length = 20)
    private String courseId;
    
    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;
    
    @Column(name = "credit", nullable = false)
    private Double credit;
    
    @Column(name = "teacher_name", length = 50)
    private String teacherName;
    
    @Column(name = "department", length = 50)
    private String department;
    
    @Column(name = "semester", length = 20)
    private String semester;
}
