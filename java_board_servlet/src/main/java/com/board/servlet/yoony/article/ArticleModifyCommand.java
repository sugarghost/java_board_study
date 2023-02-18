package com.board.servlet.yoony.article;

import com.board.servlet.yoony.MainCommand;
import com.board.servlet.yoony.category.CategoryDAO;
import com.board.servlet.yoony.category.CategoryDTO;
import com.board.servlet.yoony.database.MyBatisConfig;
import com.board.servlet.yoony.file.FileDAO;
import com.board.servlet.yoony.file.FileDTO;
import com.board.servlet.yoony.util.ValidationChecker;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 게시글 수정화면 이동 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @since 2023. 02. 18.
 */
public class ArticleModifyCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(ArticleModifyCommand.class);

  /**
   * 게시글 수정화면 이동 메소드
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   */
  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response) {
    logger.debug("execute()");
    // MyBatis instance 가져옴
    MyBatisConfig myBatisConfig = MyBatisConfig.getInstance();
    // auto close를 위한 try-with-resource
    try (
        SqlSession sqlSession = myBatisConfig.getSqlSessionFactory().openSession();
    ) {
      // MyBatis Mapper 가져옴
      ArticleDAO articleDAO = sqlSession.getMapper(ArticleDAO.class);
      int articleId = ValidationChecker.CheckStringIsNullOrEmpty(request.getParameter("articleId")) ? 0
          : Integer.parseInt(request.getParameter("articleId"));

      ArticleDTO articleDTO = articleDAO.selectArticle(articleId);
      if (articleDTO == null) {
        logger.error("게시글이 존재하지 않습니다.");
        return;
      }
      request.setAttribute("articleDTO", articleDTO);
      if (articleDTO.getIsFileExist()) {
        FileDAO fileDAO = sqlSession.getMapper(FileDAO.class);
        List<FileDTO> fileList = fileDAO.selectFileList(articleId);
        request.setAttribute("fileList", fileList);
      }

    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
