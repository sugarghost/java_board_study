package com.board.jsp.yoony.comment;

import com.board.jsp.yoony.database.MyDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommentDAO {

  private Logger logger = LogManager.getLogger(CommentDAO.class);
  private MyDatabase myDatabase = MyDatabase.getInstance();
  private static CommentDAO instance = new CommentDAO();

  private CommentDAO() {
  }

  public static CommentDAO getInstance() { return instance; }

  public int insertComment(CommentDTO commentDTO) {
    logger.debug("insertComment() : " + commentDTO.toString());
    int result = 0;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "INSERT INTO comment (article_id, content) VALUES (?, ?)";
    try {
      conn = myDatabase.getConnection();
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, commentDTO.getArticleId());
      pstmt.setString(2, commentDTO.getContent());
      result = pstmt.executeUpdate();
      logger.debug("insertComment() : " + result);
    } catch (Exception e) {
      logger.error("insertComment() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(conn, pstmt, rs);
    }
    return result;
  }

  public List<CommentDTO> getCommentList(int articleId) {
    logger.debug("getCommentList() : " + articleId);
    List<CommentDTO> commentList = new ArrayList<CommentDTO>();
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "SELECT * FROM comment WHERE article_id = ?";
    try {
      conn = myDatabase.getConnection();
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, articleId);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(rs.getInt("comment_id"));
        commentDTO.setArticleId(rs.getInt("article_id"));
        commentDTO.setContent(rs.getString("content"));
        commentDTO.setCreatedDate(rs.getDate("created_date"));
        commentList.add(commentDTO);
      }
    } catch (Exception e) {
      logger.error("getCommentList() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(conn, pstmt, rs);
    }
    return commentList;
  }

}
