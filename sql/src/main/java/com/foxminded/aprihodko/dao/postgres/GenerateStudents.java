package com.foxminded.aprihodko.dao.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class GenerateStudents {

    private List<String> firstNames = Arrays.asList(
            "Anton",
            "Andrey",
            "Anatoly",
            "Artem",
            "Ruslan",
            "Aleksandr",
            "Vladimer",
            "Aleksei",
            "Dmitry",
            "Avram"
    );

    private List<String> lastNames = Arrays.asList(
            "Prihodko",
            "Gusev",
            "Kyleshov",
            "Kurkul",
            "Babich",
            "Ivashko",
            "Zaremba",
            "Mahnach",
            "Kachalov",
            "Dayksha"
    );

    public void generatingStudents() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/antonprihodko",
                "anton", "1234")) {
            System.out.println("JDBC");
            System.out.println("Connected to PostgreSQL");
            PreparedStatement pStatement = connection
                    .prepareStatement("INSERT INTO school.students(student_id, group_id int, first_name, last_name) VALUES (?, ?, ?, ?)");

            for (int i = 0; i < 201; i++) {

            }
        } catch (SQLException e) {
            System.out.println("Connection failure");
            e.printStackTrace();
        }
    }

}
