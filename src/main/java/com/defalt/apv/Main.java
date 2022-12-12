package com.defalt.apv;

import com.defalt.apv.charts.ReportChartBuilder;
import com.defalt.apv.report.ReportCard;
import com.defalt.apv.report.StudentScores;
import com.defalt.apv.util.parser.csvparser.CsvReportParser;
import com.defalt.apv.util.parser.vkloader.VkStudentLoader;
import com.defalt.apv.util.storage.ReportStorage;
import com.defalt.apv.util.storage.sqlite.SQLiteStorage;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {
        var report = getReport();
        var chartBuilder = new ReportChartBuilder(1000, "charts", true);
        chartBuilder.createChartsFor(report);
    }

    private static ReportCard getReport() {
        var reportCard = loadReportFromStorage();
        if (reportCard == null) {
            reportCard = parseReport();
            clearStorageAndSaveCard(reportCard);
        }
        return reportCard;
    }

    private static ReportCard loadReportFromStorage() {
        try (ReportStorage storage = new SQLiteStorage()) {
            var course = storage.getCourse("C#");
            if (course == null)
                return null;

            var students = storage.getStudents();
            var scores = students.stream()
                .map(student -> new StudentScores(student, storage.getCourseScores(student, course)))
                .toList();

            return new ReportCard(course, scores);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ReportCard parseReport() {
        var studentLoader = new VkStudentLoader("RU", "Екатеринбург", "УРФУ");
        var parser = new CsvReportParser(studentLoader);
        return parser.parse("C#", "src/main/resources/basicprogramming_3.csv");
    }

    private static void clearStorageAndSaveCard(ReportCard reportCard) {
        try (ReportStorage storage = new SQLiteStorage()) {
            storage.clearAll();
            storage.saveReportCard(reportCard);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
