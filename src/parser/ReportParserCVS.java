package parser;

import report.ReportCard;
import report.Student;
import report.StudentScores;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ReportParserCVS {
    private String courseName;
    private List<String> modules;
    private List<String> tasks;

    public ReportParserCVS() {
    }

    public ReportCard parse(String courseName, String path) {
        this.courseName = courseName;
        try {
            return tryParse(path);
        } catch (IOException e) {
            throw new RuntimeException("Error happens during parsing", e);
        }
    }

    private ReportCard tryParse(String path) throws IOException {
        var lines = Files.readAllLines(Paths.get(path));
        modules = getDataStream(lines.get(0)).toList();
        tasks = getDataStream(lines.get(1)).toList();

        var maxes = getDataStream(lines.get(2)).map(Integer::parseInt).toList();
        var maxScores = ScoreParser.parse(courseName, modules, tasks, maxes);

        var studentScores = lines.stream()
            .skip(3)
            .map(this::parseStudentScores)
            .toList();

        return new ReportCard(maxScores, studentScores);
    }

    private static Stream<String> getDataStream(String line) {
        return Arrays.stream(line.split(";")).skip(2);
    }

    private StudentScores parseStudentScores(String line) {
        var data = line.split(";");
        var student = new Student(data[0], data[1]);
        var scores = Arrays.stream(data).skip(2).map(Integer::parseInt).toList();
        return new StudentScores(student, ScoreParser.parse(courseName, modules, tasks, scores));
    }
}
