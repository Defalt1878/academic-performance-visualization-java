package com.defalt.apv.util.storage.sqlite;

public class SQLiteSelectQueries {
    public static final String SELECT_COURSE_SCORES =
        "SELECT * FROM CoursesScores WHERE student_id = ? and course_id = ?";
    public static final String SELECT_MODULE_SCORES =
        "SELECT * FROM ModulesScores WHERE student_id = ? and module_id = ?";
    public static final String SELECT_TASK_SCORES =
        "SELECT * FROM TasksScores WHERE student_id = ? and task_id = ?";

    public static final String SELECT_COURSE = "SELECT * FROM Courses WHERE name = ?";

    public static final String SELECT_MODULES = "SELECT * FROM Modules WHERE course_id = ?";

    public static final String SELECT_TASKS = "SELECT * FROM Tasks WHERE module_id = ?";

    public static final String SELECT_STUDENTS = "SELECT * FROM Students";
}
