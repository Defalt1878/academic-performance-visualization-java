package com.defalt.apv.report.course;

public enum TaskType {
    EXERCISE("EXERCISE"),
    HOMEWORK("HOMEWORK");

    public final String Label;

    TaskType(String label) {
        this.Label = label;
    }

    public static TaskType FromString(String value) {
        return switch (value.toUpperCase()) {
            case "EXERCISE" -> EXERCISE;
            case "HOMEWORK" -> HOMEWORK;
            default -> throw new IllegalArgumentException("Unknown value");
        };
    }
}
