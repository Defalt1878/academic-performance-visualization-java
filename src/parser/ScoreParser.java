package parser;

import report.course.Course;
import report.course.Module;
import report.course.Task;

import java.util.List;

public class ScoreParser {
    public static Course parse(String courseName, List<String> modules, List<String> tasks, List<Integer> scores) {
        var course = new Course(courseName);
        setCourseScores(course, scores.subList(0, 4));

        var module = new Module(modules.get(4));
        for (var i = 0; i < tasks.size(); i++) {
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