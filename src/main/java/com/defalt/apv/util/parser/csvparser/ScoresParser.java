package com.defalt.apv.util.parser.csvparser;

import com.defalt.apv.report.course.Course;
import com.defalt.apv.report.scores.CourseScores;
import com.defalt.apv.report.scores.ModuleScores;

import java.util.List;

public class ScoresParser {
    public static CourseScores parse(Course course, List<String> modules, List<String> tasks, List<Integer> scores) {
        var courseScores = new CourseScores(course);
        courseScores.setActivitiesScore(scores.get(0));
        courseScores.setExercisesScore(scores.get(1));
        courseScores.setHomeworksScore(scores.get(2));
        courseScores.setSeminarsScore(scores.get(3));

        var module = courseScores.getModuleScores(modules.get(4));
        for (var i = 5; i < tasks.size(); i++) {
            var moduleName = i < modules.size() ? modules.get(i) : "";
            if (moduleName.isBlank())
                parseTask(tasks.get(i), scores.get(i), module);
            else
                module = courseScores.getModuleScores(moduleName);
        }

        return courseScores;
    }

    private static void parseTask(String task, int score, ModuleScores module) {
        switch (task) {
            case "Акт" -> module.setActivitiesScore(score);
            case "Упр" -> module.setExercisesScore(score);
            case "ДЗ" -> module.setHomeworksScore(score);
            case "Сем" -> module.setSeminarsScore(score);
            case "Доп" -> module.getTaskScores(task).setScore(score);
            default -> {
                var tokens = task.split(": ");
                if (tokens.length != 2)
                    throw new IllegalArgumentException("Unknown token!");
                module.getTaskScores(tokens[1]).setScore(score);
            }
        }
    }
}