package com.foxminded.aprihodko.misc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.model.Group;
import com.foxminded.aprihodko.model.Students;

public class GenerateStudents {

   private final StudentDao studentDao;

   public GenerateStudents(StudentDao studentDao) {
      this.studentDao = studentDao;
   }

   private final List<String> firstNames = new ArrayList<>() {
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
         add("Petr");
         add("Evgeni");
         add("Anna");
         add("Darya");
         add("Julia");
         add("Maria");
         add("Inna");
         add("Janna");
         add("Konstontin");
         add("Kirill");
      }
   };

   private final List<String> lastNames = new ArrayList<>() {
      {
         add("Prihodko");
         add("Petrov");
         add("Kyleshov");
         add("Kurkul");
         add("Babich");
         add("Ivashko");
         add("Zaremba");
         add("Mahnach");
         add("Kachalov");
         add("Dayksha");
         add("Ivanov");
         add("Sidarov");
         add("Sveridov");
         add("Potemkin");
         add("Stepanenko");
         add("Grishanov");
         add("Kozlov");
         add("Kolos");
         add("Rahcheev");
         add("Malov");
      }
   };

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
      Random random = new Random();
      final int coutOfGroup = 10;
      for (int j = 0; j < coutOfGroup; j++) {
         int countOfStudents = minStudentPerGroup + random.nextInt(maxStudentPerGroup - minStudentPerGroup);
         Long randomGroupId = (long) random.nextInt(coutOfGroup);
         for (int i = 0; i < countOfStudents; i++) {
            if (students.size() < count) {
               students.add(studentDao.save(connection, new Students(randomGroupId, firstNames.get(i), lastNames.get(i))));
            }
         }
      }
      return students;
   }
}
