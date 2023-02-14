package com.board.jsp.yoony.article;

import com.board.jsp.yoony.database.MyDatabase;
import com.board.jsp.yoony.utill.SHA256;
import com.board.jsp.yoony.utill.ValidationChecker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ArticleDAO {

  private Logger logger = LogManager.getLogger(ArticleDAO.class);

  private MyDatabase myDatabase = MyDatabase.getInstance();

  private ValidationChecker validationChecker = ValidationChecker.getInstance();
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
    String query = "INSERT INTO article (category, writer, password, title, content) VALUES (?, ?, ?, ?, ?)";

    // insert 유효성과 update 유효성은 따로 고민해야함.
    if (!articleDTO.isInsertArticleValid()) {
      logger.debug("insertArticle() : invalid data");
      return 0;
    }

    try {
      SHA256 sha256 = new SHA256();
      articleDTO.setPassword(sha256.encrypt(articleDTO.getPassword()));
      con = myDatabase.getConnection();

      // insert 후 자동 증가된 ID값을 가져오기 위해 RETURN_GENERATED_KEYS 옵션을 사용
      pstmt = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
      pstmt.setString(1, articleDTO.getCategory());
      pstmt.setString(2, articleDTO.getWriter());
      pstmt.setString(3, articleDTO.getPassword());
      pstmt.setString(4, articleDTO.getTitle());
      pstmt.setString(5, articleDTO.getContent());
      result = pstmt.executeUpdate();
      logger.debug("insertArticle() : " + result);

      ResultSet generatedKeys = pstmt.getGeneratedKeys();
      if (generatedKeys.next()) {
        result = generatedKeys.getInt(1);
      } else {
        result = 0;
      }

    } catch (Exception e) {
      logger.error("insertArticle() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return result;
  }

  public int getArticleCount(Map<String, Object> map) {
    logger.debug("getArticleCount(Map map) : " + map.toString());
    int totalCount = 0;

    String query = "SELECT COUNT(*) FROM article WHERE 1=1";

    if (!validationChecker.CheckStringIsNullOrEmpty(map.get("searchWord"))) {
      query += " AND ("
          + "title LIKE '%" + map.get("searchWord") + "%' "
          + "OR writer LIKE '%" + map.get("searchWord") + "%' "
          + "OR content LIKE '%" + map.get("searchWord") + "%' "
          + ")";
    }

    if (!validationChecker.CheckStringIsNullOrEmpty(map.get("category"))) {
      query += " AND category = '" + map.get("category") + "'";
    }
    if (!validationChecker.CheckStringIsNullOrEmpty(map.get("startDate")) &&
        !validationChecker.CheckStringIsNullOrEmpty(map.get("endDate"))) {
      query += " AND created_date BETWEEN date('" + map.get("startDate") + "')"
          + " AND date('" + map.get("endDate") + "')+1";
    }
    query += " ORDER BY article_id DESC";
    logger.debug("getArticleList query : " + query);
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = myDatabase.getConnection();
      pstmt = con.prepareStatement(query);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        totalCount = rs.getInt(1);
      }
      logger.debug("getArticleCount() : " + totalCount);
    } catch (Exception e) {
      logger.error("getArticleCount() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return totalCount;
  }

  public List<ArticleDTO> getArticleList(Map<String, Object> map) {
    logger.debug("getArticleList(Map map) : " + map.toString());
    List<ArticleDTO> articleList = new ArrayList<ArticleDTO>();
    String query = "SELECT row_numbering_article_sub_table.* FROM (SELECT *, @ROWNUM:=@ROWNUM+1 AS row_num FROM article, (SELECT @ROWNUM:=0) AS R WHERE 1=1";

    if (!validationChecker.CheckStringIsNullOrEmpty(map.get("searchWord"))) {
      query += " AND ("
          + "title LIKE '%" + map.get("searchWord") + "%' "
          + "OR writer LIKE '%" + map.get("searchWord") + "%' "
          + "OR content LIKE '%" + map.get("searchWord") + "%' "
          + ")";
    }

    if (!validationChecker.CheckStringIsNullOrEmpty(map.get("category"))) {
      query += " AND category = '" + map.get("category") + "'";
    }
    if (!validationChecker.CheckStringIsNullOrEmpty(map.get("startDate")) &&
        !validationChecker.CheckStringIsNullOrEmpty(map.get("endDate"))) {
      query += " AND created_date BETWEEN date('" + map.get("startDate") + "')"
          + " AND date('" + map.get("endDate") + "')+1";
    }
    query += " ORDER BY article_id DESC"
        + ") row_numbering_article_sub_table WHERE row_num BETWEEN ? AND ?";
    logger.debug("getArticleList query : " + query);
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = myDatabase.getConnection();
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, map.get("rowStart").toString());
      pstmt.setString(2, map.get("rowEnd").toString());
      rs = pstmt.executeQuery();

      while (rs.next()) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setArticleId(rs.getInt("article_id"));
        articleDTO.setCategory(rs.getString("category"));
        articleDTO.setWriter(rs.getString("writer"));
        articleDTO.setTitle(rs.getString("title"));
        articleDTO.setContent(rs.getString("content"));
        articleDTO.setViewCount(rs.getInt("view_count"));
        articleDTO.setFileExistFlag((rs.getInt("file_exist_flag") == 1) ? true : false);
        articleDTO.setCreatedDate(rs.getDate("created_date"));
        articleDTO.setModifiedDate(rs.getDate("modified_date"));
        articleList.add(articleDTO);
      }

      logger.debug("getArticleList() : " + articleList.size());
    } catch (Exception e) {
      logger.error("getArticleList() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return articleList;
  }

  public ArticleDTO getArticle(int articleId) {
    logger.debug("getArticle(int articleId) : " + articleId);
    ArticleDTO articleDTO = new ArticleDTO();
    String query = "SELECT * FROM article WHERE article_id = ?";
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = myDatabase.getConnection();
      pstmt = con.prepareStatement(query);
      pstmt.setInt(1, articleId);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        articleDTO.setArticleId(rs.getInt("article_id"));
        articleDTO.setCategory(rs.getString("category"));
        articleDTO.setWriter(rs.getString("writer"));
        articleDTO.setTitle(rs.getString("title"));
        articleDTO.setContent(rs.getString("content"));
        articleDTO.setViewCount(rs.getInt("view_count"));
        articleDTO.setCreatedDate(rs.getDate("created_date"));
        articleDTO.setModifiedDate(rs.getDate("modified_date"));
        articleDTO.setFileExistFlag(rs.getInt("file_exist_flag") == 1 ? true : false);

        updateArticleViewCount(articleId);

      }
      logger.debug("getArticle() : " + articleDTO);
    } catch (Exception e) {
      logger.error("getArticle() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return articleDTO;
  }

  public boolean getPasswordCheck(int articleId, String password) {
    logger.debug(
        "getPasswordCheck(int articleId, String password) : " + articleId + ", " + password);
    boolean result = false;
    String query = "SELECT * FROM article WHERE article_id = ? AND password = ?";
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      SHA256 sha256 = new SHA256();
      String encryptPassword = sha256.encrypt(password);

      con = myDatabase.getConnection();
      pstmt = con.prepareStatement(query);
      pstmt.setInt(1, articleId);
      pstmt.setString(2, encryptPassword);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        result = true;
      }
      logger.debug("getPasswordCheck() : " + result);
    } catch (Exception e) {
      logger.error("getPasswordCheck() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return result;
  }

  public int updateArticle(ArticleDTO articleDTO) {
    logger.debug("updateArticle(ArticleDTO articleDTO) : " + articleDTO.toString());
    int result = 0;
    String query = "UPDATE article SET writer = ?, title = ?, content = ?, modified_date = current_timestamp() WHERE article_id = ? AND password = ?";
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    if (!articleDTO.isUpdateArticleValid()) {
      logger.debug("updateArticle() : invalid data");
      return 0;
    }

    try {
      SHA256 sha256 = new SHA256();
      String encryptPassword = sha256.encrypt(articleDTO.getPassword());

      con = myDatabase.getConnection();
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, articleDTO.getWriter());
      pstmt.setString(2, articleDTO.getTitle());
      pstmt.setString(3, articleDTO.getContent());
      pstmt.setInt(4, articleDTO.getArticleId());
      pstmt.setString(5, encryptPassword);
      result = pstmt.executeUpdate();
      logger.debug("updateArticle() : " + result);
    } catch (Exception e) {
      logger.error("updateArticle() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return result;
  }

  public int updateArticleViewCount(int articleId) {
    logger.debug("updateArticleViewCount(int articleId) : " + articleId);
    int result = 0;
    String query = "UPDATE article SET view_count = view_count + 1 WHERE article_id = ?";
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = myDatabase.getConnection();
      pstmt = con.prepareStatement(query);
      pstmt.setInt(1, articleId);
      result = pstmt.executeUpdate();
      logger.debug("updateArticleViewCount() : " + result);
    } catch (Exception e) {
      logger.error("updateArticleViewCount() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return result;
  }

  public int updateArticleFileExist(int articleId, boolean fileExist) {
    logger.debug("updateArticleFileExist(int articleId, boolean fileExist) : " + articleId + ", "
        + fileExist);
    int result = 0;
    String query = "UPDATE article SET file_exist_flag = ? WHERE article_id = ?";
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = myDatabase.getConnection();
      pstmt = con.prepareStatement(query);
      pstmt.setInt(1, fileExist ? 1 : 0);
      pstmt.setInt(2, articleId);
      result = pstmt.executeUpdate();
      logger.debug("updateArticleFileExist() : " + result);
    } catch (Exception e) {
      logger.error("updateArticleFileExist() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return result;
  }

  public int deleteArticle(int articleId, String password) {
    logger.debug("deleteArticle(int articleId, String password) : " + articleId + ", " + password);
    int result = 0;
    String query = "DELETE FROM article WHERE article_id = ? AND password = ?";
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      SHA256 sha256 = new SHA256();
      String encryptPassword = sha256.encrypt(password);

      con = myDatabase.getConnection();
      pstmt = con.prepareStatement(query);
      pstmt.setInt(1, articleId);
      pstmt.setString(2, encryptPassword);
      result = pstmt.executeUpdate();
      logger.debug("deleteArticle() : " + result);
    } catch (Exception e) {
      logger.error("deleteArticle() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return result;
  }
}
