<%@ page import="com.board.jsp.yoony.article.ArticleDAO" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.board.jsp.yoony.article.ArticleDTO" %>
<%@ page import="com.board.jsp.yoony.category.CategoryDAO" %>
<%@ page import="com.board.jsp.yoony.category.CategoryDTO" %>
<%@ page import="com.board.jsp.yoony.article.ArticlePage" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: YK
  Date: 2023-02-06
  Time: 오후 4:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>자유 게시판 - 등록</title>
</head>
<body>
<%
    // 검색조건 지정을 위한 category 가져옴
    CategoryDAO categoryDAO = CategoryDAO.getInstance();
    List<CategoryDTO> categoryList = categoryDAO.getCategoryList();
    pageContext.setAttribute("categoryList", categoryList);

    // 검색을 위한 param 설정
    Map<String, Object> param = new HashMap<String, Object>();
    request.setCharacterEncoding("utf-8");
    String searchWord = request.getParameter("searchWord");
    String category = request.getParameter("category");
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");

    if (searchWord != null && !searchWord.equals("")) {
        param.put("searchWord", searchWord);

    }
    if (category != null && !category.equals("")) {
        param.put("category", category);
    }
    if (startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("")) {
        param.put("startDate", request.getParameter("startDate"));
        param.put("endDate", request.getParameter("endDate"));
    }

    // 페이지네이션 구현
    ArticleDAO articleDAO = ArticleDAO.getInstance();
    int totalCount = articleDAO.getArticleCount(param);
    int pageSize = 5;
    int blockPage = 10;
    int totalPage = (int)Math.ceil((double)totalCount/pageSize);

    int pageNum = 1;
    String pageTemp = request.getParameter("pageNum");
    if (pageTemp != null && !pageTemp.equals("")) {
        pageNum = Integer.parseInt(pageTemp);
    }

    int rowStart = (pageNum - 1) * pageSize + 1;
    int rowEnd = rowStart + pageSize - 1;
    param.put("rowStart", rowStart);
    param.put("rowEnd", rowEnd);

    // 검색조건에 따른 게시글 가져옴
    List<ArticleDTO> articleList = articleDAO.getArticleList(param);


%>
<h1>자유 게시판 - 목록</h1>
<form method="get">
    <table>
        <tr>
            <td>
                <input type="date" name="startDate">
                <input type="date" name="endDate">
            </td>
            <td>
                <select name="category">
                    <option value="">카테고리</option>

                    <c:forEach items="${categoryList}" var="categoryDTO">
                        <option value="${categoryDTO.getName()}">${categoryDTO.getName()}</option>
                    </c:forEach>
                </select>
            </td>
            <td>
                <input type="text" name="searchWord">
            </td>
            <td>
                <input type="submit" value="검색">
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td>카테고리</td>
            <td>제목</td>
            <td>작성자</td>
            <td>조회수</td>
            <td>등록 일시</td>
            <td>수정 일시</td>
        </tr>
        <%
            if (articleList.isEmpty()) {
        %>
        <tr>
            <td colspan="6">검색 결과가 없습니다.</td>
        </tr>
        <%
        } else {
            for (ArticleDTO articleDTO : articleList) {
        %>
        <tr>
            <td><%= articleDTO.getCategory() %>
            </td>
            <td>
                <a href="View.jsp?articleNo=<%= articleDTO.getArticleId() %>"><%= articleDTO.getTitle() %>
                </a></td>
            <td><%= articleDTO.getWriter() %>
            </td>
            <td><%= articleDTO.getViewCount() %>
            </td>
            <td><%= articleDTO.getCreatedDate() %>
            </td>
            <td><%= articleDTO.getModifiedDate() %>
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>

    <table>
        <tr>
            <td>
                <%= ArticlePage.pagingStr(totalCount,pageSize, blockPage,pageNum,request.getRequestURI(),param)%>
            </td>
            <td>
                <a href="Write.jsp">글쓰기</a>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
