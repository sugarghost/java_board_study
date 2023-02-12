package com.board.jsp.yoony.article.file;

import com.board.jsp.yoony.article.ArticleDAO;
import com.board.jsp.yoony.database.MyDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Vector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileDAO {

  private Logger logger = LogManager.getLogger(FileDAO.class);
  private static FileDAO fileDAO = new FileDAO();

  private FileDAO() {
  }

  public static FileDAO getInstance() {
    return fileDAO;
  }

  public int insertFile(FileDTO fileDTO) {
    logger.debug("insertFile() : " + fileDTO);

    int applyResult = 0;

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "INSERT INTO article_file (article_id, file_origin_name, file_save_name, file_type) VALUES (?, ?, ?, ?)";

    try {
      con = MyDatabase.getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setInt(1, fileDTO.getArticleId());
      pstmt.setString(2, fileDTO.getFileOriginName());
      pstmt.setString(3, fileDTO.getFileSaveName());
      pstmt.setString(4, fileDTO.getFileType());
      applyResult = pstmt.executeUpdate();
      logger.debug("insertFile() : " + applyResult);
    } catch (Exception e) {
      logger.error("insertFile() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      MyDatabase.closeConnection(con, pstmt, rs);
      return applyResult;
    }
  }

  public List<FileDTO> getFileList(int articleId) {
    logger.debug("getFileList() : " + articleId);
    List<FileDTO> fileList = new Vector<FileDTO>();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "SELECT * FROM article_file WHERE article_id = ?";

    try {
      con = MyDatabase.getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setInt(1, articleId);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileId(rs.getInt("file_id"));
        fileDTO.setArticleId(rs.getInt("article_id"));
        fileDTO.setFileOriginName(rs.getString("file_origin_name"));
        fileDTO.setFileSaveName(rs.getString("file_save_name"));
        fileDTO.setFileType(rs.getString("file_type"));
        fileDTO.setCreatedDate(rs.getDate("created_date"));
        fileList.add(fileDTO);
      }
    } catch (Exception e) {
      logger.error("getFileList() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      MyDatabase.closeConnection(con, pstmt, rs);
      return fileList;
    }
  }

  public int getFileCount(int articleId) {
    logger.debug("getFileCount(int articleId) : " + articleId);
    int fileCount = 0;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "SELECT COUNT(*) FROM article_file WHERE article_id = ?";
    try {
      con = MyDatabase.getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setInt(1, articleId);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        fileCount = rs.getInt(1);
      }
      logger.debug("getFileCount() : " + fileCount);
    } catch (Exception e) {
      logger.error("getFileCount() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      MyDatabase.closeConnection(con, pstmt, rs);
      return fileCount;
    }
  }

  public int deleteFile(int fileId, int articleId) {
    logger.debug("deleteFile(int fileId, int articleId) : " + fileId + ", " + articleId);
    int result = 0;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "DELETE FROM article_file WHERE file_id = ? AND article_id = ?";
    try {
      con = MyDatabase.getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setInt(1, fileId);
      pstmt.setInt(2, articleId);
      result = pstmt.executeUpdate();
      logger.debug("deleteFile() : " + result);
    } catch (Exception e) {
      logger.error("deleteFile() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      MyDatabase.closeConnection(con, pstmt, rs);
      return result;
    }
  }

  public int deleteAllFile(int articleId) {
    logger.debug("deleteAllFile(int articleId) : " + articleId);
    int result = 0;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "DELETE FROM article_file WHERE article_id = ?";
    try {
      con = MyDatabase.getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setInt(1, articleId);
      result = pstmt.executeUpdate();
      logger.debug("deleteAllFile() : " + result);
    } catch (Exception e) {
      logger.error("deleteAllFile() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      MyDatabase.closeConnection(con, pstmt, rs);
      return result;
    }
  }
}
