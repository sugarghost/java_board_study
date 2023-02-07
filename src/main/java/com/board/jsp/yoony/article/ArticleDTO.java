package com.board.jsp.yoony.article;

import java.sql.Date;

public class ArticleDTO {

  private int articleId;
  private String category;
  private String writer;
  private String password;
  private String title;
  private String content;
  private int viewCount;
  private Date createdDate;
  private Date modifiedDate;

  public Number getArticleId() {
    return articleId;
  }

  public void setArticleId(int articleId) {
    this.articleId = articleId;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public boolean isCategoryValid() {
    return category != null && !category.isEmpty();
  }

  public String getWriter() {
    return writer;
  }

  public void setWriter(String writer) {
    this.writer = writer;
  }

  public boolean isWriterValid() {
    return writer != null && !writer.isEmpty() && writer.matches("^.{3,4}$");
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isPasswordValid() {
    return password != null && !password.isEmpty() && password.matches(
        "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{4,15}$");
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isTitleValid() {
    return title != null && !title.isEmpty() && title.matches("^.{4,99}$");
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public boolean isContentValid() {
    return content != null && !content.isEmpty() && content.matches("^.{4,1999}$");
  }

  public int getViewCount() {
    return viewCount;
  }

  public void setViewCount(int viewCount) {
    this.viewCount = viewCount;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  @Override
  public String toString() {
    return "ArticleDTO{" +
        "articleId=" + articleId +
        ", category='" + category + '\'' +
        ", writer='" + writer + '\'' +
        ", password='" + password + '\'' +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", viewCount=" + viewCount +
        ", createdDate=" + createdDate +
        ", modifiedDate=" + modifiedDate +
        '}';
  }
}
