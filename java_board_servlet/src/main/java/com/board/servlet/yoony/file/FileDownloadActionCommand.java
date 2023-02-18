package com.board.servlet.yoony.file;

import com.board.servlet.yoony.MainCommand;
import com.board.servlet.yoony.category.CategoryDAO;
import com.board.servlet.yoony.category.CategoryDTO;
import com.board.servlet.yoony.database.MyBatisConfig;
import com.board.servlet.yoony.util.RequestUtil;
import com.board.servlet.yoony.util.ValidationChecker;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 파일 다운로드를 위한 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @since 2023. 02. 18.
 */
public class FileDownloadActionCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(FileDownloadActionCommand.class);

  /**
   * 파일 다운로드를 위한 메소드
   * <p>파라미터로 받은 articleId와 fileId를 통해 파일을 찾아 다운로드함
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   * @author yoony
   * @version 1.0
   * @see MainCommand
   * @see FileDAO#selectFile(Map)
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
      int articleId = RequestUtil.getIntParameter(request, "articleId");
      int fileId = RequestUtil.getIntParameter(request, "fileId");

      // MyBatis Mapper 가져옴
      FileDAO fileDAO = sqlSession.getMapper(FileDAO.class);

      // selectFile을 위한 파라미터
      Map<String, Integer> param = new HashMap<String, Integer>();
      param.put("articleId", articleId);
      param.put("fileId", fileId);
      FileDTO fileDTO = fileDAO.selectFile(param);

      if (fileDTO == null) {
        logger.error("파일 정보를 찾을 수 없습니다!");
        return;
      }

      String saveDirectory = fileDTO.getFilePath();
      String fileOriginName = fileDTO.getFileOriginName();
      String fileSaveName = fileDTO.getFileSaveName();

      File file = new File(saveDirectory + File.separator + fileSaveName);
      InputStream inStream = new FileInputStream(file);

      // 한글 이름 꺠짐 방지
      String client = request.getHeader("User-Agent");
      if (client.indexOf("WOW64") == -1) {
        fileOriginName = new String(fileOriginName.getBytes("UTF-8"), "ISO-8859-1");
      } else {
        fileOriginName = new String(fileOriginName.getBytes("KSC5601"), "ISO-8859-1");
      }

      // 파일 다운로드용 응답 헤더 설정
      response.reset();
      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition",
          "attachment; filename=\"" + fileOriginName + "\"");
      response.setHeader("Content-Length", String.valueOf(file.length()));

      OutputStream outStream = response.getOutputStream();

      byte[] buffer = new byte[(int) file.length()];
      int readBuffer = 0;
      while ((readBuffer = inStream.read(buffer)) > 0) {
        outStream.write(buffer, 0, readBuffer);
      }

      inStream.close();
      outStream.close();
    } catch (FileNotFoundException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
