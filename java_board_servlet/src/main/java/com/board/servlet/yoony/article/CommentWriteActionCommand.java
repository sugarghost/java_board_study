package com.board.servlet.yoony.article;

import com.board.servlet.yoony.MainCommand;
import com.board.servlet.yoony.comment.CommentDAO;
import com.board.servlet.yoony.comment.CommentDTO;
import com.board.servlet.yoony.database.MyBatisConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 댓글 작성 커맨드
 *
 * @version 1.0
 * @arthor yoony
 * @see MainCommand
 * @since 2023. 02. 17.
 */
public class CommentWriteActionCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(CommentWriteActionCommand.class);

  /**
   * 댓글 작성
   * <p> 게시물 존재 여부 확인 후 댓글 작성
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   * @author yoony
   * @version 1.0
   * @see MainCommand
   * @see ArticleDAO#selectArticle(int)
   * @see CommentDAO#insertComment(CommentDTO)
   * @since 2023. 02. 17.
   */
  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response) {
    logger.debug("execute()");
    // MyBatis instance 가져옴
    MyBatisConfig myBatisConfig = MyBatisConfig.getInstance();
    // auto close를 위한 try-with-resource
    try (
        SqlSession sqlSession = myBatisConfig.getSqlSessionFactory().openSession(true);
    ) {
      // MyBatis Mapper 가져옴
      ArticleDAO articleDAO = sqlSession.getMapper(ArticleDAO.class);
      int articleId = request.getParameter("articleId") == null ? 0
          : Integer.parseInt(request.getParameter("articleId"));
      String content = request.getParameter("content");
      // 게시물 존재 여부 확인
      // TODO: 게시물 존재 여부만 확인하는 쿼리 따로 짜기
      ArticleDTO articleDTO = articleDAO.selectArticle(articleId);
      if (articleDTO == null) {
        logger.error("해당 게시물이 존재하지 않습니다.");
        request.setAttribute("error", "1");
        return;
      }

      // 게시물이 존재하면 댓글 작성
      CommentDAO commentDAO = sqlSession.getMapper(CommentDAO.class);
      CommentDTO commentDTO = new CommentDTO();
      commentDTO.setArticleId(articleId);
      commentDTO.setContent(content);
      int commentInsertResult = commentDAO.insertComment(commentDTO);

      // 댓글 작성 성공 여부 확인
      if (commentInsertResult > 0) {
        logger.debug("댓글 작성에 성공했습니다.");
      } else {
        logger.error("댓글 작성에 실패했습니다.");
        request.setAttribute("error", "comment1");
        return;
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
