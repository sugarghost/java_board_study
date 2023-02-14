package com.board.servlet.yoony;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 커맨드 액션에 알 수 없는 종류의 액션이 들어오면 실행되는 클래스
 * @author yoony
 * @since 2023. 02. 14.
 * @version 1.0
 * @see Command
 * @see CommandHelper
 */
public class UnknownCommand implements Command {

  /**
   * request에 errorMessage를 담아서 error.jsp로 forward
   * errorMessage에는 알 수 없는 액션에 대한 정보가 담겨있다.
   * @param request
   *        HttpServletRequest
   * @param response
   *        HttpServletResponse
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Handle the case where the action parameter is not recognized
    request.setAttribute("errorMessage", "알수없는 요청입니다: " + request.getParameter("action"));
    request.getRequestDispatcher("/error.jsp").forward(request, response);
  }

}
