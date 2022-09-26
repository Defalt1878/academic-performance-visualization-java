package report.course;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Course {
    private final String name;
    private final List<Module> modules;

    private int activitiesScore;
    private int exercisesScore;
    private int homeworksScore;
    private int seminarsScore;

    public Course(String name) {
        this.name = name;
        modules = new ArrayList<>();
    }

    public void addModule(Module module) {
        modules.add(module);
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

    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection(modules);
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
