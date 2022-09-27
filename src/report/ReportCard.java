package report;

import report.course.Course;

import java.util.List;

public class ReportCard {
    private final Course maxScores;
    private final List<StudentScores> studentsAchievements;

    public ReportCard(Course maxScores, List<StudentScores> studentsAchievements) {
        this.maxScores = maxScores;
        this.studentsAchievements = studentsAchievements;
    }
}
