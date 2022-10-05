package com.defalt.apv.util.parser;

import com.defalt.apv.report.ReportCard;
import com.defalt.apv.report.StudentScores;
import com.defalt.apv.report.course.Course;
import com.defalt.apv.report.course.Task;
import com.defalt.apv.util.CourseBuilder;
import com.defalt.apv.util.ModuleBuilder;

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

        var studentsScores = lines.stream()
            .skip(3)
            .map(this::parseStudentScores)
            .toList();

        return new ReportCard(maxScores, studentsScores);
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
        var cb = new CourseBuilder(courseName)
            .setActivitiesScore(scores.get(0))
            .setExercisesScore(scores.get(1))
            .setHomeworksScore(scores.get(2))
            .setSeminarsScore(scores.get(3));

        var mb = new ModuleBuilder(modules.get(4));
        for (var i = 5; i < tasks.size(); i++) {
            if (i >= modules.size() || modules.get(i).isEmpty()) {
                parseTask(tasks.get(i), scores.get(i), mb);
            } else {
                cb.addModule(mb.build());
                mb = new ModuleBuilder(modules.get(i));
            }
        }
        cb.addModule(mb.build());

        return cb.build();
    }

    private static void parseTask(String task, int score, ModuleBuilder mb) {
        switch (task) {
            case "Акт" -> mb.setActivitiesScore(score);
            case "Упр" -> mb.setExercisesScore(score);
            case "ДЗ" -> mb.setHomeworksScore(score);
            case "Сем" -> mb.setSeminarsScore(score);
            case "Доп" -> mb.addHomework(new Task(task, score));
            default -> {
                var tokens = task.split(": ");
                switch (tokens[0]) {
                    case "Упр" -> mb.addExercise(new Task(tokens[1], score));
                    case "ДЗ" -> mb.addHomework(new Task(tokens[1], score));
                    default -> throw new IllegalArgumentException("Unknown token!");
                }
            }
        }
    }
}