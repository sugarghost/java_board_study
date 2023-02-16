package com.board.servlet.yoony.file;

import java.util.List;

public interface FileDAO {
  public int insertFile(FileDTO fileDTO);
  public List<FileDTO> selectFileList(int articleId);
  public int selectFileCount(int articleId);
  public int deleteFile(int fileId, int articleId);
  public int deleteAllFile(int articleId);
}
