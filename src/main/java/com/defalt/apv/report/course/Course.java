package com.defalt.apv.report.course;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Course {
    private final String name;
    private final int activitiesMaxScore;
    private final int exercisesMaxScore;
    private final int homeworksMaxScore;
    private final int seminarsMaxScore;
    private final Collection<Module> modules;

    public Course(
        String name, int activitiesMaxScore, int exercisesMaxScore,
        int homeworksMaxScore, int seminarsMaxScore, List<Module> modules
    ) {

        this.name = Objects.requireNonNull(name, "Name cannot be null!");
        this.modules =
            Collections.unmodifiableCollection(Objects.requireNonNull(modules, "Modules list cannot be null!"));

        this.activitiesMaxScore = checkNotNegative(activitiesMaxScore);
        this.exercisesMaxScore = checkNotNegative(exercisesMaxScore);
        this.homeworksMaxScore = checkNotNegative(homeworksMaxScore);
        this.seminarsMaxScore = checkNotNegative(seminarsMaxScore);
    }

    //region getters
    public String getName() {
        return name;
    }

    public Collection<Module> getModules() {
        return modules;
    }

    public Module getModule(String moduleName) {
        var modules = getModules().stream()
            .filter(module -> module.getName().equals(moduleName))
            .toList();
        if (modules.size() == 0)
            throw new IllegalArgumentException("No module was found with such name!");
        if (modules.size() > 1)
            throw new IllegalArgumentException("More than one module was found!");

        return modules.get(0);
    }

    public int getActivitiesMaxScore() {
        return activitiesMaxScore;
    }

    public int getExercisesMaxScore() {
        return exercisesMaxScore;
    }

    public int getHomeworksMaxScore() {
        return homeworksMaxScore;
    }

    public int getSeminarsMaxScore() {
        return seminarsMaxScore;
    }

    public int getMaxScore() {
        return getActivitiesMaxScore() + getExercisesMaxScore() + getHomeworksMaxScore() + getSeminarsMaxScore();
    }
    //endregion

    private int checkNotNegative(int value) {
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than zero!");
        return value;
    }

    //region overrides
    @Override
    public String toString() {
        return String.format("%s (Max score: %s)", getName(), getMaxScore());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Course course = (Course) o;
        return activitiesMaxScore == course.activitiesMaxScore &&
               exercisesMaxScore == course.exercisesMaxScore &&
               homeworksMaxScore == course.homeworksMaxScore &&
               seminarsMaxScore == course.seminarsMaxScore &&
               name.equals(course.name) && modules.equals(course.modules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, activitiesMaxScore, exercisesMaxScore, homeworksMaxScore, seminarsMaxScore, modules);
    }

    //endregion
}
