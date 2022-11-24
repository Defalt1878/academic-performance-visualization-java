package com.defalt.apv.report.person;

import com.defalt.apv.report.Identifiable;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Person extends Identifiable {
    protected final String fullName;
    protected final Gender gender;
    protected final LocalDate birthdate;
    protected final String hometown;

    public Person(String fullName, Gender gender, LocalDate birthdate, String hometown) {
        if (fullName == null)
            throw new IllegalArgumentException("Full name cannot be null!");
        this.fullName = fullName;

        this.gender = gender;
        this.birthdate = birthdate;
        this.hometown = hometown;
    }

    public String getFullName() {
        return fullName;
    }

    public Optional<Gender> getGender() {
        return Optional.ofNullable(gender);
    }

    public Optional<LocalDate> getBirthdate() {
        return Optional.ofNullable(birthdate);
    }

    public Optional<String> getHometown() {
        return Optional.ofNullable(hometown);
    }

    @Override
    public String toString() {
        var additionalInfo = Stream.of(getGender(), getBirthdate(), getHometown())
            .filter(Optional::isPresent)
            .map(op -> op.get().toString())
            .collect(Collectors.joining(", "));
        var result = getFullName();
        if (!additionalInfo.isBlank())
            result += String.format(" (%s)", additionalInfo);

        return result;
    }
}
