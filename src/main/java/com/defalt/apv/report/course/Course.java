package com.defalt.apv.report.course;

import java.util.*;

public class Course {
    private final String name;
    private final int activitiesScore;
    private final int exercisesScore;
    private final int homeworksScore;
    private final int seminarsScore;
    private final Map<String, Module> modules;

    public Course(
        String name, int activitiesScore, int exercisesScore, int homeworksScore, int seminarsScore,
        List<Module> modules
    ) {
        this.name = name;
        this.activitiesScore = activitiesScore;
        this.exercisesScore = exercisesScore;
        this.homeworksScore = homeworksScore;
        this.seminarsScore = seminarsScore;

        var modulesMap = new LinkedHashMap<String, Module>();
        modules.forEach(module -> modulesMap.put(module.getName(), module));
        this.modules = Collections.unmodifiableMap(modulesMap);
    }

    //region getters
    public String getName() {
        return name;
    }

    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    public Module getModule(String moduleName) {
        if (!modules.containsKey(moduleName))
            throw new IllegalArgumentException("No module was found with such name!");
        return modules.get(moduleName);
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

    public boolean containsModule(String moduleName) {
        return modules.containsKey(moduleName);
    }

    @Override
    public String toString() {
        return String.format("%s (Score: %s)", getName(), getFullScore());
    }
}
