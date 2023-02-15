package com.board.servlet.yoony.article.page;

public class PageDTO {

  private int pageNum;
  private int pageSize;
  private int blockPage;
  private int totalCount;
  private int totalPage;
  private int startNum;
  private int endNum;

  public PageDTO() {
  }
  public PageDTO(int pageNum, int pageSize, int blockPage, int totalCount) {
    this.pageNum = pageNum;
    this.pageSize = pageSize;
    this.blockPage = blockPage;
    this.totalCount = totalCount;
    this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    this.startNum = (((pageNum - 1) / blockPage) * blockPage) + 1;
    this.endNum = startNum + blockPage - 1;
  }

  public int getPageNum() {
    return pageNum;
  }

  public void setPageNum(int pageNum) {
    this.pageNum = pageNum;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getBlockPage() {
    return blockPage;
  }

  public void setBlockPage(int blockPage) {
    this.blockPage = blockPage;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }

  public int getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(int totalPage) {
    this.totalPage = totalPage;
  }

  public int getStartNum() {
    return startNum;
  }

  public void setStartNum(int startNum) {
    this.startNum = startNum;
  }

  public int getEndNum() {
    return endNum;
  }

  public void setEndNum(int endNum) {
    this.endNum = endNum;
  }
}
