
package com.example.gradebook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentGradeDTO {
    private String courseName;
    private Double score;
    private String gradeLevel;
    private Double credit;
    private String semester;
}
