package com.board.servlet.yoony.article;

import com.board.servlet.yoony.MainCommand;
import com.board.servlet.yoony.category.CategoryDAO;
import com.board.servlet.yoony.category.CategoryDTO;
import com.board.servlet.yoony.comment.CommentDAO;
import com.board.servlet.yoony.comment.CommentDTO;
import com.board.servlet.yoony.database.MyBatisConfig;
import com.board.servlet.yoony.file.FileDAO;
import com.board.servlet.yoony.file.FileDTO;
import com.board.servlet.yoony.util.RequestUtil;
import com.board.servlet.yoony.util.ValidationChecker;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 게시글 상세보기 커맨드
 *
 * @version 1.0
 * @arthor yoony
 * @see MainCommand
 * @since 2023. 02. 17.
 */
public class ArticleViewCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(ArticleViewCommand.class);

  /**
   * 게시글 상세보기
   * <p> 게시글 상세보기, 게시글 파일 목록, 댓글 목록을 가져옴
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   * @author yoony
   * @version 1.0
   * @see MainCommand
   * @see ArticleDAO#selectArticle(int)
   * @see ArticleDAO#updateArticleViewCount(int)
   * @see FileDAO#selectFileList(int)
   * @see CommentDAO#selectCommentList(int)
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
      int articleId = RequestUtil.getIntParameter(request, "articleId");
      ArticleDTO articleDTO = articleDAO.selectArticle(articleId);
      if (articleDTO == null) {
        logger.error("해당 게시물이 존재하지 않습니다.");
        request.setAttribute("error", "1");
        return;
      }

      // 조회수 증가
      articleDAO.updateArticleViewCount(articleId);
      // 별 의미는 없지만 보여줄때 자기가 들어가서 올라간 조회수 확인하라고 +1해줌
      articleDTO.setViewCount(articleDTO.getViewCount() + 1);

      request.setAttribute("articleDTO", articleDTO);

      // 게시글에 파일이 존재하면 파일 목록을 가져옴
      if (articleDTO.getIsFileExist()) {
        FileDAO fileDAO = sqlSession.getMapper(FileDAO.class);
        List<FileDTO> fileList = fileDAO.selectFileList(articleId);
        request.setAttribute("fileList", fileList);
      }

      // 댓글 목록을 가져옴
      CommentDAO commentDAO = sqlSession.getMapper(CommentDAO.class);
      List<CommentDTO> commentList = commentDAO.selectCommentList(articleId);
      request.setAttribute("commentList", commentList);

    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
