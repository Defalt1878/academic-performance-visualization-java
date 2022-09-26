package report;

import java.time.LocalDate;

public class Student {
    private final String fullName;
    private final String groupName;
    private String hometown;
    private Gender gender;
    private LocalDate birthDate;

    public Student(String fullName, String groupName) {
        this.fullName = fullName;
        this.groupName = groupName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getHometown() {
        return hometown;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
