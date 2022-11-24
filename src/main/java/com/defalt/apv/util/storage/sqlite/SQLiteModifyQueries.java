package com.defalt.apv.util.storage.sqlite;

public class SQLiteModifyQueries {
    public static final String ADD_STUDENT =
        "INSERT INTO Students('full_name', 'group_name', 'gender', 'birthdate', 'hometown') VALUES(?, ?, ?, ?, ?)";
    public static final String ADD_COURSE =
        "INSERT INTO Courses('name', 'homeworks_max_score', 'exercises_max_score', 'seminars_max_score', 'activities_max_score') VALUES(?, ?, ?, ?, ?)";
    public static final String ADD_MODULE =
        "INSERT INTO Modules('course_id', 'name', 'homeworks_max_score', 'exercises_max_score', 'seminars_max_score', 'activities_max_score') VALUES(?, ?, ?, ?, ?, ?)";
    public static final String ADD_TASK =
        "INSERT INTO Tasks('module_id', 'name', 'task_type', 'max_score') VALUES(?, ?, ?, ?)";
    public static final String ADD_COURSE_SCORES =
        "INSERT INTO CoursesScores('student_id', 'course_id', 'homeworks_score', 'exercises_score', 'seminars_score', 'activities_score') VALUES(?, ?, ?, ?, ?, ?)";
    public static final String ADD_MODULE_SCORES =
        "INSERT INTO ModulesScores('student_id', 'module_id', 'homeworks_score', 'exercises_score', 'seminars_score', 'activities_score') VALUES(?, ?, ?, ?, ?, ?)";
    public static final String ADD_TASK_SCORES =
        "INSERT INTO TasksScores('student_id', 'task_id', 'score') VALUES(?, ?, ?)";

    public static final String CLEAR_ALL =
        "DELETE FROM TasksScores;DELETE FROM ModulesScores;DELETE FROM CoursesScores;DELETE FROM Tasks;DELETE FROM Modules;DELETE FROM Courses;DELETE FROM Students;VACUUM;";
}
