package com.board.servlet.yoony;

import com.board.servlet.yoony.article.search.SearchManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Command 패턴에서 모든 요청을 수신해 처리하기 위한 서블릿
 * <p>.do로 끝나는 모든 요청을 받아서 처리함
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 14.
 */
@WebServlet("*.do")
public class MainServlet extends HttpServlet {

  private Logger logger = LogManager.getLogger(MainServlet.class);

  /**
   * 서블릿의 doGet 메소드로 get 요청을 수신해 처리함
   * <p>Command 패턴을 사용하기 때문에 {@link #processRequest(HttpServletRequest, HttpServletResponse)} 메소드를 통해 요청을 처리함
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
   * 서블릿의 doPost 메소드로 post 요청을 수신해 처리함
   * <p>Command 패턴을 사용하기 때문에 {@link #processRequest(HttpServletRequest, HttpServletResponse)} 메소드를 통해 요청을 처리함
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
   * <p>Servlet에서 Command를 직접 다루지 않는 이유는 Command가 변경될 경우 Servlet을 수정하지 않고도 Command만 수정하면 되기 때문임
   * <p>유지보수 및 확장성을 고려한 구조지만, 지금의 작은 규모의 프로젝트에서는 큰 의미는 없음(그냥 연습용)
   * <p>Servlet에서는 request에 대한 처리를 중심으로 진행(인코딩 등)
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  private void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // 요청 응답에 대한 인코딩을 UTF-8로 설정
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    // 처리 중 Error가 발생하면 referer를 통해 기존 요청으로 리다이렉트됨
    // 리다이렉트되면 request.setAttribute("error", "에러코드")를 통해 에러메세지를 설정해도 초기화 됨
    // 대안으로 각 페이지별 분기에서 에러메세지를 개별로 Map에 저장해두고, 에러가 발생하면 해당 Map에서 메세지를 가져와 사용함
    // error 코드는 referer+ "&error="+request.getAttribute("error")를 통해 전달됨
    // 좋은 방법은 아닌거 같지만, 고민이 너무 길어지면서 일단은 이렇게 구현함
    // TODO: 더 좋은 에러 핸들링 방식 고려
    Map<String, String> errorMessages = new HashMap<>();

    // servletPath를 가져와 각 경로별로 다른 처리를 분배함
    String servletPath = request.getServletPath();
    logger.debug("servletPath : " + servletPath);
    // 각 요청에 맞는 jsp 페이지를 지정하기 위한 변수
    String viewPage = null;

    if ("/list.do".equals(servletPath)) {
      viewPage = "/boards/free/List.jsp";
      request.setAttribute("command", "articleList");
    }
    if ("/write.do".equals(servletPath)) {
      viewPage = "/boards/free/Write.jsp";
      request.setAttribute("command", "articleWrite");
      errorMessages.put("1", "입력값 오류!");
      errorMessages.put("2", "게시물 등록 실패!");
      errorMessages.put("3", "파일 등록 실패!");
    }
    if ("/writeAction.do".equals(servletPath)) {
      viewPage = "/list.do";
      request.setAttribute("command", "articleWriteAction");
    }
    if (viewPage == null) {
      viewPage = "/Error.jsp";
      request.setAttribute("errorMessage", "알수없는 엔드 포인트입니다: " + servletPath);
    }
    // 페이지 분기별 지정된 에러 메시지들을 기본적으로 가져감
    request.setAttribute("errorMessages", errorMessages);

    // 모든 페이지에서 현재 유지되는 검색 조건을 가져와 유지하기 위해 사용
    SearchManager searchManager = new SearchManager(request);
    request.setAttribute("searchManager", searchManager);

    // request의 command 파라미터를 통해 요청을 처리할 Command를 가져오지만, 판별을 Helper에 위임
    MainCommand mainCommand = MainCommandHelper.getCommand(request);
    mainCommand.execute(request, response);

    // command 처리 과정중 에러가 발생했다면 이전으로 돌아가기 위한 처리
    if (request.getAttribute("error") != null) {
      String referer = request.getHeader("referer");
      // redirect를 통해 이동하기에 기존 request 정보가 사라지기 때문에 error 코드를 쿼리에 붙여서 날림
      // 코드에 대응되는 message들은 기존이 미리 페이지 분기에서 설정되어 있음
      response.sendRedirect(referer + "&error=" + request.getAttribute("error"));
    } else {
      // 에러가 발생하지 않았다면 지정된 페이지로 이동
      RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
      dispatcher.forward(request, response);
    }
  }
}
