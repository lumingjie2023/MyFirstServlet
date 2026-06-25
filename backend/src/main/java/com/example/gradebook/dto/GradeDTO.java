
package com.example.gradebook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO {
    private Long id;
    private String studentId;
    private String studentName;
    private String courseId;
    private String courseName;
    private Double score;
    private String gradeLevel;
    private String semester;
    private String examType;
    private Double credit;
}
