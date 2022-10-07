package com.defalt.apv.util.parser.csvparser;

import com.defalt.apv.report.ReportCard;
import com.defalt.apv.report.StudentScores;
import com.defalt.apv.report.course.Course;
import com.defalt.apv.util.parser.ReportCardParser;
import com.defalt.apv.util.parser.StudentLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CsvReportParser implements ReportCardParser {
    private final StudentLoader studentLoader;

    public CsvReportParser(StudentLoader studentLoader) {
        this.studentLoader = studentLoader;
    }

    public ReportCard parse(String courseName, String filePath) {
        try {
            return tryParse(courseName, filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error happens during reading file!", e);
        }
    }

    private ReportCard tryParse(String courseName, String path) throws IOException {
        var lines = Files.readAllLines(Paths.get(path));
        var modules = getDataStream(lines.get(0)).toList();
        var tasks = getDataStream(lines.get(1)).toList();

        var maxes = getDataStream(lines.get(2)).map(Integer::parseInt).toList();
        var course = CourseParser.parse(courseName, modules, tasks, maxes);

        var studentsScores = lines.stream()
            .skip(3)
            .map(line -> parseStudentScores(line.split(";"), course, modules, tasks))
            .toList();

        return new ReportCard(course, studentsScores);
    }

    private StudentScores parseStudentScores(
        String[] data, Course course, List<String> modules, List<String> tasks
    ) {
        var student = studentLoader.load(data[0], data[1]);
        var scores = Arrays.stream(data).skip(2).map(Integer::parseInt).toList();
        return new StudentScores(student, ScoresParser.parse(course, modules, tasks, scores));
    }

    private static Stream<String> getDataStream(String line) {
        return Arrays.stream(line.split(";")).skip(2);
    }
}