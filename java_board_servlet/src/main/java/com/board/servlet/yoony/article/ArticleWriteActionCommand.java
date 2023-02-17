package com.board.servlet.yoony.article;

import com.board.servlet.yoony.MainCommand;
import com.board.servlet.yoony.category.CategoryDAO;
import com.board.servlet.yoony.category.CategoryDTO;
import com.board.servlet.yoony.database.MyBatisConfig;
import com.board.servlet.yoony.file.FileDAO;
import com.board.servlet.yoony.file.FileDTO;
import com.board.servlet.yoony.util.Security;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Article write action command.
 */
public class ArticleWriteActionCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(ArticleWriteActionCommand.class);

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response) {
    logger.debug("ArticleWriteActionCommand.execute()");
    // MyBatis instance 가져옴
    MyBatisConfig myBatisConfig = MyBatisConfig.getInstance();
  logger.debug(request);
    String saveDirectory = "C:\\tempUploads";
    int maxPostSize = 10 * 1024 * 1024; // 10MB 제한
    String encoding = "UTF-8";
    ArticleDTO articleDTO = new ArticleDTO();

    // auto close를 위한 try-with-resource
    try (
        SqlSession sqlSession = myBatisConfig.getSqlSessionFactory().openSession();
    ) {
      MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize,
          encoding, new DefaultFileRenamePolicy());
      articleDTO.setTitle(multi.getParameter("title"));
      articleDTO.setContent(multi.getParameter("content"));
      articleDTO.setWriter(multi.getParameter("writer"));
      articleDTO.setCategoryId(Integer.parseInt((multi.getParameter("categoryId") != null) ? multi.getParameter("categoryId") : "0" ));
      articleDTO.setPassword(multi.getParameter("password"));

      if (!articleDTO.isInsertArticleValid()) {
        logger.debug("isInsertArticleValid() : invalid data");
        request.setAttribute("error", "1");
        return;
      }
      articleDTO.setPassword(Security.sha256Encrypt(multi.getParameter("password")));


      // MyBatis Mapper 가져옴
      ArticleDAO articleDAO = sqlSession.getMapper(ArticleDAO.class);
      // 게시글을 등록함
      int articleInsertResult = articleDAO.insertArticle(articleDTO);
      int articleId = articleDTO.getArticleId();
      logger.debug("articleId : " + articleId);
      // 게시글 등록 성공 여부를 확인함
      if (articleInsertResult > 0) {
        try {
          Enumeration files = multi.getFileNames();
          while (files.hasMoreElements()) {
            String file = (String) files.nextElement();
            String fileName = multi.getOriginalFileName(file);
            if (fileName == null) {
              continue;
            }
            String realFileName = multi.getFilesystemName(file);
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

            String newFileName = UUID.randomUUID() + "." + ext;

            File oldFile = new File(saveDirectory + File.separator + realFileName);
            File newFile = new File(saveDirectory + File.separator + newFileName);
            oldFile.renameTo(newFile);

            FileDTO fileDTO = new FileDTO();
            fileDTO.setArticleId(articleId);
            fileDTO.setFileOriginName(fileName);
            fileDTO.setFileSaveName(newFileName);
            fileDTO.setFileType(ext);
            fileDTO.setFilePath(saveDirectory);

            FileDAO fileDAO = sqlSession.getMapper(FileDAO.class);
            int fileInsertResult = fileDAO.insertFile(fileDTO);

            // 등록 중 실패해도 catch가 발생하지 않는다면 rollback이 발생하지 않고 진행
            // TODO: 여러개 중 한개가 실패했을때 고민 필요
            if (fileInsertResult == 0) {
              logger.error("파일 등록 실패 :" + fileName);
            }
          }
          // 게시글 등록 성공하고 파일 등록에 Exception이 발생하지 않았을 경우 commit
          sqlSession.commit();
          // 게시글 등록 시 forward 방식으로 list.JSP로 이동
          // 즉, list.JSP로 이동해서 위에 주소는 WriteAction을 잡고있음
          // 링크 이동은 문제없지만 새로고침을 하면 계속 게시글이 등록됨
          // TODO: 새로고침을 하면 계속 게시글이 등록되는 문제 해결 필요
        } catch (Exception e) {
          logger.error(e);
          sqlSession.rollback();
          request.setAttribute("error", "3");
        }
      } else {
        logger.error("게시글 등록 실패");
        sqlSession.rollback();
        request.setAttribute("error", "2");
      }
    } catch (IOException e) {
      logger.error(e);
      request.setAttribute("error", "2");
    } catch (Exception e) {
      logger.error(e);
      request.setAttribute("error", "2");
    }
  }
}
