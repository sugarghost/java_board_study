package com.board.servlet.yoony.article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleDAO {
  public int insertArticle(ArticleDTO articleDTO);
  public int selectArticleCount(Map<String, Object> params);
  public List<ArticleDTO> selectArticleList(Map<String, Object> params);
  public ArticleDTO selectArticle(int articleId);
  public boolean selectPasswordCheck(int articleId, String password);
  public int updateArticle(ArticleDTO articleDTO);
  public int deleteArticle(int articleId, String password);
}
