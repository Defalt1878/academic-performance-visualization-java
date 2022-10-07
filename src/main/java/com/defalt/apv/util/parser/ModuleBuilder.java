package com.defalt.apv.util.parser;

import com.defalt.apv.report.course.Module;
import com.defalt.apv.report.course.Task;

import java.util.ArrayList;
import java.util.List;

public class ModuleBuilder {
    private String name;

    private final List<Task> tasks;

    private int activitiesMaxScore;
    private int exercisesMaxScore;
    private int homeworksMaxScore;
    private int seminarsMaxScore;

    public ModuleBuilder() {
        this(null);
    }

    public ModuleBuilder(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public ModuleBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ModuleBuilder addTask(Task task) {
        this.tasks.add(task);
        return this;
    }

    public ModuleBuilder setActivitiesMaxScore(int activitiesMaxScore) {
        this.activitiesMaxScore = activitiesMaxScore;
        return this;
    }

    public ModuleBuilder setExercisesMaxScore(int exercisesMaxScore) {
        this.exercisesMaxScore = exercisesMaxScore;
        return this;
    }

    public ModuleBuilder setHomeworksMaxScore(int homeworksMaxScore) {
        this.homeworksMaxScore = homeworksMaxScore;
        return this;
    }

    public ModuleBuilder setSeminarsMaxScore(int seminarsMaxScore) {
        this.seminarsMaxScore = seminarsMaxScore;
        return this;
    }

    public Module build() {
        return new Module(name, activitiesMaxScore, exercisesMaxScore, homeworksMaxScore, seminarsMaxScore, tasks);
    }
}
