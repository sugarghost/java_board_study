package com.board.servlet.yoony.util;

import com.oreilly.servlet.MultipartRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 요청을 처리하는 유틸리티 클래스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 18.
 */
public class RequestUtil {

  /**
   * url에서 파라미터를 가져오는 메소드
   * <p>url의 ? 이후의 파라미터를 가져옴
   *
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


  /**
   * request에서 int형 파라미터를 가져올때 null인 경우 error가 발생해 util로 따로 빼서 만듬
   * <p>{@link ValidationChecker#CheckStringIsNullOrEmpty(String)}  }검사 후 이상이 없으면 int형으로 변환해서 반환
   * <p>null인 파라미터에 대해서는 0을 반환함
   *
   * @param request 파라미터를 가져올 request
   * @param key     가져올 파라미터 이름
   * @return int 파라미터 값 혹은 0
   * @see ValidationChecker#CheckStringIsNullOrEmpty(String)
   * ValidationChecker#CheckStringIsNullOrEmpty(String)
   * ValidationChecker#CheckStringIsNullOrEmpty(String)
   */
  public static int getIntParameter(HttpServletRequest request, String key) {
    return ValidationChecker.CheckStringIsNullOrEmpty(request.getParameter(key)) ? 0
        : Integer.parseInt(request.getParameter(key));
  }

  /**
   * {@link #getIntParameter(MultipartRequest, String)}의 MultipartRequest 버전
   * <p>일부 파일을 처리하는 경우 request를 MultipartRequest로 변환해야 하는데 이때 사용
   *
   * @param request 파라미터를 가져올 request
   * @param key     가져올 파라미터 이름
   * @return int 파라미터 값 혹은 0
   * @see ValidationChecker#CheckStringIsNullOrEmpty(String)
   * ValidationChecker#CheckStringIsNullOrEmpty(String)
   * ValidationChecker#CheckStringIsNullOrEmpty(String)
   * @see #getIntParameter(HttpServletRequest, String) #getIntParameter(HttpServletRequest,
   * String)#getIntParameter(HttpServletRequest, String)
   */
  public static int getIntParameter(MultipartRequest request, String key) {
    return ValidationChecker.CheckStringIsNullOrEmpty(request.getParameter(key)) ? 0
        : Integer.parseInt(request.getParameter(key));
  }
}
