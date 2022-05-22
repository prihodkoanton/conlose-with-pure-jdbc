package com.foxminded.aprihodko.misc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.foxminded.aprihodko.dao.GroupDao;
import com.foxminded.aprihodko.model.Group;

public class GeneratorGroups {
   private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

   private final GroupDao groupDao;

   public GeneratorGroups(GroupDao groupDao) {
      this.groupDao = groupDao;
   }

   public void generateDate(Connection connection, int groupCount) throws SQLException {
       if (groupCount > 10) {
           throw new IllegalArgumentException("Count of group should be 10 or less");
       }
      generateGroups(connection, groupCount);
   }

   private List<Group> generateGroups(Connection connection, int count) throws SQLException {
      List<Group> result = new ArrayList<>();
      for (int i = 0; i < count; i++) {
          String groupName = generateGroupName();
          result.add(groupDao.save(connection, new Group(groupName)));
      }
      return result;
   }

   private String generateGroupName() {
      String sb = String.valueOf(randomChar(alphabet)) + randomChar(alphabet) + "-" +
            ThreadLocalRandom.current().nextInt(10, 99);
    return sb;
   }

   private char randomChar(char[] array) {
      return array[ThreadLocalRandom.current().nextInt(array.length)];
   }
}
