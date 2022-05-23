package com.foxminded.aprihodko.dao.impl;

import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.dao.datasource.SimpleDatasorce;
import com.foxminded.aprihodko.utils.SqlUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;
import java.util.Properties;

import static com.foxminded.aprihodko.utils.SqlUtils.executeSqlScriptFile;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

public class DaoTestBaseClass {

    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:13");

    protected static Datasource datasource;

    @BeforeAll
    static void beforeAll() throws SQLException {
        if (!postgreSQLContainer.isRunning()) {
            postgreSQLContainer.start();
        }
        Properties properties = new Properties();
        properties.put("datasource.driver-class", postgreSQLContainer.getDriverClassName());
        properties.put("datasource.jdbc-url", postgreSQLContainer.getJdbcUrl());
        properties.put("datasource.username", postgreSQLContainer.getUsername());
        properties.put("datasource.password", postgreSQLContainer.getPassword());
        datasource = new SimpleDatasorce(properties);
        executeSqlScriptFile(datasource, "sql/create_schema.sql");
    }

    @BeforeEach
    void cleanup() throws SQLException {
        executeSqlScriptFile(datasource, "sql/cleanup.sql");
    }

    protected static void sql(String... sqlFileNames) throws SQLException {
        inTransaction(datasource, connection -> {
            for (String sqlFile : sqlFileNames) {
                executeSqlScriptFile(connection, sqlFile);
            }
        });
    }

}
