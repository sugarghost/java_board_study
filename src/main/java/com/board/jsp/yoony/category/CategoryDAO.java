package com.board.jsp.yoony.category;

import com.board.jsp.yoony.database.MyDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class CategoryDAO {

  private Logger logger = LogManager.getLogger(CategoryDAO.class);
  private MyDatabase myDatabase = MyDatabase.getInstance();
  private static CategoryDAO categoryDAO = new CategoryDAO();

  private CategoryDAO() {
  }


  public static CategoryDAO getInstance() {
    return categoryDAO;
  }

  public List<CategoryDTO> getCategoryList() {
    logger.debug("getCategoryList()");
    List<CategoryDTO> categoryList = new ArrayList<CategoryDTO>();
    String query = "SELECT * FROM category";
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

      logger.debug("getCategoryList() : " + categoryList.size());
    } catch (Exception e) {
      logger.error("getCategoryList() ERROR : " + e.getMessage());
      e.printStackTrace();
    } finally {
      myDatabase.closeConnection(con, pstmt, rs);
    }
    return categoryList;
  }
}
