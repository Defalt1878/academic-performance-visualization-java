package report;

import report.course.Course;

import java.util.List;

public class ReportCard {
    private final Course maxScores;
    private final List<StudentAchievements> studentsAchievements;

    public ReportCard(Course maxScores, List<StudentAchievements> studentsAchievements) {
        this.maxScores = maxScores;
        this.studentsAchievements = studentsAchievements;
    }
}
