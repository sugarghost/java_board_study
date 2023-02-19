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
 *
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
   *
   * @return MyBatisConfig 객체
   */
  public static MyBatisConfig getInstance() {
    if (instance == null) {
      instance = new MyBatisConfig();
    }
    return instance;
  }

  /**
   * Gets sql session factory.
   *
   * @return the sql session factory
   */
// TODO: sqlSessionFactory는 노출시키지 말고 sqlSession을 생성해서 return 하기
  // TODO: class name이 제공되는 이름과 곂치면 Wrapper를 붙여버리기(예: sqlSessionMapper)
  // TODO: Wrapper는 메인 클래스(대상 클래스)와 아주 밀접한 경우 붙이는 네이밍, Manager는 여러개를 관리하는 용도의 네이밍
  // 자원 관리를 Factory에서 하는데 그걸 가져다가 잘못 다룰 수 있기 떄문
  // Mybatis에서 session에 대한 관리를 자동으로 해줌(예전에는 안해줬기 떄문이 finally에서 닫아줬음)
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
      // TODO: xml은 소문자로
      //TODO: FileStore. Resources를 통해서 프로퍼티 설정을 참고하면서 locale을 설정해보기(다국어 설정)
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
