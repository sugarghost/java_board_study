package com.board.servlet.yoony.file;

import java.util.List;
import java.util.Map;

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
   *
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
   * <p> 사용자에게 저장된 파일의 이름이나 경로를 보여주지 않기 위해 fileSaveName, filePath는 제외됨
   *
   * @param articleId 파일을 조회할 대상 게시글 id
   * @return 조회된 파일 목록을 담은 {@link FileDTO} 리스트({@link List})
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public List<FileDTO> selectFileList(int articleId);

  /**
   * 특정 게시글의 특정 파일을 조회하는 메소드
   *
   * @param params 조회할 파일의 id와 게시글 id를 담은 {@link Map}
   *               <p>key: articleId, value: 게시글 id
   *               <p>key: fileId, value: 파일 id
   * @return 조회된 파일 정보를 담은 {@link FileDTO}
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 18.
   */
  public FileDTO selectFile(Map<String, Integer> params);

  /**
   * 특정 게시글에 등록된 파일 개수를 조회하는 메소드
   *
   * @param articleId 파일 개수를 조회할 대상 게시글 id
   * @return int 조회된 파일 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public int selectFileCount(int articleId);

  /**
   * 특정 게시글에 등록된 파일 여부를 조회하는 메소드
   * <p>article List 조회시 Count 방식이 성능이 안좋아서 만들었지만, List 조회시 자체 SubQuery를 사용함
   * <p>사실상 사용은 안하지만 혹시 모르니 보관
   *
   * @param articleId 파일 여부를 조회할 대상 게시글 id
   * @return boolean 파일 여부
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public boolean selectFileExist(int articleId);

  /**
   * 특정 게시글에 등록된 특정 파일 데이터를 삭제하는 메소드
   *
   * @param fileId    삭제할 파일 id
   * @param articleId 삭제할 파일이 속한 게시글 id
   * @return int 삭제된 파일의 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public int deleteFile(int fileId, int articleId);

  /**
   * 특정 게시글에 등록된 모든 파일 데이터를 삭제하는 메소드
   *
   * @param articleId 삭제할 파일이 속한 게시글 id
   * @return int 삭제된 파일의 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public int deleteAllFile(int articleId);
}
