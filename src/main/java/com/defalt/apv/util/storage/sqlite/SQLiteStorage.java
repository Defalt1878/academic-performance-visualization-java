package com.defalt.apv.util.storage.sqlite;

import com.defalt.apv.report.Identifiable;
import com.defalt.apv.report.ReportCard;
import com.defalt.apv.report.StudentScores;
import com.defalt.apv.report.course.Course;
import com.defalt.apv.report.course.Module;
import com.defalt.apv.report.course.Task;
import com.defalt.apv.report.course.TaskType;
import com.defalt.apv.report.person.Gender;
import com.defalt.apv.report.person.Student;
import com.defalt.apv.report.scores.CourseScores;
import com.defalt.apv.report.scores.ModuleScores;
import com.defalt.apv.report.scores.TaskScores;
import com.defalt.apv.util.storage.ReportStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteStorage implements ReportStorage {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/report_card.sqlite3";

    private final Connection conn;

    public SQLiteStorage() throws SQLException {
        this.conn = DriverManager.getConnection(DB_URL);
    }

    @Override
    public void saveReportCard(ReportCard reportCard) {
        var course = reportCard.getCourse();
        saveCourse(course);
        for (var module : course.getModules()) {
            saveModule(course, module);
            for (var task : module.getTasks())
                saveTask(module, task);
        }
        reportCard.getStudents().forEach(this::saveStudent);
        reportCard.getStudentsScores().forEach(this::saveScores);
    }

    public void saveScores(StudentScores scores) {
        var student = scores.student();
        var courseScores = scores.scores();
        saveCourseScores(student, courseScores);
        for (var modulesScore : courseScores.getModulesScores()) {
            saveModuleScores(student, modulesScore);
            for (var taskScores : modulesScore.getTasksScores())
                saveTaskScores(student, taskScores);
        }
    }

    public void saveStudent(Student student) {
        checkHasNoId(student, "Student");
        var id = executeAddQuery(SQLiteModifyQueries.ADD_STUDENT, student.getFullName(), student.getGroupName(),
                                 student.getGender().isPresent() ? student.getGender().get().toString() : null,
                                 student.getBirthdate().isPresent() ? Date.valueOf(student.getBirthdate().get()) : null,
                                 student.getHometown().isPresent() ? student.getHometown().get() : null);
        student.setId(id);
    }

    public void saveCourse(Course course) {
        checkHasNoId(course, "Course");
        var id = executeAddQuery(SQLiteModifyQueries.ADD_COURSE, course.getName(), course.getHomeworksMaxScore(),
                                 course.getExercisesMaxScore(), course.getSeminarsMaxScore(),
                                 course.getActivitiesMaxScore());
        course.setId(id);
    }

    public void saveModule(Course parentCourse, Module module) {
        checkHasNoId(module, "Module");
        var id = executeAddQuery(SQLiteModifyQueries.ADD_MODULE, parentCourse.getId(), module.getName(),
                                 module.getHomeworksMaxScore(), module.getExercisesMaxScore(),
                                 module.getSeminarsMaxScore(), module.getActivitiesMaxScore());
        module.setId(id);
    }

    public void saveTask(Module parentModule, Task task) {
        checkHasNoId(task, "Task");
        var id = executeAddQuery(SQLiteModifyQueries.ADD_TASK, parentModule.getId(), task.getName(),
                                 task.getType().toString(), task.getMaxScores());
        task.setId(id);
    }

    public void saveCourseScores(Student student, CourseScores scores) {
        var studentId = student.getId();
        executeAddQuery(SQLiteModifyQueries.ADD_COURSE_SCORES, studentId, scores.getCourse().getId(),
                        scores.getHomeworksScore(), scores.getExercisesScore(), scores.getSeminarsScore(),
                        scores.getActivitiesScore());
    }

    public void saveModuleScores(Student student, ModuleScores scores) {
        var studentId = student.getId();
        executeAddQuery(SQLiteModifyQueries.ADD_MODULE_SCORES, studentId, scores.getModule().getId(),
                        scores.getHomeworksScore(), scores.getExercisesScore(), scores.getSeminarsScore(),
                        scores.getActivitiesScore());
    }

    public void saveTaskScores(Student student, TaskScores scores) {
        var studentId = student.getId();
        executeAddQuery(SQLiteModifyQueries.ADD_TASK_SCORES, studentId, scores.getTask().getId(), scores.getScore());
    }

    private void checkHasNoId(Identifiable identifiable, String name) {
        if (identifiable.hasId())
            throw new IllegalArgumentException(
                String.format("%s cannot be added, because it already has the id.", name));
    }

    private int executeAddQuery(String query, Object... args) {
        try (var statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            for (var i = 0; i < args.length; i++)
                statement.setObject(i + 1, args[i]);
            statement.execute();
            return getIdOfInserted(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getIdOfInserted(Statement statement) throws SQLException {
        var rs = statement.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);

        throw new IllegalArgumentException("No inserted row found in statement.");
    }

    @Override
    public List<Student> getStudents() {
        var students = new ArrayList<Student>();
        try (
            var result = executeSelectQuery(SQLiteSelectQueries.SELECT_STUDENTS)
        ) {
            if (result == null)
                throw new IllegalArgumentException("No students found!");

            while (result.next()) {
                var gender = result.getString("gender");
                var birthDate = result.getDate("birthdate");
                var student = new Student(
                    result.getString("full_name"),
                    result.getString("group_name"),
                    gender == null ? null : Gender.FromString(gender),
                    birthDate == null ? null : birthDate.toLocalDate(),
                    result.getString("hometown")
                );
                student.setId(result.getInt("id"));
                students.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

    @Override
    public Course getCourse(String name) {
        try (
            var result = executeSelectQuery(SQLiteSelectQueries.SELECT_COURSE, name)
        ) {
            if (result == null || !result.next())
                return null;

            var course = new Course(
                name,
                result.getInt("activities_max_score"),
                result.getInt("exercises_max_score"),
                result.getInt("homeworks_max_score"),
                result.getInt("seminars_max_score"),
                getModules(result.getInt("id"))
            );
            course.setId(result.getInt("id"));

            return course;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Module> getModules(int courseId) {
        var modules = new ArrayList<Module>();
        try (
            var result = executeSelectQuery(SQLiteSelectQueries.SELECT_MODULES, courseId)
        ) {
            if (result == null)
                return null;
            while (result.next()) {
                var module = new Module(
                    result.getString("name"),
                    result.getInt("activities_max_score"),
                    result.getInt("exercises_max_score"),
                    result.getInt("homeworks_max_score"),
                    result.getInt("seminars_max_score"),
                    getTasks(result.getInt("id"))
                );
                module.setId(result.getInt("id"));
                modules.add(module);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modules;
    }

    private List<Task> getTasks(int moduleId) {
        var tasks = new ArrayList<Task>();
        try (
            var result = executeSelectQuery(SQLiteSelectQueries.SELECT_TASKS, moduleId)
        ) {
            if (result == null)
                return null;
            while (result.next()) {
                var task = new Task(
                    result.getString("name"),
                    TaskType.FromString(result.getString("task_type")),
                    result.getInt("max_score")
                );
                task.setId(result.getInt("id"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    @Override
    public CourseScores getCourseScores(Student student, Course course) {
        var courseScores = new CourseScores(course);
        loadCourseScores(student, course, courseScores);
        for (var moduleScores : courseScores.getModulesScores()) {
            loadModuleScores(student, moduleScores.getModule(), moduleScores);
            for (var taskScores : moduleScores.getTasksScores())
                loadTaskScores(student, taskScores.getTask(), taskScores);
        }

        return courseScores;
    }

    @Override
    public ModuleScores getModuleScores(Student student, Module module) {
        var moduleScores = new ModuleScores(module);
        loadModuleScores(student, module, moduleScores);
        for (var taskScores : moduleScores.getTasksScores())
            loadTaskScores(student, taskScores.getTask(), taskScores);

        return moduleScores;
    }

    @Override
    public TaskScores getTaskScores(Student student, Task task) {
        var taskScores = new TaskScores(task);
        loadTaskScores(student, task, taskScores);
        return taskScores;
    }

    private void loadCourseScores(Student student, Course course, CourseScores dest) {
        try (
            var result = executeSelectQuery(SQLiteSelectQueries.SELECT_COURSE_SCORES, student.getId(), course.getId())
        ) {
            if (result == null || !result.next())
                throw new IllegalArgumentException("No course scores found!");
            dest.setActivitiesScore(result.getInt("activities_score"));
            dest.setExercisesScore(result.getInt("exercises_score"));
            dest.setHomeworksScore(result.getInt("homeworks_score"));
            dest.setSeminarsScore(result.getInt("seminars_score"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadModuleScores(Student student, Module module, ModuleScores dest) {
        try (
            var result = executeSelectQuery(SQLiteSelectQueries.SELECT_MODULE_SCORES, student.getId(), module.getId())
        ) {
            if (result == null || !result.next())
                throw new IllegalArgumentException("No module scores found!");
            dest.setActivitiesScore(result.getInt("activities_score"));
            dest.setExercisesScore(result.getInt("exercises_score"));
            dest.setHomeworksScore(result.getInt("homeworks_score"));
            dest.setSeminarsScore(result.getInt("seminars_score"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTaskScores(Student student, Task task, TaskScores dest) {
        try (var result = executeSelectQuery(SQLiteSelectQueries.SELECT_TASK_SCORES, student.getId(), task.getId())) {
            if (result == null || !result.next())
                throw new IllegalArgumentException("No task scores found!");
            dest.setScore(result.getInt("score"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet executeSelectQuery(String query, Object... args) throws SQLException {
        var statement = conn.prepareStatement(query);
        for (var i = 0; i < args.length; i++)
            statement.setObject(i + 1, args[i]);
        return statement.executeQuery();
    }

    @Override
    public void clearAll() {
        try (var statement = conn.createStatement()) {
            for (var sql : SQLiteModifyQueries.CLEAR_ALL.split(";"))
                statement.addBatch(sql);
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws SQLException {
        if (conn != null)
            conn.close();
    }
}
