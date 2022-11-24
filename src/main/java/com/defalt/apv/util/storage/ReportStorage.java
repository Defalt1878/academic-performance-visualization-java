package com.defalt.apv.util.storage;

import com.defalt.apv.report.ReportCard;
import com.defalt.apv.report.course.Course;
import com.defalt.apv.report.course.Module;
import com.defalt.apv.report.course.Task;
import com.defalt.apv.report.person.Student;
import com.defalt.apv.report.scores.CourseScores;
import com.defalt.apv.report.scores.ModuleScores;
import com.defalt.apv.report.scores.TaskScores;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface ReportStorage extends AutoCloseable {
    void saveReportCard(ReportCard reportCard);

    List<Student> getStudents();

    Course getCourse(String name);

    CourseScores getCourseScores(Student student, Course course);

    ModuleScores getModuleScores(Student student, Module module);

    TaskScores getTaskScores(Student student, Task task);

    void clearAll();

    @Override
    void close() throws SQLException;
}
