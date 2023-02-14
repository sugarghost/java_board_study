package com.board.servlet.yoony;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 커맨드 패턴을 사용하기 위해 만든 인터페이스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 14.
 */
public interface Command {

  /**
   * 요청들에 대해 실행하기 위한 메소드
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws ServletException
   * @throws IOException
   */
  void execute(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException;
}
