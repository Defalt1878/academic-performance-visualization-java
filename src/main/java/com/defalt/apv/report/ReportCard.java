package com.defalt.apv.report;

import com.defalt.apv.report.course.Course;
import com.defalt.apv.report.course.Module;
import com.defalt.apv.report.person.Student;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReportCard {
    private final Course maxScores;
    private final List<StudentScores> studentsScores;

    public ReportCard(Course maxScores, List<StudentScores> studentsScores) {
        this.maxScores = maxScores;
        this.studentsScores = studentsScores;
    }

    public Course getMaxScores() {
        return maxScores;
    }

    public Collection<StudentScores> getStudentsScores() {
        return Collections.unmodifiableCollection(studentsScores);
    }

    public Collection<Student> getStudents() {
        return studentsScores.stream()
            .map(StudentScores::student)
            .toList();
    }

    public Collection<String> getModules() {
        return maxScores.getModules().stream().map(Module::getName).toList();
    }
}