package com.board.servlet.yoony.article;

import java.sql.Date;

/**
 * 게시글 DTO
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 15.
 * @see ArticleDAO
 */
public class ArticleDTO {

  // 게시글 ID(auto increment)
  private int articleId;
  // 카테고리 ID(foreign key: category table, auto increment)
  private int categoryId;
  // 작성자(3~4자)
  private String writer;
  // 비밀번호(4~15자, 영문, 숫자, 특수문자 조합)
  private String password;
  // 제목(4~99자)
  private String title;
  // 내용(4~1999자)
  private String content;
  // 조회수
  private int viewCount;
  // 작성일(current_timestamp())
  private Date createdDate;
  // 수정일
  private Date modifiedDate;

  public int getArticleId() {
    return articleId;
  }

  public void setArticleId(int articleId) {
    this.articleId = articleId;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  public String getWriter() {
    return writer;
  }

  public void setWriter(String writer) {
    this.writer = writer;
  }

  /**
   * 작성자가 유효한지 검사
   * null 체크, isEmpty 체크
   * 정규식 검사: 3~4자
   * @return 유효하면 true, 아니면 false
   */
  public boolean isWriterValid() {
    return writer != null && !writer.isEmpty() && writer.matches("^.{3,4}$");
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * 비밀번호가 유효한지 검사
   * null 체크, isEmpty 체크
   * 정규식 검사: 영문, 숫자, 특수문자 조합으로 4~15자
   * @return 유효하면 true, 아니면 false
   */
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

  /**
   * 제목이 유효한지 검사
   * null 체크, isEmpty 체크
   * 정규식 검사: 4~99자
   * @return 유효하면 true, 아니면 false
   */
  public boolean isTitleValid() {
    return title != null && !title.isEmpty() && title.matches("^.{4,99}$");
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  /**
   * 내용이 유효한지 검사
   * null 체크, isEmpty 체크
   * 정규식 검사: 4~1999자
   * @return 유효하면 true, 아니면 false
   */
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

  /**
   * 작성일이 유효한지 검사
   * null 체크
   * @return 유효하면 true, 아니면 false
   */
  public boolean isCreatedDateValid() {
    return createdDate != null;
  }

  public Date getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  /**
   * 수정일이 유효한지 검사
   * null 체크
   * @return 유효하면 true, 아니면 false
   */
  public boolean isModifiedDateValid() {
    return modifiedDate != null;
  }

  @Override
  public String toString() {
    return "ArticleDTO{" +
        "articleId=" + articleId +
        ", categoryId=" + categoryId +
        ", writer='" + writer + '\'' +
        ", password='" + password + '\'' +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", viewCount=" + viewCount +
        ", createdDate=" + createdDate +
        ", modifiedDate=" + modifiedDate +
        '}';
  }

  /**
   * 게시글 등록이 유효한지 검사
   * @return 유효하면 true, 아니면 false
   * @see ArticleDAO#insertArticle(ArticleDTO)
   * @see #isWriterValid()
   * @see #isPasswordValid()
   * @see #isTitleValid()
   * @see #isContentValid()
   */
  public boolean isInsertArticleValid() {
    return isWriterValid()
        && isPasswordValid()
        && isTitleValid()
        && isContentValid();
  }

  /**
   * 게시글 수정이 유효한지 검사
   * @return 유효하면 true, 아니면 false
   * @see ArticleDAO#updateArticle(ArticleDTO)
   * @see #isWriterValid()
   * @see #isPasswordValid()
   * @see #isTitleValid()
   * @see #isContentValid()
   */
  public boolean isUpdateArticleValid() {
    return isWriterValid()
        && isPasswordValid()
        && isTitleValid()
        && isContentValid();
  }
}
