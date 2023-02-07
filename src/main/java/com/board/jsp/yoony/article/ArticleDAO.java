package com.board.jsp.yoony.article;

import com.board.jsp.yoony.database.MyDatabase;
import com.board.jsp.yoony.utill.SHA256;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ArticleDAO {

  private Logger logger = LogManager.getLogger(ArticleDAO.class);
  private static ArticleDAO articleDAO = new ArticleDAO();

  private ArticleDAO() {
  }

  public static ArticleDAO getInstance() {
    return articleDAO;
  }

  public int insertArticle(ArticleDTO articleDTO) {
    logger.debug("articleDTO : " + articleDTO.toString());
    int result = 0;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "INSERT INTO article (category, writer, password, title, content) VALUES (?, ?, ?, ?, ?)";
    if (!articleDTO.isCategoryValid() || !articleDTO.isWriterValid()
        || !articleDTO.isPasswordValid() || !articleDTO.isTitleValid()
        || !articleDTO.isContentValid()) {
      logger.debug("insertArticle() : invalid data");
      return 0;
    }

    try {
      SHA256 sha256 = new SHA256();
      articleDTO.setPassword(sha256.encrypt(articleDTO.getPassword()));
      con = MyDatabase.getConnection();

      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, articleDTO.getCategory());
      pstmt.setString(2, articleDTO.getWriter());
      pstmt.setString(3, articleDTO.getPassword());
      pstmt.setString(4, articleDTO.getTitle());
      pstmt.setString(5, articleDTO.getContent());
      result = pstmt.executeUpdate();
      logger.debug("insertArticle() : " + result);

    } catch (Exception e) {
      logger.error("insertArticle() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      MyDatabase.closeConnection(con, pstmt, rs);
      return result;
    }
  }

  public List<ArticleDTO> getArticleList(Map<String, Object> map) {
    logger.debug("getArticleList(Map map) : " + map.toString());
    List<ArticleDTO> articleList = new Vector<ArticleDTO>();
    String query = "SELECT * FROM article WHERE 1=1 ";

    if (map.get("searchWord") != null) {
      query += " AND ("
          + "title LIKE '%" + map.get("searchWord") + "%' "
          + "OR writer LIKE '%" + map.get("searchWord") + "%' "
          + "OR content LIKE '%" + map.get("searchWord") + "%' "
          + ")";
    }

    if (map.get("category") != null) {
      query += " AND category = '" + map.get("category") + "'";
    }
    if (map.get("startDate") != null && map.get("endDate") != null) {
      query += " AND created_date BETWEEN date('" + map.get("startDate") + "')"
          + " AND date('" + map.get("endDate") + "')+1";
    }
    query += " ORDER BY article_id DESC";
    logger.debug("getArticleList query : " + query);
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = MyDatabase.getConnection();
      pstmt = con.prepareStatement(query);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setArticleId(rs.getInt("article_id"));
        articleDTO.setCategory(rs.getString("category"));
        articleDTO.setWriter(rs.getString("writer"));
        articleDTO.setTitle(rs.getString("title"));
        articleDTO.setContent(rs.getString("content"));
        articleDTO.setViewCount(rs.getInt("view_count"));
        articleDTO.setCreatedDate(rs.getDate("created_date"));
        articleDTO.setModifiedDate(rs.getDate("modified_date"));
        articleList.add(articleDTO);
      }

      logger.debug("getArticleList() : " + articleList.size());
    } catch (Exception e) {
      logger.error("getArticleList() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      MyDatabase.closeConnection(con, pstmt, rs);
      return articleList;
    }
  }
}
