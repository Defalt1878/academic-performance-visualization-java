package com.defalt.apv.util.parser.vkloader;

import com.defalt.apv.report.person.Gender;
import com.defalt.apv.report.person.Student;
import com.vk.api.sdk.objects.base.Sex;
import com.vk.api.sdk.objects.users.UserFull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class StudentParser {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");

    public Student parse(String fullName, String groupName, UserFull user) {
        return new Student(fullName, groupName, getGender(user), getBirthdate(user), getHomeTown(user));
    }

    private Gender getGender(UserFull userFull) {
        var sex = userFull.getSex();
        if (sex == Sex.MALE)
            return Gender.MALE;
        if (sex == Sex.FEMALE)
            return Gender.FEMALE;
        return null;
    }

    private LocalDate getBirthdate(UserFull userFull) {
        var bDate = userFull.getBdate();
        if (bDate == null)
            return null;

        if (bDate.split("\\.").length == 2)
            bDate += ".0001";

        return LocalDate.parse(bDate, dateFormatter);
    }

    private String getHomeTown(UserFull userFull) {
        var homeTown = userFull.getHomeTown();
        if (homeTown != null && !homeTown.isBlank())
            return homeTown;

        var city = userFull.getCity();
        var cityTitle = city == null ? "" : city.getTitle();
        return cityTitle.isBlank() ? null : cityTitle;
    }
}
