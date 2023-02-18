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
 * 게시글 삭제 처리를 처리하는 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @since 2023. 02. 18.
 */
public class ArticleDeleteActionCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(ArticleDeleteActionCommand.class);

  /**
   * 게시글 삭제 처리을 처리하는 메소드
   * <p>게시글 삭제를 위해 articleId와 password를 매개변수로 받아옴
   * <p>password를 sha256로 암호화하고 게시글의 비밀번호와 일치하는지 확인
   * <p>일치하면 게시글을 삭제하고 해당 게시글에 속한 파일들을 삭제
   * <p>사용되는 에러 코드는 view.do에서 정의됨
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   * @version 1.0
   * @aothor yoony
   * @see MainCommand
   * @see ArticleDAO#selectArticle(int)
   * @see ArticleDAO#selectPasswordCheck(ArticleDTO)
   * @see ArticleDAO#deleteArticle(ArticleDTO)
   * @see FileDAO#deleteAllFile(int)
   * @since 2023. 02. 18.
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
