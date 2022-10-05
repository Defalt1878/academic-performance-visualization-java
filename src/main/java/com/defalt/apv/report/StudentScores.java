package com.defalt.apv.report;

import com.defalt.apv.report.course.Course;
import com.defalt.apv.report.person.Student;

public record StudentScores(Student student, Course scores) {

    @Override
    public String toString() {
        return String.format("%s - %s", student, scores);
    }
}
