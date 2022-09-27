package report;

import report.course.Course;
import report.course.Module;
import report.course.Task;

import java.util.ArrayList;

public class Scores {
    public static Module getModule(Course course, String moduleName) {
        var modules = course.getModules();
        if (!modules.containsKey(moduleName))
            throw new IllegalArgumentException("No module was found with such name!");
        return modules.get(moduleName);
    }

    public static Task getTask(Course course, String taskName) {
        var results = new ArrayList<Task>();
        for (var module : course.getModules().values()) {
            try {
                results.add(getExercise(module, taskName));
            } catch (IllegalArgumentException ignored) {
            }
            try {
                results.add(getHomework(module, taskName));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (results.size() == 1)
            return results.get(0);
        if (results.size() == 0)
            throw new IllegalArgumentException("No task with such name was found!");
        throw new IllegalArgumentException("More than one match was found!");
    }

    public static Task getTask(Module module, String taskName) {
        var exercise = module.getExercises().getOrDefault(taskName, null);
        var homework = module.getHomeworks().getOrDefault(taskName, null);
        if (exercise == null && homework == null)
            throw new IllegalArgumentException("No task was found with such name!");
        if (exercise != null && homework != null)
            throw new IllegalArgumentException("More than one match was found!");
        return exercise == null
            ? homework
            : exercise;
    }

    public static Task getExercise(Module module, String exerciseName) {
        var exercises = module.getExercises();
        if (!exercises.containsKey(exerciseName))
            throw new IllegalArgumentException("No exercise was found in module with such name!");
        return exercises.get(exerciseName);
    }

    public static Task getHomework(Module module, String homeWorkName) {
        var homeworks = module.getExercises();
        if (!homeworks.containsKey(homeWorkName))
            throw new IllegalArgumentException("No homework was found in module with such name!");
        return homeworks.get(homeWorkName);
    }

    public static boolean containsModule(Course course, String moduleName) {
        return course.getModules().containsKey(moduleName);
    }

    public static boolean containsTask(Module module, String taskName) {
        return module.getExercises().containsKey(taskName) || module.getHomeworks().containsKey(taskName);
    }

    public static boolean containsTask(Course course, String taskName) {
        return course.getModules().values().stream().anyMatch(module -> containsTask(module, taskName));
    }
}
