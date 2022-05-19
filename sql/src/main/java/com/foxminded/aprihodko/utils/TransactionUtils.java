package com.foxminded.aprihodko.utils;

import java.sql.Connection;
import java.sql.SQLException;

import com.foxminded.aprihodko.dao.datasource.Datasource;

public class TransactionUtils {
   public static void transaction(Datasource datasource, ConnectionConsumer consumer) throws SQLException{
      try(Connection connection = datasource.getConnection()){
         connection.setAutoCommit(false);
         try{
            consumer.consume(connection);
            connection.commit();
         } catch (Exception e){
            connection.rollback();
            throw new SQLException("Exception in transaction", e);
         }
      }
   }
   
   public interface ConnectionConsumer{
      void consume(Connection connection) throws Exception;
   }
}
