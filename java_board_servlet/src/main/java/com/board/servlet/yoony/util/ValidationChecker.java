package com.board.servlet.yoony.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 유효성 검사를 위한 클래스
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 14.
 */
public class ValidationChecker {

  /**
   * String이 null이거나 빈 문자열인지 확인
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 14.
   * @param targetString 확인할 String
   * @return boolean null이거나 빈 문자열이면 true, 아니면 false
   */
  public static boolean CheckStringIsNullOrEmpty(String targetString){
    return targetString == null || "".equals(targetString) || targetString.isEmpty();
  }

  /**
   * Object가 null인지 확인
   * CheckStringIsNullOrEmpty에 Object를 검사하려면 캐스팅 하는 과정이 필요했기 때문에 따로 만듬
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 14.
   * @param targetObject 확인할 Object
   * @return boolean null이면 true, 아니면 false
   */
  public static boolean CheckStringIsNullOrEmpty(Object targetObject){
    return targetObject == null || "".equals(targetObject);
  }
}
