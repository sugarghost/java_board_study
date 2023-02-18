package com.board.servlet.yoony.article;

import com.board.servlet.yoony.MainCommand;
import com.board.servlet.yoony.category.CategoryDAO;
import com.board.servlet.yoony.category.CategoryDTO;
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
   * <p>articleId를 매개변수로 받아 게시글 정보를 가져옴
   * <p>게시글에 등록된 파일 정보가 있다면 파일 리스트를 가져옴
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   * @version 1.0
   * @author yoony
   * @see MainCommand
   * @see ArticleDAO#selectArticle(int)
   * @see FileDAO#selectFileList(int)
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
      // MyBatis Mapper 가져옴
      ArticleDAO articleDAO = sqlSession.getMapper(ArticleDAO.class);
      int articleId = RequestUtil.getIntParameter(request, "articleId");

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
