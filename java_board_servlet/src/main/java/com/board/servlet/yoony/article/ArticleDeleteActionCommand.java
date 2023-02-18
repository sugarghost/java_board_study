package com.board.servlet.yoony.article;

import com.board.servlet.yoony.MainCommand;
import com.board.servlet.yoony.database.MyBatisConfig;
import com.board.servlet.yoony.file.FileDAO;
import com.board.servlet.yoony.file.FileDTO;
import com.board.servlet.yoony.util.RequestUtil;
import com.board.servlet.yoony.util.Security;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Article write action command.
 */
public class ArticleDeleteActionCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(ArticleDeleteActionCommand.class);

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response) {
    logger.debug("execute()");
    // MyBatis instance 가져옴
    MyBatisConfig myBatisConfig = MyBatisConfig.getInstance();

    // auto close를 위한 try-with-resource
    try (
        SqlSession sqlSession = myBatisConfig.getSqlSessionFactory().openSession();
    ) {

      ArticleDAO articleDAO = sqlSession.getMapper(ArticleDAO.class);
      int articleId = RequestUtil.getIntParameter(request, "articleId");
      String password = Security.sha256Encrypt(request.getParameter("password"));

      ArticleDTO targetArticleDTO = articleDAO.selectArticle(articleId);
      if (targetArticleDTO == null) {
        logger.error("해당 게시물이 존재하지 않습니다.");
        request.setAttribute("error", "1");
        return;
      }

      ArticleDTO deleteArticleDTO = new ArticleDTO();
      deleteArticleDTO.setArticleId(articleId);
      deleteArticleDTO.setPassword(password);

      boolean isPasswordValid = articleDAO.selectPasswordCheck(deleteArticleDTO);
      if (!isPasswordValid) {
        logger.error("패스워드가 일치하지 않습니다!.");
        request.setAttribute("error", "2");
        return;
      }
      int result = articleDAO.deleteArticle(deleteArticleDTO);

      if (result < 1) {
        logger.error("게시글 삭제에 실패했습니다.");
        request.setAttribute("error", "delete1");
        return;
      }
      FileDAO fileDAO = sqlSession.getMapper(FileDAO.class);
      // 게시글이 지워지면 파일도 전부 삭제(실제 파일 삭제는 보류)
      fileDAO.deleteAllFile(articleId);
      sqlSession.commit();
    } catch (Exception e) {
      logger.error(e);
      e.printStackTrace();
    }
  }
}
