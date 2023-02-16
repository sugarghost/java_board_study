package com.board.servlet.yoony.article;

import java.sql.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 게시글 DTO
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 15.
 * @see ArticleDAO
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
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

  /**
   * 작성자가 유효한지 검사
   * null 체크, isEmpty 체크
   * 정규식 검사: 3~4자
   * @return 유효하면 true, 아니면 false
   */
  public boolean isWriterValid() {
    return writer != null && !writer.isEmpty() && writer.matches("^.{3,4}$");
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

  /**
   * 제목이 유효한지 검사
   * null 체크, isEmpty 체크
   * 정규식 검사: 4~99자
   * @return 유효하면 true, 아니면 false
   */
  public boolean isTitleValid() {
    return title != null && !title.isEmpty() && title.matches("^.{4,99}$");
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

  /**
   * 작성일이 유효한지 검사
   * null 체크
   * @return 유효하면 true, 아니면 false
   */
  public boolean isCreatedDateValid() {
    return createdDate != null;
  }

  /**
   * 수정일이 유효한지 검사
   * null 체크
   * @return 유효하면 true, 아니면 false
   */
  public boolean isModifiedDateValid() {
    return modifiedDate != null;
  }

  /**
   * 게시글 등록이 유효한지 검사
   * @return 유효하면 true, 아니면 false
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
