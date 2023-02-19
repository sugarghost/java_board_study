package com.board.servlet.yoony;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 커맨드 패턴을 사용하기 위해 만든 인터페이스 클래스명 중첩을 피하기 위해 Command 대신 MainCommand를 사용함 이후 주석이나 설명에서는 편의상 Command로
 * 명시될 수 있음
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 14.
 */
public interface MainCommand {

  /**
   * 요청들에 대해 실행하기 위한 메소드
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws ServletException the servlet exception
   * @throws IOException      the io exception
   */
  void execute(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException;
}
