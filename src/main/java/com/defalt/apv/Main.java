package com.defalt.apv;

import com.defalt.apv.util.parser.VkStudentLoader;
import com.defalt.apv.util.parser.csvparser.CsvReportParser;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        var path = "src/main/resources/basicprogramming_2.csv";
        var stopwatch = new StopWatch();

        stopwatch.start();
        var vkStudentLoader = new VkStudentLoader("Екатеринбург", "УРФУ");
        var parser = new CsvReportParser(vkStudentLoader);
        var report = parser.parse("C#", path);
        stopwatch.stop();

        System.out.printf("Time: %s seconds%n%n", stopwatch.getTime(TimeUnit.SECONDS));

        var loadedStudents = vkStudentLoader.getStudentsIds().entrySet().stream()
            .map(entry -> String.format("%s; id: %s", entry.getKey(), entry.getValue()))
            .toList();

        System.out.println(loadedStudents.size());
        System.out.println();
        System.out.println(String.join("\n", loadedStudents));
    }
}
