package com.defalt.apv.util;

import com.defalt.apv.report.course.Module;
import com.defalt.apv.report.course.Task;

import java.util.ArrayList;
import java.util.List;

public class ModuleBuilder {
    private String name;

    private final List<Task> exercises;
    private final List<Task> homeworks;

    private int activitiesScore;
    private int exercisesScore;
    private int homeworksScore;
    private int seminarsScore;

    public ModuleBuilder() {
        this(null);
    }

    public ModuleBuilder(String name) {
        this.name = name;
        exercises = new ArrayList<>();
        homeworks = new ArrayList<>();
    }

    public ModuleBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ModuleBuilder addExercise(Task exercise) {
        exercises.add(exercise);
        return this;
    }

    public ModuleBuilder addHomework(Task homework) {
        homeworks.add(homework);
        return this;
    }

    public ModuleBuilder setActivitiesScore(int activitiesScore) {
        this.activitiesScore = activitiesScore;
        return this;
    }

    public ModuleBuilder setExercisesScore(int exercisesScore) {
        this.exercisesScore = exercisesScore;
        return this;
    }

    public ModuleBuilder setHomeworksScore(int homeworksScore) {
        this.homeworksScore = homeworksScore;
        return this;
    }

    public ModuleBuilder setSeminarsScore(int seminarsScore) {
        this.seminarsScore = seminarsScore;
        return this;
    }

    public Module build() {
        return new Module(name, activitiesScore, exercisesScore, homeworksScore, seminarsScore, exercises, homeworks);
    }
}
