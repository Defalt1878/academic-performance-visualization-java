package com.defalt.apv.report;

import com.defalt.apv.report.course.Course;
import com.defalt.apv.report.course.Module;
import com.defalt.apv.report.person.Student;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReportCard {
    private final Course course;
    private final List<StudentScores> studentsScores;

    public ReportCard(Course course, List<StudentScores> studentsScores) {
        this.course = course;
        this.studentsScores = studentsScores;
    }

    public Course getCourse() {
        return course;
    }

    public List<StudentScores> getStudentsScores() {
        return Collections.unmodifiableList(studentsScores);
    }

    public List<Student> getStudents() {
        return studentsScores.stream()
            .map(StudentScores::student)
            .toList();
    }

    public List<String> getModules() {
        return course.getModules().stream().map(Module::getName).toList();
    }
}