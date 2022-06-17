package com.foxminded.aprihodko.utils;

import com.foxminded.aprihodko.dao.datasource.Datasource;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionUtils {
    public static void inTransaction(Datasource datasource, ConnectionConsumer consumer) throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                consumer.consume(connection);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw new SQLException("Exception in transaction", e);
            }
        }
    }

    public static <T> T fromTransaction(Datasource datasource, ConnectionConsumerWithResult<T> consumer)
            throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                T result = consumer.consume(connection);
                connection.commit();
                return result;
            } catch (Exception e) {
                connection.rollback();
                throw new SQLException("Exception in transaction", e);
            }
        }
    }

    public interface ConnectionConsumer {
        void consume(Connection connection) throws Exception;
    }

    public interface ConnectionConsumerWithResult<T> {
        T consume(Connection connection) throws Exception;
    }
}
