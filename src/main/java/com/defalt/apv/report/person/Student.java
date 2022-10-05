package com.defalt.apv.report.person;

import java.time.LocalDate;

public class Student extends Person {
    private final String groupName;

    public Student(String fullName, String groupName) {
        this(fullName, groupName, null, null, null);
    }

    public Student(String fullName, String groupName, Gender gender, LocalDate birthDate, String hometown) {
        super(fullName, gender, birthDate, hometown);

        if (groupName == null)
            throw new IllegalArgumentException("Group name cannot be null!");
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
