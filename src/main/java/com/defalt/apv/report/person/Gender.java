package com.defalt.apv.report.person;

public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE");

    public final String Label;
    Gender(String label) {
        this.Label = label;
    }

    public static Gender FromString(String value) {
        return switch (value.toUpperCase()) {
            case "MALE" -> MALE;
            case "FEMALE" -> FEMALE;
            default -> throw new IllegalArgumentException("Unknown value");
        };
    }
}
