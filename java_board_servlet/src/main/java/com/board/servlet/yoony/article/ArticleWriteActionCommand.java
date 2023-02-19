package com.board.servlet.yoony.article;

import com.board.servlet.yoony.MainCommand;
import com.board.servlet.yoony.category.CategoryDAO;
import com.board.servlet.yoony.category.CategoryDTO;
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
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 게시글 등록 액션을 처리하는 커맨드
 *
 * @version 1.0
 * @aothor yoony
 * @see MainCommand
 * @since 2023. 02. 18.
 */
public class ArticleWriteActionCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(ArticleWriteActionCommand.class);

  /**
   * 게시글 등록 액션을 처리하는 메소드
   * <p>게시글 등록을 위해 제목, 내용, 작성자, 카테고리, 비밀번호를 입력받아 해당 게시글을 등록
   * <p>파일이 첨부되었다면 파일을 저장하고 파일 정보를 DB에 저장
   * <p>사용되는 에러 코드는 write.do에서 정의됨
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   * @throws IOException
   * @version 1.0
   * @aothor yoony
   * @see MainCommand
   * @see ArticleDAO#insertArticle(ArticleDTO)
   * @see FileDAO#insertFile(FileDTO)
   * @since 2023. 02. 18.
   */
  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response) {
    // TODO: 코드를 줄일려는 노력해야함, 리팩토링 중심 코드
    // Try catch만 줄여도 코드가 줄어듬(그냥 던져버리기)(아니면 핸들러를 마련해서 핸들러가 처리를 해도 코드가 깔끔)
    logger.debug("execute()");
    // MyBatis instance 가져옴
    MyBatisConfig myBatisConfig = MyBatisConfig.getInstance();
    logger.debug(request);
    // TODO: properties로 빼기(아래 3줄): 정적 자원은 resources에 빼기,
    // TODO: properties를 사용할때 국제화 고민하기(locale)(경험해보면 좋음, 별도 TODO)
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
      articleDTO.setTitle(multi.getParameter("title"));
      articleDTO.setContent(multi.getParameter("content"));
      articleDTO.setWriter(multi.getParameter("writer"));
      articleDTO.setCategoryId(RequestUtil.getIntParameter(multi, "categoryId"));
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
        } catch (Exception e) {
          logger.error(e);
          sqlSession.rollback();
          request.setAttribute("error", "3");
        }
      } else {
        logger.error("게시글 등록 실패");
        // TODO: rollback 처리 같은 마지막 처리 필요 부분은 fanllay에서 처리(에러 체크를 통해 마지막 결정 등...)
        sqlSession.rollback();
        request.setAttribute("error", "2");
      }
    } catch (IOException e) {
      logger.error(e);
      // TODO: 다시한번 명시하지만 에러 처리 부분은 따로 클래스를 빼거나 처리하기(명확하게)(본인만의 Exception 만들기)
      // 현재 코드 상으로는 어떤 에러인지 알기 힘듬
      request.setAttribute("error", "2");
    } catch (Exception e) {
      // catch에 e.printStackTrace()를 사용하는 이유는 Log 때문
      logger.error(e);
      request.setAttribute("error", "2");
    }
  }
}
