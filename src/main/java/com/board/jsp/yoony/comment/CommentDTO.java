package com.board.jsp.yoony.comment;

import java.sql.Date;

public class CommentDTO {

  private Number commentId;
  private Number articleId;
  private String content;
  private Date createdDate;

  public Number getCommentId() {
    return commentId;
  }

  public void setCommentId(Number commentId) {
    this.commentId = commentId;
  }

  public Number getArticleId() {
    return articleId;
  }

  public void setArticleId(Number articleId) {
    this.articleId = articleId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }
}
