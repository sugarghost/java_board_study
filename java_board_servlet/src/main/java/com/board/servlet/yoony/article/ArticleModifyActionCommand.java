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
import java.util.Enumeration;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 게시글 수정 처리를 위한 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @since 2023. 02. 18.
 */
public class ArticleModifyActionCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(ArticleModifyActionCommand.class);

  /**
   * 게시글 수정 처리를 위한 메소드
   * <p>게시글 수정을 위해 articleId, title, content, writer, password를 매개변수로 받아옴
   * <p>password를 sha256으로 암호화하고 게시글의 비밀번호와 일치하는지 확인
   * <p>일치하면 게시글을 수정하고 해당 게시글에 속한 파일들을 수정
   * <p>파일이 등록되면 파일을 저장하고 파일 정보를 DB에 저장
   * <p>삭제 대상 파일 id를 deleteFileId으로 받아와서 해당 파일을 삭제
   * <p>사용되는 에러 코드는 modify.do에서 정의됨
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   * @version 1.0
   * @aothor yoony
   * @see MainCommand
   * @see ArticleDAO#selectPasswordCheck(ArticleDTO)
   * @see ArticleDAO#updateArticle(ArticleDTO)
   * @see FileDAO#deleteFile(FileDTO)
   * @see FileDAO#insertFile(FileDTO)
   * @since 2023. 02. 18.
   */

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response) {
    logger.debug("execute()");
    // MyBatis instance 가져옴
    MyBatisConfig myBatisConfig = MyBatisConfig.getInstance();
    logger.debug(request);
    String saveDirectory = "C:\\tempUploads";
    int maxPostSize = 10 * 1024 * 1024; // 10MB 제한
    String encoding = "UTF-8";
    ArticleDTO articleDTO = new ArticleDTO();

    // auto close를 위한 try-with-resource
    // 트랜잭션 처리를 위해 sqlSession을 하나 생성하고 Article과 File이 같이 사용
    try (
        SqlSession sqlSession = myBatisConfig.getSqlSessionFactory().openSession();
    ) {
      MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize,
          encoding, new DefaultFileRenamePolicy());
      articleDTO.setArticleId(RequestUtil.getIntParameter(multi, "articleId"));

      articleDTO.setTitle(multi.getParameter("title"));
      articleDTO.setContent(multi.getParameter("content"));
      articleDTO.setWriter(multi.getParameter("writer"));
      articleDTO.setPassword(multi.getParameter("password"));

      if (!articleDTO.isUpdateArticleValid()) {
        logger.debug("게시글 수정에 필요한 정보가 유효하지 않습니다!");
        request.setAttribute("error", "1");
        return;
      }
      articleDTO.setPassword(Security.sha256Encrypt(multi.getParameter("password")));

      // MyBatis Mapper 가져옴
      ArticleDAO articleDAO = sqlSession.getMapper(ArticleDAO.class);
      boolean isPasswordValid = articleDAO.selectPasswordCheck(articleDTO);

      // 패스워드가 일치하지 않으면 에러
      if (!isPasswordValid) {
        logger.debug("패스워드가 일치하지 않습니다.");
        request.setAttribute("error", "2");
        return;
      }
      // 검사를 통과했으면 수정을 진행

      int articleUpdateResult = articleDAO.updateArticle(articleDTO);
      if (articleUpdateResult < 1) {
        logger.debug("게시글 수정에 실패했습니다.");
        request.setAttribute("error", "3");
        return;
      }
      // 파일 삭제 대상의 ID들을 받아옴
      String[] deleteFileIds = multi.getParameterValues("deleteFileId");
      FileDAO fileDAO = sqlSession.getMapper(FileDAO.class);

      // 파일 삭제 대상이 있다면 삭제

      if (deleteFileIds != null) {
        for (String deleteFileId : deleteFileIds) {
          FileDTO deleteFileDTO = new FileDTO();
          deleteFileDTO.setFileId(Integer.parseInt(deleteFileId));
          deleteFileDTO.setArticleId(articleDTO.getArticleId());
          // 시간 관계상 처리가 안된 File에 대한 분기를 생략
          int fileDeleteResult = fileDAO.deleteFile(deleteFileDTO);
          if (fileDeleteResult > 0) {
            // 실제 파일 삭제(보류)
            // File deleteFile = new File(saveDirectory + File.separator + 실제 저장 파일 이름);
            // deleteFile.delete();
          }
        }
      }

      // 파일 삭제와 별개로 넘어온 파일이 있는 경우 Upload 처리
      try {
        Enumeration uploadfiles = multi.getFileNames();
        while (uploadfiles.hasMoreElements()) {
          String file = (String) uploadfiles.nextElement();
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
          fileDTO.setArticleId(articleDTO.getArticleId());
          fileDTO.setFileOriginName(fileName);
          fileDTO.setFileSaveName(newFileName);
          fileDTO.setFileType(ext);
          fileDTO.setFilePath(saveDirectory);

          int fileInsertResult = fileDAO.insertFile(fileDTO);

          if (fileInsertResult == 0) {
            logger.debug("파일 업로드에 실패했습니다.");
            request.setAttribute("error", "4");
            return;
          }
        }

      } catch (Exception e) {
        logger.error(e);
        e.printStackTrace();
        request.setAttribute("error", "4");
      }

      // 작업이 다 완료되면서 이상이 없으면 commit
      sqlSession.commit();
    } catch (IOException e) {
      logger.error(e);
      request.setAttribute("error", "3");
    } catch (Exception e) {
      logger.error(e);
      request.setAttribute("error", "3");
    }
  }
}
