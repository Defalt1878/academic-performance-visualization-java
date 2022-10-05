package com.defalt.apv.util.parser;

import com.defalt.apv.report.person.Student;

public interface StudentLoader {
    Student load(String fullName, String groupName);
}
