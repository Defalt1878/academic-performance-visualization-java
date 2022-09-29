package report.course;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
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
        exercises = new LinkedHashMap<>();
        homeworks = new LinkedHashMap<>();
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
        return Stream.concat(getExercises().stream(), getHomeworks().stream()).collect(Collectors.toList());
    }

    public Task getExercise(String exerciseName) {
        if (!exercises.containsKey(exerciseName))
            throw new IllegalArgumentException("No exercise was found with such name!");
        return exercises.get(exerciseName);
    }

    public Task getHomework(String homeWorkName) {
        if (!homeworks.containsKey(homeWorkName))
            throw new IllegalArgumentException("No homework was found with such name!");
        return homeworks.get(homeWorkName);
    }

    public Task getTask(String taskName) {
        var exercise = exercises.getOrDefault(taskName, null);
        var homework = homeworks.getOrDefault(taskName, null);
        if (exercise == null && homework == null)
            throw new IllegalArgumentException("No task was found with such name!");
        if (exercise != null && homework != null)
            throw new IllegalArgumentException("More than one match was found!");
        return exercise == null ? homework : exercise;
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
    //endregion

    public boolean containsExercise(String exerciseName) {
        return exercises.containsKey(exerciseName);
    }

    public boolean containsHomework(String homeworkName) {
        return homeworks.containsKey(homeworkName);
    }

    @Override
    public String toString() {
        return String.format("%s (Score: %s)", getName(), getFullScore());
    }
}
