package utils.parser;

import report.ReportCard;
import report.StudentScores;
import report.course.Course;
import report.course.Module;
import report.course.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CsvReportParser implements ReportCardParser {
    private final StudentLoader studentLoader;
    private String courseName;
    private List<String> modules;
    private List<String> tasks;

    public CsvReportParser(StudentLoader studentLoader) {
        this.studentLoader = studentLoader;
    }

    public ReportCard parse(String courseName, String filePath) {
        this.courseName = courseName;
        try {
            return tryParse(filePath);
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
        var student = studentLoader.load(data[0], data[1]);
        var scores = Arrays.stream(data).skip(2).map(Integer::parseInt).toList();
        return new StudentScores(student, ScoreParser.parse(courseName, modules, tasks, scores));
    }
}

class ScoreParser {
    public static Course parse(String courseName, List<String> modules, List<String> tasks, List<Integer> scores) {
        var course = new Course(courseName);
        setCourseScores(course, scores.subList(0, 4));

        var module = new Module(modules.get(4));
        for (var i = 5; i < tasks.size(); i++) {
            if (i >= modules.size() || modules.get(i).isEmpty()) {
                parseTask(tasks.get(i), scores.get(i), module);
            } else {
                course.addModule(module);
                module = new Module(modules.get(i));
            }
        }
        course.addModule(module);

        return course;
    }

    private static void setCourseScores(Course course, List<Integer> scores) {
        course.setActivitiesScore(scores.get(0));
        course.setExercisesScore(scores.get(1));
        course.setHomeworksScore(scores.get(2));
        course.setSeminarsScore(scores.get(3));
    }

    private static void parseTask(String task, int score, Module module) {
        switch (task) {
            case "Акт" -> module.setActivitiesScore(score);
            case "Упр" -> module.setExercisesScore(score);
            case "ДЗ" -> module.setHomeworksScore(score);
            case "Сем" -> module.setSeminarsScore(score);
            case "Доп" -> module.addHomework(new Task(task, score));
            default -> {
                var tokens = task.split(": ");
                switch (tokens[0]) {
                    case "Упр" -> module.addExercise(new Task(tokens[1], score));
                    case "ДЗ" -> module.addHomework(new Task(tokens[1], score));
                    default -> throw new IllegalArgumentException("Unknown token!");
                }
            }
        }
    }
}