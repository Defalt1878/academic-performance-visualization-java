package com.defalt.apv.report.course;

import java.util.Objects;

public final class Task {
    private final String name;
    private final TaskType type;
    private final int maxScore;

    public Task(String name, TaskType type, int maxScore) {
        this.name = Objects.requireNonNull(name, "Name cannot be null!");
        this.type = Objects.requireNonNull(type, "Type cannot be null!");
        if (maxScore < 0)
            throw new IllegalArgumentException("Value cannot be less than zero!");
        this.maxScore = maxScore;
    }

    //region getters
    public String getName() {
        return name;
    }

    public TaskType getType() {
        return type;
    }

    public int getMaxScores() {
        return maxScore;
    }
    //endregion

    //region overrides
    @Override
    public String toString() {
        return String.format("%s (Type: %s, Max score: %s)", getName(), getType().name(), getMaxScores());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Task task = (Task) o;
        return maxScore == task.maxScore && name.equals(task.name) && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, maxScore);
    }

    //endregion
}
