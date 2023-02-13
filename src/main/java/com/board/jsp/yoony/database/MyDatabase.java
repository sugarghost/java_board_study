package com.board.jsp.yoony.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyDatabase {

  private static final String DRIVER_NAME = "org.mariadb.jdbc.Driver";
  private static final String URL = "jdbc:mariadb://127.0.0.1:33020/yoony_board";
  private static final String USER = "root";
  private static final String PASSWORD = "system1";

  private static MyDatabase instance;

  private MyDatabase() {
    try {
      Class.forName(DRIVER_NAME);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static MyDatabase getInstance() {
    if(instance==null) {
      instance = new MyDatabase();
    }
    return instance;
  }

  public Connection getConnection() throws Exception {
    Connection con = null;

    // TODO: Class Loader가 계속 호출되면서 낭비가됨
    // 싱글톤으로 한다고 하면 인스턴스를 한번 생성할떄만 올려주는 방식으로 가야함.
    // 한번쯤 공부를 해야함
    con = DriverManager.getConnection(URL, USER, PASSWORD);
    return con;
  }


  public void closeConnection(Connection con, PreparedStatement pstmt, ResultSet rs) {
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
