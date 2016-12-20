package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mySql.SqlServiceImpl;


public class MySqlDriver {

   public static void main(String[] args) throws SQLException {

      Connection conn = new SqlServiceImpl().getConnection("host/dbname?",
            "user", "pwd", "");
      String query = "SELECT * FROM products";
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(query);
      int a = rs.getFetchSize();
      System.out.println(a);


   }

}
