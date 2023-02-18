package com.board.servlet.yoony.article;

import com.board.servlet.yoony.MainCommand;
import com.board.servlet.yoony.article.page.PageDTO;
import com.board.servlet.yoony.category.CategoryDAO;
import com.board.servlet.yoony.category.CategoryDTO;
import com.board.servlet.yoony.database.MyBatisConfig;
import com.board.servlet.yoony.util.ValidationChecker;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 게시글 목록을 보여주는 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @since 2023. 02. 14.
 */
public class ArticleListCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(ArticleListCommand.class);

  /**
   * 게시글 목록을 가져옴 categoryDAO.selectCategoryList()를 통해 카테고리 목록을 가져온 후 articleDAO.selectArticleList()를
   * 통해 게시글 목록을 가져온다.
   * <p>게시글 목록을 가져올 때는 검색을 위한 param을 넘겨준다.
   * <p>검색 조건은 searchWord, category, startDate, endDate, pageNum(현재 페이지) 이다.
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   * @author yoony
   * @version 1.0
   * @see MainCommand
   * @see ArticleDAO#selectArticleCount(Map)
   * @see ArticleDAO#selectArticleList(Map)
   * @see CategoryDAO#selectCategoryList()
   * @since 2023. 02. 16.
   */
  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response) {
    logger.debug("execute()");
    // MyBatis instance 가져옴
    MyBatisConfig myBatisConfig = MyBatisConfig.getInstance();
    // auto close를 위한 try-with-resource
    // session을 DAO별로 따로 생성했는데, 굳이 이럴 필요는 없을 것 같음
    try (
        SqlSession articleSqlSession = myBatisConfig.getSqlSessionFactory().openSession();
        SqlSession categorySqlSession = myBatisConfig.getSqlSessionFactory().openSession();
    ) {
      // MyBatis Mapper 가져옴
      ArticleDAO articleDAO = articleSqlSession.getMapper(ArticleDAO.class);
      CategoryDAO categoryDAO = categorySqlSession.getMapper(CategoryDAO.class);

      // categoryList를 가져와 Map 형태로 변환
      // Mybatis에서 자체적으로 해결하면 좋지만 고민 시간이 너무 길어져서 일단 비효율적이라도 진행
      // TODO: List => Map 변환을 거치지 않고 Mybatis 자체적으로 Map 리턴 고려
      List<CategoryDTO> categoryList = categoryDAO.selectCategoryList();
      Map<Integer, String> categoryMap = new HashMap<>();
      for (CategoryDTO category : categoryList) {
        categoryMap.put(category.getCategoryId(), category.getName());
      }
      logger.debug("categoryMap size: " + categoryMap.size());
      request.setAttribute("categoryMap", categoryMap);

      // 검색을 위한 param 설정
      // 새롭게 SearchManager를 만들었지만 List부분은 기존 방식을 유지
      // SearchManager는 일단은 List 이외의 페이지나 요청에서 검색을 요청하기 위한 용도로 사용될 예정
      Map<String, Object> param = new HashMap<String, Object>();
      String searchWord = request.getParameter("searchWord");
      String categoryId = request.getParameter("categoryId");
      String startDate = request.getParameter("startDate");
      String endDate = request.getParameter("endDate");

      param.put("searchWord", searchWord);
      param.put("categoryId", categoryId);
      param.put("startDate", request.getParameter("startDate"));
      param.put("endDate", request.getParameter("endDate"));

      request.setAttribute("searchWord", searchWord);
      request.setAttribute("categoryId", categoryId);
      request.setAttribute("startDate", startDate);
      request.setAttribute("endDate", endDate);

      // 페이지네이션 구현
      int totalCount = articleDAO.selectArticleCount(param);
      logger.debug("selectArticleCount: " + totalCount);
      int pageSize = 5;
      int blockSize = 10;

      int pageNum;
      String pageTemp = request.getParameter("pageNum");
      if (!ValidationChecker.CheckStringIsNullOrEmpty(pageTemp)) {
        pageNum = Integer.parseInt(pageTemp);
      } else {
        pageNum = 1;
      }
      PageDTO pageDTO = new PageDTO(pageNum, pageSize, blockSize, totalCount);
      request.setAttribute("pageDTO", pageDTO);
      param.put("rowStart", pageDTO.getStartRowNum());
      param.put("pageSize", pageSize);

      // 검색조건에 따른 게시글 가져옴
      List<ArticleDTO> articleList = articleDAO.selectArticleList(param);
      logger.debug("articleList size: " + articleList.size());
      request.setAttribute("articleList", articleList);
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
