package com.defalt.apv.util.parser.vkloader;

import com.defalt.apv.report.person.Student;
import com.defalt.apv.util.parser.StudentLoader;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class VkStudentLoader implements StudentLoader {
    private final Map<Student, Integer> studentsIds = new LinkedHashMap<>();
    private final UserInfoLoader userInfoLoader;
    private final StudentParser studentParser;

    public VkStudentLoader(String countryCode, String cityName, String universityName) {
        userInfoLoader = new UserInfoLoader(
            Objects.requireNonNull(countryCode, "Country code cannot be null!"),
            Objects.requireNonNull(cityName, "City name cannot be null!"),
            Objects.requireNonNull(universityName, "University name cannot be null!"));

        studentParser = new StudentParser();
    }

    @Override
    public Student load(String fullName, String groupName) {
        var userFullOptional = userInfoLoader.findUser(fullName);

        if (userFullOptional.isEmpty())
            return new Student(fullName, groupName);

        var userFull = userFullOptional.get();
        var student = studentParser.parse(fullName, groupName, userFull);
        studentsIds.put(student, userFull.getId());

        return student;
    }

    public Map<Student, Integer> getStudentsIds() {
        return Collections.unmodifiableMap(studentsIds);
    }
}
