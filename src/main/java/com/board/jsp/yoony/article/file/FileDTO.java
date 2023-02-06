package com.board.jsp.yoony.article.file;

public class FileDTO {

  private Number fileId;
  private Number articleId;
  private String fileName;
  private String filePath;
  private String fileType;

  public Number getFileId() {
    return fileId;
  }

  public void setFileId(Number fileId) {
    this.fileId = fileId;
  }

  public Number getArticleId() {
    return articleId;
  }

  public void setArticleId(Number articleId) {
    this.articleId = articleId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }
}
