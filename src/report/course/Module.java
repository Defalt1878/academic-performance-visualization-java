package report.course;

import java.util.*;
import java.util.stream.Stream;

public class Module {
    private final String name;

    private final Map<String, Task> exercises;
    private final Map<String, Task> homeworks;

    private int activitiesScore;
    private int exercisesScore;
    private int homeworksScore;
    private int seminarsScore;

    public Module(String name) {
        this.name = name;
        exercises = new HashMap<>();
        homeworks = new HashMap<>();
    }

    //region setters
    public void addExercise(Task exercise) {
        exercises.put(exercise.getName(), exercise);
    }

    public void addHomework(Task homework) {
        homeworks.put(homework.getName(), homework);
    }

    public void setActivitiesScore(int activitiesScore) {
        this.activitiesScore = activitiesScore;
    }

    public void setExercisesScore(int exercisesScore) {
        this.exercisesScore = exercisesScore;
    }

    public void setHomeworksScore(int homeworksScore) {
        this.homeworksScore = homeworksScore;
    }

    public void setSeminarsScore(int seminarsScore) {
        this.seminarsScore = seminarsScore;
    }
    //endregion

    //region getters
    public String getName() {
        return name;
    }

    public Collection<Task> getExercises() {
        return Collections.unmodifiableCollection(exercises.values());
    }

    public Collection<Task> getHomeworks() {
        return Collections.unmodifiableCollection(homeworks.values());
    }

    public Collection<Task> getTasks() {
        return Stream.concat(getExercises().stream(), getHomeworks().stream()).toList();
    }

    public Task getTask(String name) {
        var exercise = exercises.getOrDefault(name, null);
        var homework = homeworks.getOrDefault(name, null);
        if (exercise == null && homework == null)
            throw new IllegalArgumentException("No task found with such name!");
        if (exercise != null && homework != null)
            throw new IllegalArgumentException("More than one match found!");
        return exercise == null
            ? homework
            : exercise;
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
    //endregion

    @Override
    public String toString() {
        return String.format("%s (Score: %s)", name,
                             activitiesScore + exercisesScore + homeworksScore + seminarsScore);
    }
}
