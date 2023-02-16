package com.board.servlet.yoony.database;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisConfig {

  private static MyBatisConfig instance;
  private SqlSessionFactory sqlSessionFactory;

  private MyBatisConfig() {
    setupSqlSessionFactory();
  }

  public static MyBatisConfig getInstance() {
    if (instance == null) {
      instance = new MyBatisConfig();
    }
    return instance;
  }

  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

  private void setupSqlSessionFactory() {
    try {
    InputStream configuration = Resources.getResourceAsStream("Mybatis.xml");
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
