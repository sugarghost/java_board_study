package com.board.servlet.yoony;

import com.board.servlet.yoony.article.ArticleDAO;
import com.board.servlet.yoony.article.search.SearchManager;
import java.io.IOException;
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
 * .do로 끝나는 모든 요청을 받아서 처리함
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 14.
 */
@WebServlet("*.do")
public class MainServlet extends HttpServlet {

  private Logger logger = LogManager.getLogger(ArticleDAO.class);

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
   * Servlet에서 Command를 직접 다루지 않는 이유는 Command가 변경될 경우 Servlet을 수정하지 않고도 Command만 수정하면 되기 때문임
   * 유지보수 및 확장성을 고려한 구조지만, 지금의 작은 규모의 프로젝트에서는 큰 의미는 없음(그냥 연습용)
   * Servlet에서는 request에 대한 처리를 중심으로 진행(인코딩 등)
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  private void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // request의 인코딩을 UTF-8로 설정
    request.setCharacterEncoding("UTF-8");

    String uri = request.getRequestURI();
    logger.debug("uri : " + uri);
    String contextPath = request.getContextPath();
    String currentUrl = uri.substring(contextPath.length());
    logger.debug("currentUrl : " + currentUrl);
    String viewPage = null;

    if("/List.do".equals(currentUrl)) {
      viewPage = "/boards/free/List.jsp";
      request.setAttribute("command","articleList");
    }
    if(viewPage == null) {
      viewPage = "/Error.jsp";
      request.setAttribute("errorMessage", "알수없는 엔드 포인트입니다: " + currentUrl);
    }

    // 모든 페이지에서 현재 유지되는 검색 조건을 가져와 유지하기 위해 사용
    SearchManager searchManager = new SearchManager(request);
    request.setAttribute("searchManager", searchManager);

    // request의 command 파라미터를 통해 요청을 처리할 Command를 가져오지만, 판별을 Helper에 위임
    MainCommand mainCommand = MainCommandHelper.getCommand(request);
    mainCommand.execute(request, response);


    RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
    dispatcher.forward(request, response);
  }


}
