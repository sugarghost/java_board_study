package com.board.jsp.yoony.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyDatabase {

  public static final String DRIVER_NAME = "org.mariadb.jdbc.Driver";
  public static final String URL = "jdbc:mariadb://127.0.0.1:33020/yoony_board";
  public static final String USER = "root";
  public static final String PASSWORD = "system1";

  public static Connection getConnection() throws Exception {
    Connection con = null;

    Class.forName(DRIVER_NAME);
    con = DriverManager.getConnection(URL, USER, PASSWORD);
    return con;
  }


  public static void closeConnection(Connection con, PreparedStatement pstmt, ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    if (pstmt != null) {
      try {
        pstmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    if (con != null) {
      try {
        con.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
