package com.defalt.apv.charts;

import com.defalt.apv.report.Grade;
import com.defalt.apv.report.ReportCard;
import com.defalt.apv.report.StudentScores;
import com.defalt.apv.report.person.Gender;
import com.defalt.apv.report.scores.ModuleScores;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReportChartBuilder {
    private final int imageHeight;
    private final boolean excludeWithZeroScore;
    private final Path chartsSavePath;

    public ReportChartBuilder(
        int imagesHeight,
        String chartsSavePath,
        boolean excludeWithZeroScore
    ) {
        this.imageHeight = imagesHeight;
        this.excludeWithZeroScore = excludeWithZeroScore;
        this.chartsSavePath = Paths.get(chartsSavePath).toAbsolutePath();
    }

    public void createChartsFor(ReportCard report) {
        saveChart(createGradesPieChartDataset(report, ss -> true, "Общая успеваемость"), 1);
        saveChart(
            createGradesPieChartDataset(
                report, ss -> ss.student().getGroupName().matches(".+У1.+"), "Успеваемость уровня комфорт"
            ), 1
        );
        saveChart(
            createGradesPieChartDataset(
                report, ss -> ss.student().getGroupName().matches(".+У2.+"), "Успеваемость уровня спорт"
            ), 1
        );
        saveChart(
            createGradesPieChartDataset(
                report, ss -> ss.student().getGender().orElse(null) == Gender.FEMALE, "Успеваемость девушек"
            ), 1

        );
        saveChart(
            createGradesPieChartDataset(
                report, ss -> ss.student().getGender().orElse(null) == Gender.MALE, "Успеваемость парней"
            ), 1
        );
        saveChart(createPopularCitiesChart(report), 2);
        saveChart(createAvgAcademicPerformancePerModuleChart(report), 2);
        saveChart(createAvgAcademicPerformancePerBirthdateMonthChart(report), 2);
    }

    private JFreeChart createGradesPieChartDataset(
        ReportCard report,
        Predicate<StudentScores> studentsFilter,
        String chartTitle
    ) {
        var dataset = new DefaultPieDataset<String>();

        report.getStudentsScores().stream()
            .filter(this::excludeZeroScoreIfEnabled)
            .filter(studentsFilter)
            .map(ss -> ss.getGradeInfo().getGrade())
            .collect(Collectors.groupingBy(grade -> grade, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> dataset.setValue(entry.getKey().toString(), entry.getValue()));

        var chart = ChartFactory.createPieChart(chartTitle, dataset);
        var plot = (PiePlot<String>) chart.getPlot();
        plot.setSectionPaint(Grade.TWO.toString(), Color.GRAY);
        plot.setSectionPaint(Grade.THREE.toString(), Color.RED);
        plot.setSectionPaint(Grade.FOUR.toString(), Color.YELLOW);
        plot.setSectionPaint(Grade.FIVE.toString(), Color.GREEN);

        return chart;
    }

    private JFreeChart createPopularCitiesChart(ReportCard report) {
        var dataset = new DefaultCategoryDataset();
        report.getStudentsScores().stream()
            .filter(this::excludeZeroScoreIfEnabled)
            .map(ss -> ss.student().getHometown().orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(hometown -> hometown, Collectors.counting()))
            .entrySet().stream()
            .sorted(Comparator.comparing(entry -> -entry.getValue()))
            .limit(10)
            .forEach(entry -> dataset.setValue(entry.getValue(), "Колличество студентов", entry.getKey()));

        return ChartFactory.createBarChart(
            "Распределение студентов по городам",
            "Город",
            "Количество студетов",
            dataset
        );
    }

    private JFreeChart createAvgAcademicPerformancePerModuleChart(ReportCard report) {
        var dataset = new DefaultCategoryDataset();

        var courseScores = report.getStudentsScores().stream()
            .filter(this::excludeZeroScoreIfEnabled)
            .toList();

        for (var module : report.getCourse().getModules()) {
            var moduleName = module.getName();
            var scores = courseScores.stream()
                .map(ss -> ss.scores().getModuleScores(moduleName))
                .toList();

            var fullAvgScore = scores.stream()
                .mapToDouble(ModuleScores::getResultScore)
                .average().orElse(0);

            var tasksAvgScore = scores.stream()
                .mapToDouble(moduleScores -> 100d * moduleScores.getExercisesScore() / module.getExercisesMaxScore())
                .average().orElse(0);

            var homeworksAvgScore = scores.stream()
                .mapToDouble(moduleScores -> 100d * moduleScores.getHomeworksScore() / module.getHomeworksMaxScore())
                .average().orElse(0);

            dataset.setValue(fullAvgScore, "Средний балл за весь модуль", moduleName);
            dataset.setValue(tasksAvgScore, "Средний балл за упражнения", moduleName);
            dataset.setValue(homeworksAvgScore, "Средний балл за практики", moduleName);
        }

        return ChartFactory.createBarChart(
            "Средние баллы за каждый модуль",
            "Модуль",
            "Средний балл",
            dataset
        );
    }

    private JFreeChart createAvgAcademicPerformancePerBirthdateMonthChart(ReportCard report) {
        var dataset = new DefaultCategoryDataset();
        report.getStudentsScores().stream()
            .filter(this::excludeZeroScoreIfEnabled)
            .filter(ss -> ss.student().getBirthdate().isPresent())
            .collect(Collectors.groupingBy(
                ss -> ss.student().getBirthdate().get().getMonth(),
                Collectors.averagingDouble(ss -> ss.scores().getResultScore()))
            ).entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .limit(10)
            .forEach(entry -> dataset.setValue(
                entry.getValue(),
                "Средний балл",
                entry.getKey().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
            ));

        return ChartFactory.createBarChart(
            "Средние баллы за курс по месяцу рождения",
            "Месяц рождения",
            "Средний балл",
            dataset
        );
    }

    private boolean excludeZeroScoreIfEnabled(StudentScores studentScores) {
        return !excludeWithZeroScore || studentScores.scores().getFullScore() > 0;
    }

    private void saveChart(JFreeChart chart, float ratio) {
        var fileName = chart.getTitle().getText() + ".png";
        var file = chartsSavePath.resolve(fileName).toFile();
        try {
            if (!Files.exists(chartsSavePath))
                Files.createDirectories(chartsSavePath);
            ChartUtils.saveChartAsPNG(file, chart, Math.round(ratio * imageHeight), imageHeight);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
