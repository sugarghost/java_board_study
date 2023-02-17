package com.board.servlet.yoony.category;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 카테고리 관련 DAO 인터페이스
 * <p>mapper/CategoryMapper.xml에 정의된 쿼리를 실행하기 위한 인터페이스
 *
 * @author yoony
 * @version 1.0
 * @see CategoryDTO
 * @since 2023. 02. 17.
 */
@Mapper
public interface CategoryDAO {

  /**
   * category List를 조회하는 메소드
   *
   * @return 조회된 카테고리 목록을 담은 {@link CategoryDTO} 리스트({@link List})
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 17.
   */
  public List<CategoryDTO> selectCategoryList();
  //public Map selectCategoryMap();
}
