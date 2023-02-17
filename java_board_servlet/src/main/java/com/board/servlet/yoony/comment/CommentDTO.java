package com.board.servlet.yoony.comment;

import java.sql.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 댓글 DTO
 * @author yoony
 * @since 2023. 02. 17.
 * @version 1.0
 * @see CommentDAO
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommentDTO {

  // 댓글 id(auto increment)
  private int commentId;
  // 게시글 id(foreign key: article table)
  private int articleId;
  // 내용
  private String content;
  // 작성일(current_timestamp())
  private Date createdDate;

}
