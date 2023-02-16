package com.board.servlet.yoony.category;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryDAO {
  public List<CategoryDTO> selectCategoryList();
  //public Map selectCategoryMap();
}
