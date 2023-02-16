package com.board.servlet.yoony.database;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * MyBatis 설정 클래스
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 16.
 */
public class MyBatisConfig {


  private Logger logger = LogManager.getLogger(MyBatisConfig.class);

  private static MyBatisConfig instance;
  private SqlSessionFactory sqlSessionFactory;

  /**
   * private 생성자
   * getInstance()를 통해서만 객체 생성 가능
   * sqlSessionFactory를 생성하는 setupSqlSessionFactory()를 호출
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 16.
   * @see #getInstance()
   * @see #setupSqlSessionFactory()
   */
  private MyBatisConfig() {
    setupSqlSessionFactory();
  }

  /**
   * 싱글톤 패턴을 이용한 MyBatisConfig 객체 생성
   * @return MyBatisConfig 객체
   */
  public static MyBatisConfig getInstance() {
    if (instance == null) {
      instance = new MyBatisConfig();
    }
    return instance;
  }

  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }
  /**
   * MyBatis 설정 파일을 읽어 SqlSessionFactory를 생성하는 메소드
   * 싱글톤 방식으로 private 생성자인 MyBatisConfig()에서 1회만 호출
   * 생성된 sqlSessionFactory는 getSqlSessionFactory()를 통해 사용 가능
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 16.
   * @see #MyBatisConfig()
   * @see #getSqlSessionFactory()
   *
   */
  private void setupSqlSessionFactory() {
    try {
    InputStream configuration = Resources.getResourceAsStream("Mybatis.xml");
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    } catch (IOException e) {
      logger.error("MyBatis 설정 파일을 읽는 중 오류가 발생했습니다. :" + e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      logger.error("setupSqlSessionFactory() Exception : " + e.getMessage());
      e.printStackTrace();
    }
  }
}
