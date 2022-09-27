import parser.ReportParserCVS;
import report.Student;

import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        var path = "basicprogramming_2.csv";
        var parser = new ReportParserCVS();
        var report = parser.parse("C#", path);

        var sorted = report.sortStudentsByScore().stream()
            .map(Student::getFullName)
            .map(student -> String.format("%s\t\t%s", student, report.getStudentScore(student)))
            .collect(Collectors.joining("\n"));
        System.out.println(sorted);
    }
}
