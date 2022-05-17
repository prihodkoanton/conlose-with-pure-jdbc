package com.foxminded.aprihodko.dao.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GenerateGroups {

   public static String randomName() {
      final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      final String numbers = "12345674890";
      final Random rand = new Random();
      final Set<String> identifiers = new HashSet<String>();
      StringBuilder builder = new StringBuilder();
      StringBuilder builder2 = new StringBuilder();
      StringBuilder builder3 = new StringBuilder();
      StringBuilder builder4 = new StringBuilder();
      while (builder.toString().length() == 0) {
         int length = rand.nextInt(2) + 2;
         for (int i = 0; i < 2; i++) {
            builder.append(characters.charAt(rand.nextInt(characters.length())));
         }

         builder2.append("-");
         for (int i = 0; i < 2; i++) {
            builder3.append(numbers.charAt(rand.nextInt(numbers.length())));
         }
         builder4.append(builder).append(builder2).append(builder3);
         if (identifiers.contains(builder4.toString())) {
            builder4 = new StringBuilder();
         }
      }
      return builder4.toString();
   }

   public void createGroups() {
      try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/antonprihodko",
            "anton", "1234")) {
         System.out.println("JDBC");
         System.out.println("Connected to PostgreSQL");
         Statement statement = connection.createStatement();
         PreparedStatement pStatement = connection
               .prepareStatement("INSERT INTO school.groups(group_id, group_name) VALUES (?, ?)");
         for (int i = 0; i < 11; i++) {
            pStatement.setLong(1, i);
            pStatement.setString(2, GenerateGroups.randomName());
            pStatement.executeUpdate();
         }
         
      } catch (SQLException e) {
         System.out.println("Connection failure");
         e.printStackTrace();
      } finally {
        
      }
   }
}
