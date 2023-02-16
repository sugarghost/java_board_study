package com.board.servlet.yoony.comment;

import java.sql.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommentDTO {

  private int commentId;
  private int articleId;
  private String content;
  private Date createdDate;

}
