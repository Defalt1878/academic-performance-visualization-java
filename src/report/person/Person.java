package report.person;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Person {
    protected final String fullName;
    protected final Gender gender;
    protected final LocalDate birthDate;
    protected final String hometown;

    public Person(String fullName, Gender gender, LocalDate birthDate, String hometown) {
        if (fullName == null)
            throw new IllegalArgumentException("Full name cannot be null!");
        this.fullName = fullName;

        this.gender = gender;
        this.birthDate = birthDate;
        this.hometown = hometown;
    }

    public String getFullName() {
        return fullName;
    }

    public Optional<Gender> getGender() {
        return Optional.ofNullable(gender);
    }

    public Optional<LocalDate> getBirthDate() {
        return Optional.ofNullable(birthDate);
    }

    public Optional<String> getHometown() {
        return Optional.ofNullable(hometown);
    }

    @Override
    public String toString() {
        var additionalInfo = Stream.of(getGender(), getBirthDate(), getHometown())
            .filter(Optional::isPresent)
            .map(op -> op.get().toString())
            .collect(Collectors.joining(", "));
        var result = getFullName();
        if (!additionalInfo.isBlank())
            result += String.format(" (%s)", additionalInfo);

        return result;
    }
}
