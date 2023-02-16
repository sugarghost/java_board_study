package com.board.servlet.yoony.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 카테고리를 DTO
 * @author yoony
 * @since 2023. 02. 15.
 * @version 1.0
 * @see CategoryDAO
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CategoryDTO {

  // 카테고리 ID(auto increment)
  private int categoryId;
  // 카테고리 이름
  private String name;

}
