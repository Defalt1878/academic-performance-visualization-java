package report;

import report.course.Course;
import report.person.Student;

public record StudentScores(Student student, Course scores) {

    @Override
    public String toString() {
        return String.format("%s - %s", student, scores);
    }
}
