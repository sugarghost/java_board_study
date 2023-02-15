package com.board.servlet.yoony.category;

/**
 * 카테고리를 DTO
 * @author yoony
 * @since 2023. 02. 15.
 * @version 1.0
 * @see CategoryDAO
 */
public class CategoryDTO {

  // 카테고리 ID(auto increment)
  private int categoryId;
  // 카테고리 이름
  private String name;

  public int getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


}
