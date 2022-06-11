package com.foxminded.aprihodko.menu.actions.impl;

import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.console.Console;
import com.foxminded.aprihodko.model.Students;

public class StudentAddAction extends AbstractAction{
    private static Datasource datasource;
    private static StudentDao studentDao;

    public StudentAddAction(Datasource datasource, StudentDao studentDao) {
        super("Add new student ", console -> {
            String newFirstName = console.askForString("Create firt student name");
            String newLastName = console.askForString("Create last student name");
            String groupdId = console.askForString("Enter group_id for student");
            if(!newFirstName.isEmpty() && !newLastName.isEmpty() && !groupdId.isEmpty()) {
                Students student = new Students(Long.valueOf(groupdId), newFirstName, newLastName);
                try {
                    inTransaction(datasource, connection -> studentDao.save(connection, student));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return findAllStudents(console);
            }
            return "";
        });
        this.datasource = datasource;
        this.studentDao = studentDao;
    }
    
    private static String findAllStudents(Console console) {
        try {
            List<Students> students = fromTransaction(datasource, connection -> studentDao.findAll(connection));
            AtomicInteger count = new AtomicInteger();
            String result = students.stream()
                    .map(student -> String.format("%2d) %s", count.incrementAndGet(), student.getFirstName(), student.getLastName()))
                    .collect(Collectors.joining("\n"));
            String counts = String.valueOf(students.size());
            console.println(counts);
            return "students";
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
