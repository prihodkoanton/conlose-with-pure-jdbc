package com.foxminded.aprihodko.menu;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.dao.GroupDao;
import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.console.Console;
import com.foxminded.aprihodko.menu.console.DefaultConsole;
import com.foxminded.aprihodko.model.Course;
import com.foxminded.aprihodko.model.Group;
import com.foxminded.aprihodko.model.Students;

@ExtendWith(MockitoExtension.class)
class AppMenuTest {

    // printWriter -[writes]-> pipedOutputStream -[content for]-> pipedInputStream // input for console
    PipedOutputStream pipedOutputStream = new PipedOutputStream();
    PipedInputStream pipedInputStream = new PipedInputStream();

    PrintWriter printWriter = new PrintWriter(pipedOutputStream);

    // console -[writes]-> outputPrintStream -[writes]-> byteArrayOutputStream
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream outputPrintStream = new PrintStream(byteArrayOutputStream);

    @Spy
    Console console = new DefaultConsole(pipedInputStream, outputPrintStream);

    @Mock
    Datasource datasource;

    @Mock
    GroupDao groupDao;

    @Mock
    StudentDao studentsDao;

    @Mock
    CourseDao courseDao;

    @InjectMocks
    AppMenu appMenu;

    @Mock
    Connection connection;

    private String consoleOutput;
    
    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.LENIENT);

    @BeforeEach
    void setUp() throws SQLException, IOException {
        pipedInputStream.connect(pipedOutputStream);
        when(datasource.getConnection()).thenReturn(connection);
    }

    @Test
    void shouldEditCourses() throws SQLException, IOException {
        Course course = new Course(1L, "course", "course description");
        when(courseDao.findAll(connection)).thenReturn(Arrays.asList(course));
        when(courseDao.findById(connection, 1L)).thenReturn(Optional.of(course));
        runTest(2L, "1", "1", "1", "1");
        assertTrue(consoleOutput.contains("Edit course 'course' with dicreption: 'course description'"));
    }
    
    @Test
    void shouldReturnAllCourses() throws SQLException, IOException{
        Course course = new Course(1L, "course", "course description");
        when(courseDao.findAll(connection)).thenReturn(Arrays.asList(course));
        runTest(2L, "1", "2");
        assertTrue(consoleOutput.contains("course"));
    }

    @Test
    void shouldEditStudents() throws SQLException, IOException {
        Students student = new Students(1L, 1, "john", "doe");
        when(studentsDao.findAll(connection)).thenReturn(Arrays.asList(student));
        when(studentsDao.findById(connection, 1L)).thenReturn(Optional.of(student));
        runTest(2L, "2", "1", "1", "1");
        assertTrue(consoleOutput.contains("Enter new first student name:"));
    }
    
    @Test
    void shouldDeleteStudents() throws SQLException, IOException {
        Students student = new Students(1L, 1, "john", "doe");
        when(studentsDao.findAll(connection)).thenReturn(Arrays.asList(student));
        when(studentsDao.findById(connection, 1L)).thenReturn(Optional.of(student));
        studentsDao.deleteById(connection, 1L);
        runTest(2L, "2", "2", "1");
        assertTrue(consoleOutput.contains("Delete student john doe with id: '1'"));
    }
    
    @Test
    void shouldAddStudents() throws SQLException, IOException {
        Students student = new Students(1L, 1, "john", "doe");
        studentsDao.save(connection, student);
        when(studentsDao.findById(connection, 1L)).thenReturn(Optional.of(student));
        runTest(2L, "2", "3", "john");
        System.out.println(consoleOutput);
        assertTrue(consoleOutput.contains("Create first student name:"));
    }
    
    @Test
    void shouldFindAllStudentsRelatedToCourseWithGivenName() throws SQLException, IOException{
        Students student = new Students(1L, 1, "john", "doe");
        Course course = new Course(1L, "course", "course description");
        studentsDao.assignCourseToStudent(connection, student.getId(), course.getId());
        runTest(2L, "2", "4", "test");
        assertTrue(consoleOutput.contains("Enter course name:"));
    }
    
    @Test
    void shouldRemoveTheStudentFromOneHisCourse () throws SQLException, IOException{
        Students student = new Students(1L, 1, "john", "doe");
        Course course = new Course(1L, "course", "course description");
        when(studentsDao.findById(connection, 1L)).thenReturn(Optional.of(student));
        when(courseDao.findById(connection, 1L)).thenReturn(Optional.of(course));
        studentsDao.assignCourseToStudent(connection, student.getId(), course.getId());
        studentsDao.removeTheStudentFromOneHisCourse(connection, 1L, 1L);
        runTest(2L, "2", "5");
        System.out.println(consoleOutput);
        assertTrue(consoleOutput.contains("Enter student id:"));
    }
    
    @Test
    void shouldReturnAllStudents () throws SQLException, IOException{
        Students student = new Students(1L, 1, "john", "doe");
        when(studentsDao.findAll(connection)).thenReturn(Arrays.asList(student));
        runTest(2L, "2","6");
        assertTrue(consoleOutput.contains("john doe"));
    }

    @Test
    void shouldEditGroups() throws SQLException, IOException {
        Group group = new Group(1L, "group");
        when(groupDao.findAll(connection)).thenReturn(Arrays.asList(group));
        when(groupDao.findById(connection, 1L)).thenReturn(Optional.of(group));
        runTest(2L, "3", "1", "1", "1");
        assertTrue(consoleOutput.contains("Enter new group name:"));
    }
    
    @Test
    void shouldReturnAllGroups() throws SQLException, IOException{
        Group group = new Group(1L, "group");
        when(groupDao.findAll(connection)).thenReturn(Arrays.asList(group));
        runTest(2L, "3", "2");
        assertTrue(consoleOutput.contains(consoleOutput));
    }
    
    @Test
    void shouldFindAllGroupsWithLessOrEqualsStudentCount() throws SQLException, IOException{
        Group group = new Group(1L, "group");
        Students student = new Students(1L, 1, "john", "doe");
        studentsDao.save(connection, student);
        groupDao.save(connection, group);
        runTest(2L, "3", "3", "1");
        System.out.println(consoleOutput);
        assertTrue(consoleOutput.contains("Enter count of students:"));
    }

    private Exception runTest(Long timeoutSeconds, String... args) throws IOException {
        for (String arg : args) {
            printWriter.println(arg);
        }
        printWriter.flush();
        Exception exception = runWithTimeout(timeoutSeconds, () -> appMenu.run());
        byteArrayOutputStream.flush();
        this.consoleOutput = new String(byteArrayOutputStream.toByteArray());
        return exception;
    }

    private Exception runWithTimeout(Long seconds, Runnable execution) {
        Exception[] exceptionThrown = new Exception[1];
        try {
            Thread thread = new Thread(() -> {
                try {
                    execution.run();
                } catch (Exception e) {
                    exceptionThrown[0] = e;
                }
            });
            thread.start();
            Instant finish = Instant.now().plus(2, ChronoUnit.SECONDS);
            while (Instant.now().isBefore(finish)) ;
            thread.interrupt();
        } catch (Exception e) {
            // ignore
        }
        return exceptionThrown[0];
    }
}
