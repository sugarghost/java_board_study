package com.board.servlet.yoony.file;

import java.util.List;
/**
 * 파일 관련 DAO 인터페이스
 * <p>mapper/FileMapper.xml에 정의된 쿼리를 실행하기 위한 인터페이스
 *
 * @author yoony
 * @version 1.0
 * @see FileDTO
 * @since 2023. 02. 17.
 */
public interface FileDAO {

  /**
   * 파일을 등록하는 메소드
   * @param fileDTO 파일 정보를 담은 DTO
   *                <p>fileId는 Auto Increment이므로 입력하지 않음
   *                <p>createdDate는 current_timestamp로 입력되므로 입력하지 않음
   * @return int 등록된 파일의 개수
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public int insertFile(FileDTO fileDTO);

  /**
   * 특정 게시글에 파일목록을 조회하는 메소드
   * @param articleId 파일을 조회할 대상 게시글 id
   * @return 조회된 파일 목록을 담은 {@link FileDTO} 리스트({@link List})
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public List<FileDTO> selectFileList(int articleId);

  /**
   * 특정 게시글에 등록된 파일 개수를 조회하는 메소드
   * @param articleId 파일 개수를 조회할 대상 게시글 id
   * @return int 조회된 파일 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public int selectFileCount(int articleId);

  /**
   * 특정 게시글에 등록된 특정 파일 데이터를 삭제하는 메소드
   * @param fileId 삭제할 파일 id
   * @param articleId 삭제할 파일이 속한 게시글 id
   * @return int 삭제된 파일의 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public int deleteFile(int fileId, int articleId);

  /**
   * 특정 게시글에 등록된 모든 파일 데이터를 삭제하는 메소드
   * @param articleId 삭제할 파일이 속한 게시글 id
   * @return int 삭제된 파일의 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public int deleteAllFile(int articleId);
}
