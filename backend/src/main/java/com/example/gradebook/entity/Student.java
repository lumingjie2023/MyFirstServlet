
package com.example.gradebook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @Column(name = "student_id", length = 20)
    private String studentId;
    
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @Column(name = "gender", length = 10)
    private String gender;
    
    @Column(name = "major", nullable = false, length = 50)
    private String major;
    
    @Column(name = "class_name", nullable = false, length = 50)
    private String className;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "phone", length = 20)
    private String phone;
}
