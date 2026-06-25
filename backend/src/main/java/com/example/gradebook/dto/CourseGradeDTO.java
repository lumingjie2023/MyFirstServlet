
package com.example.gradebook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseGradeDTO {
    private String studentId;
    private String studentName;
    private Double score;
    private String gradeLevel;
}
