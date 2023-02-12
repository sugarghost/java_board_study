package com.board.jsp.yoony.article.file;

import java.sql.Date;

public class FileDTO {

  private int fileId;
  private int articleId;
  private String fileOriginName;
  private String fileSaveName;
  private String fileType;
  private Date createdDate;

  public int getFileId() {
    return fileId;
  }

  public void setFileId(int fileId) {
    this.fileId = fileId;
  }

  public int getArticleId() {
    return articleId;
  }

  public void setArticleId(int articleId) {
    this.articleId = articleId;
  }

  public String getFileSaveName() {
    return fileSaveName;
  }

  public void setFileSaveName(String fileSaveName) {
    this.fileSaveName = fileSaveName;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFileOriginName() {
    return fileOriginName;
  }

  public void setFileOriginName(String fileOriginName) {
    this.fileOriginName = fileOriginName;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }
}
