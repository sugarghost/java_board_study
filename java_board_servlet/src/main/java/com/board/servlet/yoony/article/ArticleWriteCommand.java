package com.board.servlet.yoony.article;

import com.board.servlet.yoony.MainCommand;
import com.board.servlet.yoony.category.CategoryDAO;
import com.board.servlet.yoony.category.CategoryDTO;
import com.board.servlet.yoony.database.MyBatisConfig;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 게시글 작성 화면 이동 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @since 2023. 02. 18.
 */
public class ArticleWriteCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(ArticleWriteCommand.class);

  /**
   * 게시글 작성 화면 이동 메소드
   * <p>카테고리 목록을 가져와 request에 저장함
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   * @version 1.0
   * @author yoony
   * @see MainCommand
   * @see CategoryDAO#selectCategoryList()
   * @since 2023. 02. 18.
   */
  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response) {
    logger.debug("execute()");
    // MyBatis instance 가져옴
    MyBatisConfig myBatisConfig = MyBatisConfig.getInstance();
    // auto close를 위한 try-with-resource
    try (
        SqlSession categorySqlSession = myBatisConfig.getSqlSessionFactory().openSession();
    ) {
      // MyBatis Mapper 가져옴
      CategoryDAO categoryDAO = categorySqlSession.getMapper(CategoryDAO.class);
      // 카테고리 목록을 가져옴
      List<CategoryDTO> categoryList = categoryDAO.selectCategoryList();
      request.setAttribute("categoryList", categoryList);
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
