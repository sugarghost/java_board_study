package com.board.servlet.yoony;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command 패턴에서 모든 요청을 수신해 처리하기 위한 서블릿
 * .do로 끝나는 모든 요청을 받아서 처리함
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 14.
 */
@WebServlet("*.do")
public class MainServlet extends HttpServlet {

  /**
   * 서블릿의 doGet 메소드로 get 요청을 수신해 처리함 Command 패턴을 사용하기 떄문에 processRequest 메소드를 통해 요청을 처리함
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   * @see #processRequest(HttpServletRequest, HttpServletResponse) 
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * 서블릿의 doPost 메소드로 post 요청을 수신해 처리함 Command 패턴을 사용하기 떄문에 processRequest 메소드를 통해 요청을 처리함
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   * @see #processRequest(HttpServletRequest, HttpServletResponse) 
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * 요청을 처리하는 메소드 CommandHelper를 통해 요청을 처리할 Command를 가져와서 실행함
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  private void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // request의 command 파라미터를 통해 요청을 처리할 Command를 가져오지만, 판별을 Helper에 위임함
    // Servlet은 사실상 요청 수신을 위한 주소역할만 함(고민 필요)
    Command command = CommandHelper.getCommand(request);
    command.execute(request, response);
  }


}
