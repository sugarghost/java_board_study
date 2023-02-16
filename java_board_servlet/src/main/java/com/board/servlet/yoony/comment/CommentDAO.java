package com.board.servlet.yoony.comment;

import java.util.List;

public interface CommentDAO {
  public int insertComment(CommentDTO commentDTO);
  public List<CommentDTO> getCommentList(int articleId);
}
