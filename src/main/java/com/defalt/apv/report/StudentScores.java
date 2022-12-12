package com.defalt.apv.report;

import com.defalt.apv.report.person.Student;
import com.defalt.apv.report.scores.CourseScores;

public record StudentScores(Student student, CourseScores scores) {
    public GradeInfo getGradeInfo() {
        return GradeInfo.forScore(scores.getResultScore());
    }

    @Override
    public String toString() {
        return String.format("%s - %s", student(), scores());
    }
}
