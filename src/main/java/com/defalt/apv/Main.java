package com.defalt.apv;

import com.defalt.apv.report.StudentScores;
import com.defalt.apv.report.person.Student;
import com.defalt.apv.util.parser.csvparser.CsvReportParser;
import com.defalt.apv.util.parser.vkloader.VkStudentLoader;
import com.defalt.apv.util.storage.ReportStorage;
import com.defalt.apv.util.storage.sqlite.SQLiteStorage;
import org.apache.commons.lang3.time.StopWatch;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        var path = "src/main/resources/basicprogramming_2.csv";
        var stopwatch = new StopWatch();

        stopwatch.start();
//        var vkStudentLoader = new VkStudentLoader("RU", "Екатеринбург", "УРФУ");
//        var parser = new CsvReportParser(vkStudentLoader);
//        var report = parser.parse("C#", path);
        try (ReportStorage storage = new SQLiteStorage()) {
//            storage.clearAll();
//            storage.saveReportCard(report);
            var course = storage.getCourse("C#");
            var firstStudent = storage.getStudents().get(0);
            var scores = storage.getCourseScores(firstStudent, course);
            var studentScores = new StudentScores(firstStudent, scores);
            System.out.println(studentScores);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stopwatch.stop();

        System.out.printf("Time: %s seconds%n%n", stopwatch.getTime(TimeUnit.SECONDS));
    }
}
