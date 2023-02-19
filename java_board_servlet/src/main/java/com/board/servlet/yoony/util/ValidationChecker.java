package com.board.servlet.yoony.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 유효성 검사를 위한 클래스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 14.
 */
public class ValidationChecker {

  /**
   * String이 null이거나 빈 문자열인지 확인
   *
   * @param targetString 확인할 String
   * @return boolean null이거나 빈 문자열이면 true, 아니면 false
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 14.
   */
  public static boolean CheckStringIsNullOrEmpty(String targetString) {
    // TODO: 테스트 같은 경우는 이름이 길어도 되지만, 가능한 명확한게 좋음
    // TODO: 메소드 소문자로 들어가야함(이름 또한 너무 기니 isEmpty로 바꿔야함)
    // TODO: "null"이라는 스트링 값 자체가넘어가는 게 이상함(애초에 문자로 된 null이 가면 안됨) 만약 간다면 의미가 있는 즉 의도된 상태일 수 있으니 의도와 다른 결과를 낼 수 있음)
    // TODO: 명명규칙상 boolean은 is로 시작하는 방안을 고려
    return targetString == null || "".equals(targetString) || targetString.isEmpty()
        || "null".equals(targetString);
  }

  /**
   * Object가 null인지 확인
   * <p>CheckStringIsNullOrEmpty에 Object를 검사하려면 캐스팅 하는 과정이 필요했기 때문에 따로 만듬
   *
   * @param targetObject 확인할 Object
   * @return boolean null이면 true, 아니면 false
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 14.
   */
  public static boolean CheckStringIsNullOrEmpty(Object targetObject) {
    return targetObject == null || "".equals(targetObject);
  }

  /**
   * String의 길이가 지정한 StringLength보다 길면 StringLength만큼 자르고 "..."을 붙여서 반환
   *
   * @param targetString 자를 String
   * @param StringLength 자를 길이
   * @return substring된 targetString
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 19.
   */
  public static String SubStringWithSkipMark(String targetString, int StringLength) {
    if (targetString.length() > StringLength) {
      targetString = targetString.substring(0, StringLength) + "...";
    }
    return targetString;
  }
}
