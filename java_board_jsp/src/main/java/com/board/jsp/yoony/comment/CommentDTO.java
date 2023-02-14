package com.board.jsp.yoony.comment;

import java.sql.Date;

public class CommentDTO {

  private int commentId;
  private int articleId;
  private String content;
  private Date createdDate;

  public int getCommentId() {
    return commentId;
  }

  public void setCommentId(int commentId) {
    this.commentId = commentId;
  }

  public int getArticleId() {
    return articleId;
  }

  public void setArticleId(int articleId) {
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

  @Override
  public String toString() {
    return "CommentDTO{" +
        "commentId=" + commentId +
        ", articleId=" + articleId +
        ", content='" + content + '\'' +
        ", createdDate=" + createdDate +
        '}';
  }
}
