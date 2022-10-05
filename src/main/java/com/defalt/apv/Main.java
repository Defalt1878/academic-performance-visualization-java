package com.defalt.apv;

import com.defalt.apv.report.person.Student;
import com.defalt.apv.util.parser.CsvReportParser;
import com.defalt.apv.util.parser.ReportCardParser;
import com.defalt.apv.util.parser.VkStudentLoader;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        var stopwatch = new StopWatch();
        stopwatch.start();
        var path = "src/main/resources/basicprogramming_2.csv";
        ReportCardParser parser = new CsvReportParser(new VkStudentLoader("Екатеринбург", "УРФУ"));
        var report = parser.parse("C#", path);
        stopwatch.stop();
        System.out.printf("Time: %s seconds%n%n", stopwatch.getTime(TimeUnit.SECONDS));
        var loadedStudents = report.getStudents().stream()
            .filter(student -> student.getGender().isPresent())
            .map(Student::toString)
            .toList();
        System.out.println(loadedStudents.size());
        System.out.println();
        System.out.println(String.join("\n", loadedStudents));
    }
}
