package com.board.jsp.yoony.article;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArticlePage {

  private static Logger logger = LogManager.getLogger(ArticlePage.class);
  public static String pagingStr(int totalCount, int pageSize, int blockPage, int pageNum,
      String requestUrl, Map<String, Object> paramMap) {
    logger.debug("pagingStr() : " + totalCount + ", " + pageSize + ", " + blockPage + ", " + pageNum
        + ", " + requestUrl + ", " + paramMap
    );

    int totalPage = (int) Math.ceil((double) totalCount / pageSize);
    String params = "";

    if (paramMap.get("searchWord") != null) {
      params += "&searchWord=" + paramMap.get("searchWord");
    }

    if (paramMap.get("category") != null) {
      params += "&category=" + paramMap.get("category");
    }
    if (paramMap.get("startDate") != null && paramMap.get("endDate") != null) {
      params += "&startDate=" + paramMap.get("startDate") + "&endDate=" + paramMap.get("endDate");
    }

    int pageTemp = (((pageNum - 1) / blockPage) * blockPage) + 1;
    String pagingStr = "";
    if(pageTemp != 1) {
      pagingStr += "<li class='page-item'>";
      pagingStr += "<a class='page-link' href='" + requestUrl + "?pageNum=" + 1 + params + "'><<</a>";
      pagingStr += "</li>";

      pagingStr += "<li class='page-item'>";
      pagingStr += "<a class='page-link' href='" + requestUrl + "?pageNum=" + (pageTemp - 1) + params + "'><</a>";
      pagingStr += "</li>";
    }

    int blockCount = 1;
    while (blockCount <= blockPage && pageTemp <= totalPage) {
      if (pageTemp == pageNum) {
        pagingStr += "<li class='page-item active'>";
        pagingStr += "<span class='page-link'>" + pageTemp + "</span>";
        pagingStr += "</li>";
      } else {
        pagingStr += "<li class='page-item'>";
        pagingStr += "<a class='page-link' href='" + requestUrl + "?pageNum=" + pageTemp + params + "'>" + pageTemp
            + "</a>";
        pagingStr += "</li>";
      }
      pageTemp++;
      blockCount++;
    }
    if(pageTemp <= totalPage) {
      pagingStr += "<li class='page-item'>";
      pagingStr += "<a class='page-link' href='" + requestUrl + "?pageNum=" + pageTemp + params + "'>></a>";
      pagingStr += "</li>";

      pagingStr += "<li class='page-item'>";
      pagingStr += "<a class='page-link' href='" + requestUrl + "?pageNum=" + totalPage + params + "'>>></a>";
      pagingStr += "</li>";
    }
    pagingStr += "";
    return pagingStr;
  }

}
