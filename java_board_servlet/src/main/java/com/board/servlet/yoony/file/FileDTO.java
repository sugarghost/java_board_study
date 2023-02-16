package com.board.servlet.yoony.file;

import java.sql.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 파일 DTO
 * @author yoony
 * @since 2023. 02. 16.
 * @version 1.0
 * @see FileDAO
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FileDTO {

  private int fileId;
  private int articleId;
  private String fileOriginName;
  private String fileSaveName;
  private String fileType;
  private String filePath;
  private Date createdDate;

}
