package com.foxminded.aprihodko.utils;

import com.foxminded.aprihodko.dao.datasource.Datasource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

public class SqlUtils {
    private SqlUtils() {
    }

    public static void executeSqlScript(Connection connection, String sqlScript) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlScript);
        }
    }

    public static void executeSqlScriptFile(Datasource datasource, String fileName) throws SQLException {
        inTransaction(datasource, (connection) -> {
            SqlUtils.executeSqlScript(connection, ResourceUtils.loadTextFileFromResources(fileName));
        });
    }

    public static void executeSqlScriptFile(Connection connection, String fileName) throws SQLException, IOException {
        SqlUtils.executeSqlScript(connection, ResourceUtils.loadTextFileFromResources(fileName));
    }
}
