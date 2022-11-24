package com.defalt.apv.report.person;

import javax.annotation.Nullable;
import java.time.LocalDate;

public class Student extends Person {
    private final String groupName;

    public Student(String fullName, String groupName) {
        this(fullName, groupName, null, null, null);
    }

    public Student(
        String fullName, String groupName, @Nullable Gender gender, @Nullable LocalDate birthDate,
        @Nullable String hometown
    ) {
        super(fullName, gender, birthDate, hometown);

        if (groupName == null)
            throw new IllegalArgumentException("Group name cannot be null!");
        this.groupName = groupName;
    }

    public Student(
        int id, String fullName, String groupName, @Nullable Gender gender, @Nullable LocalDate birthDate,
        @Nullable String hometown
    ) {
        this(fullName, groupName, gender, birthDate, hometown);
    }

    public String getGroupName() {
        return groupName;
    }
}
