package com.defalt.apv.util;

import com.defalt.apv.report.course.Course;
import com.defalt.apv.report.course.Module;

import java.util.ArrayList;
import java.util.List;

public class CourseBuilder {
    private String name;
    private final List<Module> modules;

    private int activitiesScore;
    private int exercisesScore;
    private int homeworksScore;
    private int seminarsScore;

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

    public CourseBuilder setActivitiesScore(int activitiesScore) {
        this.activitiesScore = activitiesScore;
        return this;
    }

    public CourseBuilder setExercisesScore(int exercisesScore) {
        this.exercisesScore = exercisesScore;
        return this;
    }

    public CourseBuilder setHomeworksScore(int homeworksScore) {
        this.homeworksScore = homeworksScore;
        return this;
    }

    public CourseBuilder setSeminarsScore(int seminarsScore) {
        this.seminarsScore = seminarsScore;
        return this;
    }

    public Course build() {
        return new Course(name, activitiesScore, exercisesScore, homeworksScore, seminarsScore, modules);
    }
}
