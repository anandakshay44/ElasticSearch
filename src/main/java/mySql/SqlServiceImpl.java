package mySql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import common.Constant;
import common.Utils;

public class SqlServiceImpl implements SqlService {
   private Connection conn = null; // Cached Database Connection
   private boolean connectionInitialized = false;


   /**
    * This method will return a MySQL database connection for use, either a new
    * connection or a cached connection
    * 
    * @param host
    *           String Hostname
    * @param user
    *           String Database username
    * @param passwd
    *           String database password
    * @return A MySQL Database connection for use
    */
   public Connection getConnection(String host, String user, String passwd,
         String properties) {
      if (conn == null) {
         conn = getNewConnection(host, user, passwd, properties);
      }
      // Test Connection, and reconnect if necessary
      else if (!isConnectionValid()) {
         closeConnection();
         conn = getNewConnection(host, user, passwd, properties);
      }
      return conn;
   }

   /**
    * This method will return a new MySQL database connection
    * 
    * @param host
    *           String Hostname for MySQL Connection
    * @param user
    *           String Database username for MySQL Connection
    * @param passwd
    *           String database password for MySQL Connection
    * @return connection new MySQL Connection
    */
   private Connection getNewConnection(String host, String user, String passwd,
         String properties) {
      Connection newConn = null;
      String dbURL = Utils.buildString(Constant.JDBC_URL, host, Constant.SLASH,
            properties);
      String connectionInfo = Utils.buildString(dbURL, Constant.SPACE, user,
            Constant.PASSWORD_FILTERED);
      try {
         if (!connectionInitialized) {
            // load jdbc driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connectionInitialized = true;
         }
         newConn = DriverManager.getConnection(dbURL, user, passwd);
         if (newConn == null) {
            System.out.println("newConn is null");
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return newConn;
   }

   /**
    * Close current connection
    */
   public void closeConnection() {
      if (conn != null) {
         try {
            conn.close();
            conn = null;
         } catch (SQLException e) {

         }
      }
   }

   /**
    * Check if connection is valid by pinging MySQL server. If connection is
    * null or invalid return false, otherwise true.
    * 
    * @return the state of the connection
    */
   private boolean isConnectionValid() {
      boolean available = false;
      if (conn != null) {
         Statement stmt = null;
         ResultSet rs = null;
         try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(Constant.PING);
            available = true;
         } catch (SQLException e) {
            available = false;
         } finally {
            try {
               if (stmt != null) {
                  stmt.close();
               }
               if (rs != null) {
                  rs.close();
               }
            } catch (SQLException e) {

            }
            rs = null;
            stmt = null;
         }
      }
      return available;
   }

}
