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

    public Map<String, Task> getExercises() {
        return Collections.unmodifiableMap(exercises);
    }

    public Map<String, Task> getHomeworks() {
        return Collections.unmodifiableMap(homeworks);
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

    @Override
    public String toString() {
        return String.format("%s (Score: %s)", getName(), getFullScore());
    }
}
