package com.foxminded.aprihodko.dao.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GenerateStudents {

   private List<String> firstNames = new ArrayList<>() {
      {
         add("Anton");
         add("Andrey");
         add("Anatoly");
         add("Artem");
         add("Ruslan");
         add("Aleksandr");
         add("Vladimer");
         add("Aleksei");
         add("Dmitry");
         add("Avram");
      }
   };
   private List<String> lastNames = new ArrayList<>() {
      {
         add("Prihodko");
         add("Gusev");
         add("Kyleshov");
         add("Kurkul");
         add("Babich");
         add("Ivashko");
         add("Zaremba");
         add("Mahnach");
         add("Kachalov");
         add("Dayksha");
      }
   };

   public void generatingStudents() {
      try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/antonprihodko",
            "anton", "1234")) {
         System.out.println("JDBC");
         System.out.println("Connected to PostgreSQL");
         PreparedStatement pStatement = connection
               .prepareStatement("INSERT INTO school.students(student_id, group_id int, first_name, last_name) VALUES (?, ?, ?, ?)");
         
         for (int i =0; i <201; i++) {
            
         }
      } catch (SQLException e) {
         System.out.println("Connection failure");
         e.printStackTrace();
      }
   }

}
