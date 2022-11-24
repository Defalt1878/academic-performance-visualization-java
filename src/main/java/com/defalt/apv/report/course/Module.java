package com.defalt.apv.report.course;

import com.defalt.apv.report.Identifiable;

import javax.naming.OperationNotSupportedException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Module extends Identifiable {
    private final String name;

    private final Collection<Task> tasks;

    private final int activitiesMaxScore;
    private final int exercisesMaxScore;
    private final int homeworksMaxScore;
    private final int seminarsMaxScore;

    public Module(
        String name, int activitiesMaxScore, int exercisesMaxScore,
        int homeworksMaxScore, int seminarsMaxScore, List<Task> tasks
    ) {
        this.name = Objects.requireNonNull(name, "Name cannot be null!");
        this.tasks =
            Collections.unmodifiableCollection(Objects.requireNonNull(tasks, "Tasks list cannot be null!"));

        this.activitiesMaxScore = checkNotNegative(activitiesMaxScore);
        this.exercisesMaxScore = checkNotNegative(exercisesMaxScore);
        this.homeworksMaxScore = checkNotNegative(homeworksMaxScore);
        this.seminarsMaxScore = checkNotNegative(seminarsMaxScore);
    }

    //region getters

    public String getName() {
        return name;
    }

    public Collection<Task> getExercises() {
        return tasks.stream().filter(task -> task.getType() == TaskType.EXERCISE).toList();
    }

    public Collection<Task> getHomeworks() {
        return tasks.stream().filter(task -> task.getType() == TaskType.HOMEWORK).toList();
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public Task getTask(String taskName) {
        var tasks = getTasks().stream().filter(task -> task.getName().equals(taskName)).toList();
        if (tasks.size() == 0)
            throw new IllegalArgumentException("No task was found with such name!");
        if (tasks.size() > 1)
            throw new IllegalArgumentException("More than one task was found!");

        return tasks.get(0);
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
        Module module = (Module) o;
        return activitiesMaxScore == module.activitiesMaxScore &&
               exercisesMaxScore == module.exercisesMaxScore &&
               homeworksMaxScore == module.homeworksMaxScore &&
               seminarsMaxScore == module.seminarsMaxScore &&
               name.equals(module.name) && tasks.equals(module.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tasks, activitiesMaxScore, exercisesMaxScore, homeworksMaxScore, seminarsMaxScore);
    }
    //endregion
}
