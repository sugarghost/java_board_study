package com.board.servlet.yoony.file;

import java.sql.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 파일 DTO
 *
 * @author yoony
 * @version 1.0
 * @see FileDAO
 * @since 2023. 02. 16.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FileDTO {

  // 파일 ID(auto increment)
  private int fileId;
  // 게시글 ID(foreign key: article table)
  private int articleId;
  // 원본 파일명(사용자가 등록한 당시 파일명)
  private String fileOriginName;
  // 저장 파일명(서버에 저장된 파일명으로 UUID를 사용)
  private String fileSaveName;
  // 파일 타입
  private String fileType;
  // 파일 경로
  private String filePath;
  // 파일 등록일 (current_timestamp())
  private Date createdDate;
}
