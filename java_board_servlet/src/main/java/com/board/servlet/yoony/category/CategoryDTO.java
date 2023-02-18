package com.board.servlet.yoony.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 카테고리 DTO
 *
 * @author yoony
 * @version 1.0
 * @see CategoryDAO
 * @since 2023. 02. 15.
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
