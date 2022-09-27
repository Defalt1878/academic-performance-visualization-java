package report;

import report.course.Course;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReportCard {
    private final Course maxScores;
    private final List<StudentScores> studentsScores;

    public ReportCard(Course maxScores, List<StudentScores> studentsScores) {
        this.maxScores = maxScores;
        this.studentsScores = studentsScores;
    }

    public int getStudentScore(String fullName) {
        return studentsScores.stream()
            .filter(studentScores -> studentScores.student().getFullName().equals(fullName))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Student not found!"))
            .scores().getFullScore();
    }

    public List<Student> getStudentsWithScore(int minInclusive, int maxExclusive) {
        return studentsScores.stream()
            .filter(studentScores -> {
                var fullScore = studentScores.scores().getFullScore();
                return fullScore >= minInclusive && fullScore < maxExclusive;
            })
            .map(StudentScores::student)
            .toList();
    }

    public Student getStudentWithMaxScore() {
        return studentsScores.stream()
            .max(Comparator.comparing(studentScores -> studentScores.scores().getFullScore()))
            .orElseThrow().student();
    }

    public Student getStudentWithModuleMaxScore(String moduleName) {
        return studentsScores.stream()
            .max(Comparator.comparing(
                studentScores -> Scores.getModule(studentScores.scores(), moduleName).getFullScore()
            ))
            .orElseThrow().student();
    }

    public Student getStudentWithTaskMaxScore(String taskName) {
        return studentsScores.stream()
            .max(Comparator.comparing(
                studentScores -> Scores.getTask(studentScores.scores(), taskName).getScore()
            ))
            .orElseThrow().student();
    }

    public List<Student> sortStudentsByScore() {
        return studentsScores.stream()
            .sorted(Collections.reverseOrder(
                Comparator.comparing(studentScores -> studentScores.scores().getFullScore())
            ))
            .map(StudentScores::student)
            .toList();
    }
}
