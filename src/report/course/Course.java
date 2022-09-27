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

    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    public Module getModule(String name){
        if (!modules.containsKey(name))
            throw new IllegalArgumentException("No module found with such name!");
        return modules.get(name);
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
