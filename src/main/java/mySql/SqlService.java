package mySql;

import java.sql.Connection;

public interface SqlService {
   
   public Connection getConnection(String host, String user, String passwd,
         String properties);
   public void closeConnection();

}
