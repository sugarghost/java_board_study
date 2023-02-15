package com.board.servlet.yoony.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 데이터베이스 연결을 위한 클래스
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 14.
 */
public class MyDatabase {

  private static final String DRIVER_NAME = "org.mariadb.jdbc.Driver";
  private static final String URL = "jdbc:mariadb://127.0.0.1:33020/yoony_board_v2";
  private static final String USER = "root";
  private static final String PASSWORD = "system1";

  private static MyDatabase instance;

  /**
   * 생성자, 싱글톤 개념으로 만들어져 있기 때문에 외부에서 생성자를 호출할 수 없으며 대신 getInstance 메소드를 통해 객체를 가져옴
   * 이전에는 Connection 객체를 생성할 떄마다 Class.forName을 호출했기 때문에 성능 개선을 위해 생성자에서 한 번만 호출하도록 함
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 14.
   * @throws ClassNotFoundException
   * @see #getInstance()
   *
   */
  private MyDatabase() {
    try {
      Class.forName(DRIVER_NAME);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * MyDatabase 객체를 가져오는 메소드
   * 싱글톤 방식으로 이미 instance가 존재하면 그대로 반환하고, 존재하지 않으면 생성 후 반환
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 14.
   * @see MyDatabase
   * @return MyDatabase
   */
  public static MyDatabase getInstance() {
    if(instance==null) {
      instance = new MyDatabase();
    }
    return instance;
  }

  /**
   * DAO에서 데이터베이스 연결을 위한 Connection을 가져오기 위한 메소드
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 14.
   * @return Connection
   * @throws Exception
   */
  public Connection getConnection() throws Exception {
    Connection con = null;
    con = DriverManager.getConnection(URL, USER, PASSWORD);
    return con;
  }


  /**
   * DAO에서 #getConnection()을 통해 가져온 Connection, PreparedStatement, ResultSet을 닫기 위한 메소드
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 14.
   * @param con Connection
   * @param pstmt PreparedStatement
   * @param rs ResultSet
   */
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
