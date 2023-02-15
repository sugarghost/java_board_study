package com.board.servlet.yoony.article;

import com.board.servlet.yoony.MainCommand;
import com.board.servlet.yoony.article.page.PageDTO;
import com.board.servlet.yoony.category.CategoryDAO;
import com.board.servlet.yoony.category.CategoryDTO;
import com.board.servlet.yoony.util.ValidationChecker;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 게시글 목록을 보여주는 커맨드
 * @author yoony
 * @since 2023. 02. 14.
 * @version 1.0
 * @see MainCommand
 */
public class ArticleListCommand implements MainCommand {


  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // 유효성 체크를 위한 모듈
    ValidationChecker validationChecker = ValidationChecker.getInstance();

    // 검색조건 지정을 위한 category 가져옴
    CategoryDAO categoryDAO = CategoryDAO.getInstance();
    // List에서는 table 출력시 category ID를 name으로 변환이 필요하기에 category Map 형태로 반환
    Map<Integer, String> categoryMap = categoryDAO.getCategoryMap();
    request.setAttribute("categoryMap",categoryMap);

    // 검색을 위한 param 설정
    // TODO: 새롭게 SearchManager를 만들었지만 List부분은 기존 방식을 유지하다가 나중에 변경할 예정
    // SearchManager는 일단은 List 이외의 페이지나 요청에서 검색을 요청하기 위한 용도로 사용될 예정
    Map<String, Object> param = new HashMap<String, Object>();
    String searchWord = request.getParameter("searchWord");
    String category = request.getParameter("category");
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");

    param.put("searchWord", searchWord);
    param.put("category", category);
    param.put("startDate", request.getParameter("startDate"));
    param.put("endDate", request.getParameter("endDate"));

    request.setAttribute("searchWord", searchWord);
    request.setAttribute("category", category);
    request.setAttribute("startDate", startDate);
    request.setAttribute("endDate", endDate);


    // 페이지네이션 구현
    ArticleDAO articleDAO = ArticleDAO.getInstance();
    int totalCount = articleDAO.getArticleCount(param);
    int pageSize = 5;
    int blockSize = 10;

    int pageNum;
    String pageTemp = request.getParameter("pageNum");
    if (!validationChecker.CheckStringIsNullOrEmpty(pageTemp)) {
      pageNum = Integer.parseInt(pageTemp);
    } else {
      pageNum = 1;
    }
    PageDTO pageDTO = new PageDTO(pageNum, pageSize, blockSize, totalCount);
    request.setAttribute("pageDTO", pageDTO);

    int rowStart = (pageNum - 1) * pageSize + 1;
    int rowEnd = rowStart + pageSize - 1;
    param.put("rowStart", rowStart);
    param.put("rowEnd", rowEnd);

    // 검색조건에 따른 게시글 가져옴
    List<ArticleDTO> articleList = articleDAO.getArticleList(param);
    request.setAttribute("articleList",articleList);
  }
}
