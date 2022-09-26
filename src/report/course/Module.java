package report.course;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Module {
    private final String name;

    private final List<Task> exercises;
    private final List<Task> homeworks;

    private int activitiesScore;
    private int exercisesScore;
    private int homeworksScore;
    private int seminarsScore;

    public Module(String name) {
        this.name = name;
        exercises = new ArrayList<>();
        homeworks = new ArrayList<>();
    }

    public void addExercise(Task exercise) {
        exercises.add(exercise);
    }

    public void addHomework(Task homework) {
        homeworks.add(homework);
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

    public String getName() {
        return name;
    }

    public Collection<Task> getExercises() {
        return Collections.unmodifiableCollection(exercises);
    }

    public Collection<Task> getHomeworks() {
        return Collections.unmodifiableCollection(homeworks);
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

    @Override
    public String toString() {
        return String.format("%s (Score: %s)", name,
                             activitiesScore + exercisesScore + homeworksScore + seminarsScore);
    }
}
