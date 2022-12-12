package com.defalt.apv.report.scores;

import com.defalt.apv.report.course.Module;

import java.util.List;

public class ModuleScores {
    private final Module module;

    private final List<TaskScores> tasksScores;
    private int activitiesScore;
    private int exercisesScore;
    private int homeworksScore;
    private int seminarsScore;

    public ModuleScores(Module module) {
        this.module = module;
        this.tasksScores = module.getTasks().stream().map(TaskScores::new).toList();
    }

    public Module getModule() {
        return module;
    }

    public List<TaskScores> getTasksScores() {
        return tasksScores;
    }

    public TaskScores getTaskScores(String taskName) {
        var tasks = getTasksScores().stream()
            .filter(taskScores -> taskScores.getTask().getName().equals(taskName))
            .toList();
        if (tasks.size() == 0)
            throw new IllegalArgumentException("No task was found with such name!");
        if (tasks.size() > 1)
            throw new IllegalArgumentException("More than one task was found!");

        return tasks.get(0);
    }

    //region getters
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
        var max = module.getExercisesMaxScore() + module.getHomeworksMaxScore();
        return 100d * current / max;
    }
    //endregion

    //region setters
    public void setActivitiesScore(int value) {
        this.activitiesScore = checkCorrectValue(value, getModule().getActivitiesMaxScore());
    }

    public void setExercisesScore(int value) {
        this.exercisesScore = checkCorrectValue(value, getModule().getExercisesMaxScore());
    }

    public void setHomeworksScore(int value) {
        this.homeworksScore = checkCorrectValue(value, getModule().getHomeworksMaxScore());
    }

    public void setSeminarsScore(int value) {
        this.seminarsScore = checkCorrectValue(value, getModule().getSeminarsMaxScore());
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
        return String.format("%s (Score: %s)", getModule().getName(), getFullScore());
    }
}
