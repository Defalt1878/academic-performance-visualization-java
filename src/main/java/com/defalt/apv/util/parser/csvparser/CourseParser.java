package com.defalt.apv.util.parser.csvparser;

import com.defalt.apv.report.course.Course;
import com.defalt.apv.report.course.Task;
import com.defalt.apv.report.course.TaskType;
import com.defalt.apv.util.parser.CourseBuilder;
import com.defalt.apv.util.parser.ModuleBuilder;

import java.util.List;

public class CourseParser {
    public static Course parse(String courseName, List<String> modules, List<String> tasks, List<Integer> maxScores) {
        var cb = new CourseBuilder(courseName)
            .setActivitiesMaxScore(maxScores.get(0))
            .setExercisesMaxScore(maxScores.get(1))
            .setHomeworksMaxScore(maxScores.get(2))
            .setSeminarsMaxScore(maxScores.get(3));

        var mb = new ModuleBuilder(modules.get(4));
        for (var i = 5; i < tasks.size(); i++) {
            var moduleName = i < modules.size() ? modules.get(i) : "";
            if (moduleName.isBlank()) {
                parseTask(tasks.get(i), maxScores.get(i), mb);
            } else {
                cb.addModule(mb.build());
                mb = new ModuleBuilder(moduleName);
            }
        }
        cb.addModule(mb.build());

        return cb.build();
    }

    private static void parseTask(String task, int score, ModuleBuilder mb) {
        switch (task) {
            case "Акт" -> mb.setActivitiesMaxScore(score);
            case "Упр" -> mb.setExercisesMaxScore(score);
            case "ДЗ" -> mb.setHomeworksMaxScore(score);
            case "Сем" -> mb.setSeminarsMaxScore(score);
            case "Доп" -> mb.addTask(new Task(task, TaskType.HOMEWORK, score));
            default -> {
                var tokens = task.split(": ");
                var type = switch (tokens[0]) {
                    case "Упр" -> TaskType.EXERCISE;
                    case "ДЗ" -> TaskType.HOMEWORK;
                    default -> throw new IllegalArgumentException("Unknown token!");
                };
                mb.addTask(new Task(tokens[1], type, score));
            }
        }
    }
}