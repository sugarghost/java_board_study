package com.board.servlet.yoony.util;

/**
 * 요청을 처리하는 유틸리티 클래스
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 18.
 */
public class RequestUtil {

  /**
   * url에서 파라미터를 가져오는 메소드
   * <p>url의 ? 이후의 파라미터를 가져옴
   * @param url 파라미터 가져올 대상 url
   * @return String 해당 url에 존재하는 파라미터
   */
  public static String getUrlParameter(String url) {
    String[] urlSplit = url.split("\\?");
    if (urlSplit.length == 1) {
      return "";
    }
    return urlSplit[1];
  }
}
