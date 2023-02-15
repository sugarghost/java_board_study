package com.board.servlet.yoony.category;

import com.board.servlet.yoony.database.MyDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 카테고리 DAO
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 15.
 * @see CategoryDTO
 */
public class CategoryDAO {

  private Logger logger = LogManager.getLogger(CategoryDAO.class);
  private MyDatabase myDatabase = MyDatabase.getInstance();
  private static CategoryDAO categoryDAO = new CategoryDAO();

  private CategoryDAO() {
  }

  /**
   * CategoryDAO의 인스턴스를 반환
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 15.
   * @return CategoryDAO
   */
  public static CategoryDAO getInstance() {
    return categoryDAO;
  }

  /**
   * 카테고리 목록을 반환하는 메소드
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 15.
   * @see CategoryDTO
   * @return List<CategoryDTO> 카테고리 목록
   */
  public List<CategoryDTO> getCategoryList() {
    logger.debug("getCategoryList()");
    List<CategoryDTO> categoryList = new ArrayList<CategoryDTO>();
    String query = "SELECT category_id, name FROM category ORDER BY category_id";
    logger.debug("query : " + query);

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = myDatabase.getConnection();
      pstmt = con.prepareStatement(query);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(rs.getInt("category_id"));
        categoryDTO.setName(rs.getString("name"));
        categoryList.add(categoryDTO);
      }

      logger.debug("getCategoryList() size : " + categoryList.size());
    } catch (Exception e) {
      logger.error("getCategoryList() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return categoryList;
  }
  public HashMap<Integer,String> getCategoryMap() {
    logger.debug("getCategoryMap()");
    HashMap<Integer,String> categoryMap = new HashMap<>();
    String query = "SELECT category_id, name FROM category ORDER BY category_id";
    logger.debug("query : " + query);

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = myDatabase.getConnection();
      pstmt = con.prepareStatement(query);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        categoryMap.put(rs.getInt("category_id"),rs.getString("name"));
      }

      logger.debug("getCategoryList() size : " + categoryMap.size());
    } catch (Exception e) {
      logger.error("getCategoryList() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return categoryMap;
  }
}
