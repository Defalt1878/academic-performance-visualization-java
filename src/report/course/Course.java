package report.course;

import java.util.*;

public class Course {
    private final String name;
    private final Map<String, Module> modules;

    private int activitiesScore;
    private int exercisesScore;
    private int homeworksScore;
    private int seminarsScore;

    public Course(String name) {
        this.name = name;
        modules = new HashMap<>();
    }

    //region setters
    public void addModule(Module module) {
        modules.put(module.getName(), module);
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

    public Map<String, Module> getModules() {
        return Collections.unmodifiableMap(modules);
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
