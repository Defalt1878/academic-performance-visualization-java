package report.person;

public enum Gender {
    MALE, FEMALE;

    public static Gender fromString(String gender) {
        return switch (gender.toLowerCase()) {
            case "male" -> MALE;
            case "female" -> FEMALE;
            default -> throw new IllegalArgumentException("Unknown gender!");
        };
    }
}
