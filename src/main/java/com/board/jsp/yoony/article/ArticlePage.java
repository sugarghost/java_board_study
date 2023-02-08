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
      pagingStr += "<a href='" + requestUrl + "?pageNum=" + 1 + params + "'><<</a>";
      pagingStr += "&nbsp;&nbsp;";
      pagingStr += "<a href='" + requestUrl + "?pageNum=" + (pageTemp - 1) + params + "'><</a>";
    }

    int blockCount = 1;
    while (blockCount <= blockPage && pageTemp <= totalPage) {
      if (pageTemp == pageNum) {
        pagingStr += "&nbsp;<span style='color:red; font-weight:bold;'>" + pageTemp + "</span>&nbsp;";
      } else {
        pagingStr += "&nbsp;<a href='" + requestUrl + "?pageNum=" + pageTemp + params + "'>" + pageTemp
            + "</a>&nbsp;";
      }
      pageTemp++;
      blockCount++;
    }
    if(pageTemp <= totalPage) {
      pagingStr += "<a href='" + requestUrl + "?pageNum=" + pageTemp + params + "'>></a>";
      pagingStr += "&nbsp;&nbsp;";
      pagingStr += "<a href='" + requestUrl + "?pageNum=" + totalPage + params + "'>>></a>";
    }
    return pagingStr;
  }

}
