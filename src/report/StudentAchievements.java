package report;

import report.course.Course;

public class StudentAchievements {
    private final Student student;
    private final Course scores;

    public StudentAchievements(Student student, Course scores) {
        this.student = student;
        this.scores = scores;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", student, scores);
    }
}
