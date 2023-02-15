package com.board.servlet.yoony.article;

import com.board.servlet.yoony.database.MyDatabase;
import com.board.servlet.yoony.util.Security;
import com.board.servlet.yoony.util.ValidationChecker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 게시글 DAO
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 14.
 * @see ArticleDTO
 */
public class ArticleDAO {

  private Logger logger = LogManager.getLogger(ArticleDAO.class);

  private MyDatabase myDatabase = MyDatabase.getInstance();

  private ValidationChecker validationChecker = ValidationChecker.getInstance();
  private static ArticleDAO articleDAO = new ArticleDAO();

  private ArticleDAO() {
  }

  /**
   * ArticleDAO의 인스턴스를 반환
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 15.
   * @return ArticleDAO
   */
  public static ArticleDAO getInstance() {
    return articleDAO;
  }


  /**
   * 게시글을 DB에 저장하는 메소드
   * 게시글을 DB에 저장하고, 저장된 게시글의 ID를 반환
   * 게시글의 유효성 검사는 ArticleDTO에서 수행
   * 게시글의 비밀번호는 SHA-256으로 암호화하여 저장
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 15.
   * @see ArticleDTO
   * @see Security
   * @param articleDTO 저장할 게시글
   * @return int 저장된 게시글의 ID
   */
  public int insertArticle(ArticleDTO articleDTO) {
    logger.debug("articleDTO : " + articleDTO.toString());
    int result = 0;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String query = "INSERT INTO article (category_id, writer, password, title, content) VALUES (?, ?, ?, ?, ?)";

    if (!articleDTO.isInsertArticleValid()) {
      logger.debug("insertArticle() : invalid data");
      return 0;
    }

    try {
      Security security = new Security();
      articleDTO.setPassword(security.sha256Encrypt(articleDTO.getPassword()));
      con = myDatabase.getConnection();

      // insert 후 자동 증가된 ID값을 가져오기 위해 RETURN_GENERATED_KEYS 옵션을 사용
      pstmt = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
      pstmt.setInt(1, articleDTO.getCategoryId());
      pstmt.setString(2, articleDTO.getWriter());
      pstmt.setString(3, articleDTO.getPassword());
      pstmt.setString(4, articleDTO.getTitle());
      pstmt.setString(5, articleDTO.getContent());
      result = pstmt.executeUpdate();
      logger.debug("insertArticle() result : " + result);

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


  // TODO: mybatis로 변경하면서 쿼리 수정하고 주석 달기

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
      query += " AND category_id = '" + map.get("category") + "'";
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
        articleDTO.setCategoryId(rs.getInt("category_id"));
        articleDTO.setWriter(rs.getString("writer"));
        articleDTO.setTitle(rs.getString("title"));
        articleDTO.setContent(rs.getString("content"));
        articleDTO.setViewCount(rs.getInt("view_count"));
        articleDTO.setCreatedDate(rs.getDate("created_date"));
        articleDTO.setModifiedDate(rs.getDate("modified_date"));
        articleList.add(articleDTO);
      }

      logger.debug("getArticleList() size : " + articleList.size());
    } catch (Exception e) {
      logger.error("getArticleList() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return articleList;
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
      query += " AND category_id = '" + map.get("category") + "'";
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
      logger.debug("getArticleCount() totalCount : " + totalCount);
    } catch (Exception e) {
      logger.error("getArticleCount() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return totalCount;
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
        articleDTO.setCategoryId(rs.getInt("category_id"));
        articleDTO.setWriter(rs.getString("writer"));
        articleDTO.setTitle(rs.getString("title"));
        articleDTO.setContent(rs.getString("content"));
        articleDTO.setViewCount(rs.getInt("view_count"));
        articleDTO.setCreatedDate(rs.getDate("created_date"));
        articleDTO.setModifiedDate(rs.getDate("modified_date"));

        updateArticleViewCount(articleId);

      }
      logger.debug("getArticle() articleDTO : " + articleDTO);
    } catch (Exception e) {
      logger.error("getArticle() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return articleDTO;
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
      logger.debug("updateArticleViewCount() result : " + result);
    } catch (Exception e) {
      logger.error("updateArticleViewCount() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return result;
  }
}