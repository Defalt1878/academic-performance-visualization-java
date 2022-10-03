package utils.parser;

import report.person.Student;

public interface StudentLoader {
    Student load(String fullName, String groupName);
}
