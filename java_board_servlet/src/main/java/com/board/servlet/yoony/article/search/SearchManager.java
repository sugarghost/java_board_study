package com.board.servlet.yoony.article.search;

import com.board.servlet.yoony.util.ValidationChecker;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 검색 조건을 관리하기 위한 클래스 기존에 JSP include를 통해 검색 조건을 관리하던 방식에서 변경함
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 15.
 */
@Getter
@Setter
@ToString
public class SearchManager {

  private String pageNum;
  private String searchWord;
  private String categoryId;
  private String startDate;
  private String endDate;

  /**
   * 검색 조건을 관리하기 위한 생성자
   * <p>request에서 검색 조건을 가져와 필드에 저장함
   *
   * @param request HttpServletRequest
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 15.
   */
  public SearchManager(HttpServletRequest request) {
    this.pageNum = (request.getParameter("pageNum") != null) ? request.getParameter("pageNum") : "";
    this.searchWord =
        (request.getParameter("searchWord") != null) ? request.getParameter("searchWord") : "";
    this.categoryId =
        (request.getParameter("categoryId") != null) ? request.getParameter("categoryId") : "";
    this.startDate =
        (request.getParameter("startDate") != null) ? request.getParameter("startDate") : "";
    this.endDate = (request.getParameter("endDate") != null) ? request.getParameter("endDate") : "";
  }

  /**
   * 저장된 검색 조건을 Url에 붙여서 사용하기 위한 String을 생성함
   *
   * @return String 저장된 검색 조건을 붙여서 만든 query String
   * @throws UnsupportedEncodingException
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 15.
   */
  public String getSearchParamsQuery() throws UnsupportedEncodingException {
    String searchParams = "";
    if (!ValidationChecker.CheckStringIsNullOrEmpty(pageNum)) {
      searchParams += "&pageNum=" + pageNum;
    }
    if (!ValidationChecker.CheckStringIsNullOrEmpty(searchWord)) {
      searchParams += "&searchWord=" + URLEncoder.encode(searchWord, "UTF-8");
    }
    if (!ValidationChecker.CheckStringIsNullOrEmpty(categoryId)) {
      searchParams += "&categoryId=" + categoryId;
    }
    if (!ValidationChecker.CheckStringIsNullOrEmpty(startDate)) {
      searchParams += "&startDate=" + startDate;
    }
    if (!ValidationChecker.CheckStringIsNullOrEmpty(endDate)) {
      searchParams += "&endDate=" + endDate;
    }
    return searchParams;
  }

  /**
   * 저장된 검색 조건을 Url에 붙여서 사용하기 위한 String을 생성함
   * <p>pageNum을 제외한 나머지 검색 조건을 붙여서 만듬
   * <p>Paging.jsp에서는 이미 PageNum을 조건으로 붙이기 때문에 중복이 발생해서 따로 만듬
   * <p>기존 방식으로도 동작은 하지만 보기 안좋아서 따로 만듬
   * <p>좀더 좋은 설계가 있겠지만 이 문제로 오래 고민하는 대신 다른 기능 작업하려는 판단
   *
   * @return String 저장된 검색 조건을 붙여서 만든 query String
   * @throws UnsupportedEncodingException
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 15.
   */
  public String getSearchParamsQueryWithOutPageNum() throws UnsupportedEncodingException {
    String searchParams = "";
    if (!ValidationChecker.CheckStringIsNullOrEmpty(searchWord)) {
      searchParams += "&searchWord=" + URLEncoder.encode(searchWord, "UTF-8");
    }
    if (!ValidationChecker.CheckStringIsNullOrEmpty(categoryId)) {
      searchParams += "&categoryId=" + categoryId;
    }
    if (!ValidationChecker.CheckStringIsNullOrEmpty(startDate)) {
      searchParams += "&startDate=" + startDate;
    }
    if (!ValidationChecker.CheckStringIsNullOrEmpty(endDate)) {
      searchParams += "&endDate=" + endDate;
    }
    return searchParams;
  }
}
