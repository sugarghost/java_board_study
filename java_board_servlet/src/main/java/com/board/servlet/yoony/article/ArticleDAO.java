package com.board.servlet.yoony.article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ArticleDAO {
  public int selectArticleCount(Map<String, Object> params);
  public List<ArticleDTO> selectArticleList(Map<String, Object> params);
}
