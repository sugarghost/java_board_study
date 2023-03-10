package com.board.servlet.yoony;

import com.board.servlet.yoony.article.search.SearchManager;
import com.board.servlet.yoony.util.RequestUtil;
import java.io.IOException;
import java.util.Enumeration;
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
   * <p>Command 패턴을 사용하기 때문에 {@link #processRequest(HttpServletRequest, HttpServletResponse)} 메소드를
   * 통해 요청을 처리함
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
   * <p>Command 패턴을 사용하기 때문에 {@link #processRequest(HttpServletRequest, HttpServletResponse)} 메소드를
   * 통해 요청을 처리함
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

    // 모든 페이지에서 현재 유지되는 검색 조건을 가져와 유지하기 위해 사용
    SearchManager searchManager = new SearchManager(request);
    request.setAttribute("searchManager", searchManager);

    // TODO: @PathParam을 사용하거나 어노테이션을 따로 만들어보기
    // 메소드에 관련된 어노테이션을 만들고 메소드 실행 전 개입을 해서 데코레이터처럼 작동함(어노테이션 AOP)
    // servletPath를 가져와 각 경로별로 다른 처리를 분배함
    String servletPath = request.getServletPath();
    logger.debug("servletPath : " + servletPath);
    // 각 요청에 맞는 jsp 페이지를 지정하기 위한 변수
    String viewPage = null;
    // action 요청에 대한 처리 후 리다이렉트 여부를 결정하기 위한 변수
    boolean isRedirect = false;
    String redirectParameters = "";
    // download 등의 페이지 이동이 없는 요청에 대한 처리를 위한 변수
    // isRedirect와 합쳐서 int에 저장해서 아래에서 if 분기로 처리해도 되지만 가독성을 위해 분리
    boolean isForward = true;
    // TODO: if else 내용 치워버리고 1/10로 코드를 줄여버리기(힌트: 인터페이스를 쓰는 이유는 if else를 줄이기 위해서)
    // TODO: if문은 else if 처리하기(return 문이 있는게 아니니 if문을 전부 검사함)
    if ("/list.do".equals(servletPath)) {
      viewPage = "/boards/free/list.jsp";
      request.setAttribute("command", "articleList");
    }
    if ("/write.do".equals(servletPath)) {
      viewPage = "/boards/free/write.jsp";
      request.setAttribute("command", "articleWrite");
      // TODO: feedback 에러 핸들러를 따로 둬서 그쪽 클래스에서 핸들하는 방안이 좋음
      errorMessages.put("1", "입력값 오류!");
      errorMessages.put("2", "게시물 등록 실패!");
      errorMessages.put("3", "파일 등록 실패!");
    }
    if ("/writeAction.do".equals(servletPath)) {
      // action 처리의 경우 처리 후 리다이렉트를 위해 isRedirect를 true로 설정
      isRedirect = true;
      viewPage = "/list.do";
      request.setAttribute("command", "articleWriteAction");
    }
    if ("/view.do".equals(servletPath)) {
      viewPage = "/boards/free/view.jsp";
      request.setAttribute("command", "articleView");
      errorMessages.put("1", "해당 게시물이 존재하지 않습니다!");
      errorMessages.put("2", "비밀번호가 일치하지 않습니다!");
      errorMessages.put("comment1", "댓글 내용이 유효하지 않습니다!");
      errorMessages.put("comment2", "댓글 등록에 실패했습니다!");
      errorMessages.put("delete1", "삭제에 실패했습니다!");
    }
    if ("/comment_write_action.do".equals(servletPath)) {
      isRedirect = true;
      redirectParameters = RequestUtil.getUrlParameter(request.getHeader("referer"));
      viewPage = "/view.do";
      request.setAttribute("command", "commentWriteAction");
    }
    if ("/file_download_action.do".equals(servletPath)) {
      isForward = false;
      request.setAttribute("command", "fileDownloadAction");
    }
    if ("/delete_action.do".equals(servletPath)) {
      isRedirect = true;
      redirectParameters = searchManager.getSearchParamsQuery();
      viewPage = "/list.do";
      request.setAttribute("command", "articleDeleteAction");
    }
    if ("/modify.do".equals(servletPath)) {
      viewPage = "/boards/free/modify.jsp";
      request.setAttribute("command", "articleModify");
      errorMessages.put("1", "해당 게시물이 존재하지 않습니다!");
      errorMessages.put("2", "비밀번호가 일치하지 않습니다!");
      errorMessages.put("3", "게시글 수정에 실패했습니다!");
      errorMessages.put("4", "파일 수정에 실패했습니다!");
    }
    if ("/modify_action.do".equals(servletPath)) {
      isRedirect = true;
      redirectParameters = searchManager.getSearchParamsQuery();
      redirectParameters += "&articleId=" + request.getParameter("articleId");
      viewPage = "/view.do";
      request.setAttribute("command", "articleModifyAction");
    }

    if (viewPage == null) {
      viewPage = "/error.jsp";
      request.setAttribute("errorMessage", "알수없는 엔드 포인트입니다: " + servletPath);
    }
    // 페이지 분기별 지정된 에러 메시지들을 기본적으로 가져감
    request.setAttribute("errorMessages", errorMessages);

    // request의 command 파라미터를 통해 요청을 처리할 Command를 가져오지만, 판별을 Helper에 위임
    // TODO: request를 특정 클래스에 던지는 코드는 좋은 코드가 아님
    // request가 매개변수로 던져지면 확장성이 떨어짐
    // pojo형태(내가 만든 자바 DTO또는 MAP이 됬든 던져서 처리하는게 나음)
    MainCommand mainCommand = MainCommandHelper.getCommand(request);
    mainCommand.execute(request, response);

    // command 처리 과정중 에러가 발생했다면 이전으로 돌아가기 위한 처리
    if (request.getAttribute("error") != null) {
      // referer의 용도는 통계 데이터 수집이 큼
      // TODO: referer를 오류 발생시 이전 페이지로 돌아가는건 좋은 구문이 아님
      // 에러 발생시 일반적으로 에러페이지로 돌아감
      String referer = request.getHeader("referer");
      // redirect를 통해 이동하기에 기존 request 정보가 사라지기 때문에 error 코드를 쿼리에 붙여서 날림
      // 코드에 대응되는 message들은 기존이 미리 페이지 분기에서 설정되어 있음
      response.sendRedirect(referer + "&error=" + request.getAttribute("error"));
    } else if (isRedirect) {
      // action 처리인 경우 redirect를 통해 이동
      response.sendRedirect(viewPage+"?"+redirectParameters);
    } else if (isForward) {
      // 에러가 발생하지 않았다면 지정된 페이지로 이동
      RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
      dispatcher.forward(request, response);
    }
    // else에 경우 이동하지 않으며 페이지가 그대로 유지됨
    // 대표적으로 download 요청이 이에 해당함
  }
}
