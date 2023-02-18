package com.board.servlet.yoony.article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 게시글 관련 DAO 인터페이스
 * <p>mapper/ArticleMapper.xml에 정의된 쿼리를 실행하기 위한 인터페이스
 *
 * @author yoony
 * @version 1.0
 * @see ArticleDTO
 * @since 2023. 02. 17.
 */
@Mapper
public interface ArticleDAO {

  /**
   * 게시글을 등록하는 메소드
   * <p>useGeneratedKeys="true"를 통해 insert 된 articleId가 매개변수 articleDTO에 자동으로 입력됨
   * <p>사용 전 articleDTO.isInsertArticleValid()를 통해 유효성 검사를 해야함
   *
   * @param articleDTO 게시글 정보를 담은 DTO articleId는 Auto Increment이므로 입력하지 않음
   *                   <p>createdDate는 current_timestamp로 입력되므로 입력하지 않음
   * @return int 등록된 게시글의 개수
   * @author yoony
   * @version 1.0
   * @see ArticleDTO#isInsertArticleValid()
   * @since 2023. 02. 17.
   */
  public int insertArticle(ArticleDTO articleDTO);

  /**
   * 게시글 개수를 조회하는 메소드
   *
   * @param params Map<String, Object> 형태로 담긴 검색 조건
   *               <p>key: searchWord, value: 검색 키워드로 title, content, writer 세가지 컬럼에 like 연산처리
   *               <p>key: categoryId, value: 카테고리 ID
   *               <p>key: startDate, value: 날자 검색 시작일로 endDate와 함께 쓰이며 작성일 기간 검색에 사용
   *               <p>key: endDate, value: 날자 검색 종료일로 startDate와 함께 쓰이며 작성일 기간 검색에 사용
   * @return int 조회된 게시글 개수
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public int selectArticleCount(Map<String, Object> params);

  /**
   * 게시글 목록을 조회하는 메소드
   *
   * @param params Map<String, Object> 형태로 담긴 검색 조건
   *               <p>key: searchWord, value: 검색 키워드로 title, content, writer 세가지 컬럼에 like 연산처리
   *               <p>key: categoryId, value: 카테고리 ID
   *               <p>key: startDate, value: 날자 검색 시작일로 endDate와 함께 쓰이며 작성일 기간 검색에 사용
   *               <p>key: endDate, value: 날자 검색 종료일로 startDate와 함께 쓰이며 작성일 기간 검색에 사용
   * @return List ArticleDTO 리스트
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public List<ArticleDTO> selectArticleList(Map<String, Object> params);

  /**
   * 게시글을 조회해 DTO로 가져오는 메소드
   *
   * @param articleId 게시글 ID
   * @return {@link ArticleDTO} 조회된 게시글 정보를 담은 DTO
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public ArticleDTO selectArticle(int articleId);

  /**
   * 특정 게시글에 대해서 입력한 패스워드가 일치하는지 확인
   *
   * @param articleDTO 검사 대상 articleId, password
   *                   <p>게시글 ID
   *                   <p>게시글 비밀번호 (SHA-256로 암호화된 값)
   * @return boolean 게시글 비밀번호가 입력된 비밀번호와 일치하면 true, 일치하지 않으면 false
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public boolean selectPasswordCheck(ArticleDTO articleDTO);

  /**
   * 게시글을 수정하는 메소드
   * <p>modeifiedDate는 current_timestamp로 입력되므로 입력하지 않음
   * <p>사용 전 {@link ArticleDTO#isUpdateArticleValid()}를 통해 유효성 검사를 해야함
   *
   * @param articleDTO 게시글 DTO
   *                   <p>writer, title, content가 update set 대상
   *                   <p>articleId, password를 기준으로 update
   *                   <p>password는 SHA-256으로 암호화된 값
   * @return the int
   */
  public int updateArticle(ArticleDTO articleDTO);

  /**
   * 게시글의 조회수를 1 증가시키는 메소드
   *
   * @param articleId 게시글 id
   * @return int update된 행의 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public int updateArticleViewCount(int articleId);

  /**
   * 게시글을 삭제하는 메소드
   * <p>사용 전 {@link ArticleDAO#selectPasswordCheck(ArticleDTO)}를 통해 비밀번호가 일치하는지 확인을 권장
   * <p>쿼리에서도 비밀번호를 체크하지만, 보안을 위해 2차 검증
   *
   * @param articleDTO 삭제 대상 articleId, password
   *                   <p>게시글 ID
   *                   <p>게시글 비밀번호 (SHA-256로 암호화된 값)
   * @return int 삭제된 행의 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public int deleteArticle(ArticleDTO articleDTO);
}
