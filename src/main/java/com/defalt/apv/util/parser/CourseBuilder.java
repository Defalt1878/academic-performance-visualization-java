package com.defalt.apv.util.parser;

import com.defalt.apv.report.course.Course;
import com.defalt.apv.report.course.Module;

import java.util.ArrayList;
import java.util.List;

public class CourseBuilder {
    private String name;
    private final List<Module> modules;

    private int activitiesMaxScore;
    private int exercisesMaxScore;
    private int homeworksMaxScore;
    private int seminarsMaxScore;

    public CourseBuilder() {
        this(null);
    }

    public CourseBuilder(String name) {
        this.name = name;
        modules = new ArrayList<>();
    }

    public CourseBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CourseBuilder addModule(Module module) {
        modules.add(module);
        return this;
    }

    public CourseBuilder setActivitiesMaxScore(int activitiesMaxScore) {
        this.activitiesMaxScore = activitiesMaxScore;
        return this;
    }

    public CourseBuilder setExercisesMaxScore(int exercisesMaxScore) {
        this.exercisesMaxScore = exercisesMaxScore;
        return this;
    }

    public CourseBuilder setHomeworksMaxScore(int homeworksMaxScore) {
        this.homeworksMaxScore = homeworksMaxScore;
        return this;
    }

    public CourseBuilder setSeminarsMaxScore(int seminarsMaxScore) {
        this.seminarsMaxScore = seminarsMaxScore;
        return this;
    }

    public Course build() {
        return new Course(name, activitiesMaxScore, exercisesMaxScore, homeworksMaxScore, seminarsMaxScore, modules);
    }
}
