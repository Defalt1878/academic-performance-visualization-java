package com.defalt.apv.report.scores;

import com.defalt.apv.report.course.Course;

import java.util.List;
import java.util.Objects;

public class CourseScores {
    private final Course course;
    private final List<ModuleScores> modulesScores;

    private int activitiesScore;
    private int exercisesScore;
    private int homeworksScore;
    private int seminarsScore;

    public CourseScores(Course course) {
        this.course = Objects.requireNonNull(course, "Course cannot be null!");
        this.modulesScores = course.getModules().stream().map(ModuleScores::new).toList();
    }

    //region getters
    public Course getCourse() {
        return course;
    }

    public List<ModuleScores> getModulesScores() {
        return modulesScores;
    }

    public ModuleScores getModuleScores(String moduleName) {
        var modules = getModulesScores().stream()
            .filter(moduleScores -> moduleScores.getModule().getName().equals(moduleName))
            .toList();
        if (modules.size() == 0)
            throw new IllegalArgumentException("No module was found with such name!");
        if (modules.size() > 1)
            throw new IllegalArgumentException("More than one module was found!");

        return modules.get(0);
    }

    public int getActivitiesScore() {
        return activitiesScore;
    }

    public int getExercisesScore() {
        return exercisesScore;
    }

    public int getHomeworksScore() {
        return homeworksScore;
    }

    public int getSeminarsScore() {
        return seminarsScore;
    }

    public int getFullScore() {
        return getActivitiesScore() + getExercisesScore() + getHomeworksScore() + getSeminarsScore();
    }

    public double getResultScore() {
        var current = getExercisesScore() + getHomeworksScore();
        var max = course.getExercisesMaxScore() + course.getHomeworksMaxScore();
        return 100d * current / max;
    }
    //endregion

    //region setters
    public void setActivitiesScore(int value) {
        this.activitiesScore = checkCorrectValue(value, getCourse().getActivitiesMaxScore());
    }

    public void setExercisesScore(int value) {
        this.exercisesScore = checkCorrectValue(value, getCourse().getExercisesMaxScore());
    }

    public void setHomeworksScore(int value) {
        this.homeworksScore = checkCorrectValue(value, getCourse().getHomeworksMaxScore());
    }

    public void setSeminarsScore(int value) {
        this.seminarsScore = checkCorrectValue(value, getCourse().getSeminarsMaxScore());
    }
    //endregion

    private int checkCorrectValue(int value, int max) {
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than zero!");
        if (value > max)
            throw new IllegalArgumentException("Value cannot be greater than max!");
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s (Score: %s)", getCourse().getName(), getFullScore());
    }
}
