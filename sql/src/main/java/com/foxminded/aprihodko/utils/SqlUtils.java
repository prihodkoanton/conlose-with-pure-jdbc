package com.foxminded.aprihodko.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.foxminded.aprihodko.dao.datasource.Datasource;

import static com.foxminded.aprihodko.utils.TransactionUtils.transaction;

public class SqlUtils {
   public static void executeSqlScript(Connection connection, String sqlScript) throws SQLException {
      try (Statement statement = connection.createStatement()) {
          statement.executeUpdate(sqlScript);
      }
  }

  public static void executeSqlScriptFile(Datasource datasource, String fileName) throws SQLException {
      transaction(datasource, (connection) ->
              SqlUtils.executeSqlScript(connection, ResourceUtils.loadTextFileFromResources(fileName)));
  }
}
