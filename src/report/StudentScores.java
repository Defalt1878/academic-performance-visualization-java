package report;

import report.course.Course;

public record StudentScores(Student student, Course scores) {

    @Override
    public String toString() {
        return String.format("%s - %s", student, scores);
    }
}
