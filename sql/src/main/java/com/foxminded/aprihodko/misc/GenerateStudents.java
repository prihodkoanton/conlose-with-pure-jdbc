package com.foxminded.aprihodko.misc;

import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.model.Students;
import com.github.javafaker.Address;
import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateStudents {

    private final StudentDao studentDao;
    private final Address fake;

    public GenerateStudents(StudentDao studentDao) {
        this.studentDao = studentDao;
        fake = new Faker().address();
    }

    public void generateData(Connection connection, int countOfStudents) throws SQLException {
        if (countOfStudents > 200) {
            throw new IllegalArgumentException("Count of Students must be less then 201");
        }
        generateStudents(connection, countOfStudents);
    }

    private List<Students> generateStudents(Connection connection, int count) throws SQLException {
        List<Students> students = new ArrayList<>();
        final int minStudentPerGroup = 10;
        final int maxStudentPerGroup = 30;
        final int groupCount = 10;
        int countOfStudents;
        Random random = new Random();
        int randomGroupId;
        for (int j = 0; j < count; j++) {
            countOfStudents = minStudentPerGroup + random.nextInt(maxStudentPerGroup - minStudentPerGroup);
            randomGroupId = random.nextInt(groupCount);
            for (int i = 0; i < countOfStudents; i++) {
                if (students.size() < count) {
                    students.add(studentDao.save(connection,
                            new Students(randomGroupId, fake.firstName(), fake.lastName())));
                } 
            }
        }
        return students;
    }
}
