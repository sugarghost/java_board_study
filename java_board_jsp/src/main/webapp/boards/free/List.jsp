<%@ page import="com.board.jsp.yoony.article.ArticleDAO" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.board.jsp.yoony.article.ArticleDTO" %>
<%@ page import="com.board.jsp.yoony.category.CategoryDAO" %>
<%@ page import="com.board.jsp.yoony.category.CategoryDTO" %>
<%@ page import="com.board.jsp.yoony.article.ArticlePage" %>
<%@ page import="com.board.jsp.yoony.utill.ValidationChecker" %>
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
    <%@ include file="/common/Encode.jsp" %>
    <title>자유 게시판 - 등록</title>
    <%@ include file="/common/Bootstrap.jsp" %>
</head>
<body>
<%@ include file="/common/SearchKeeper.jsp" %>
<%
    // 유효성 체크를 위한 모듈
    ValidationChecker validationChecker = ValidationChecker.getInstance();

    // 검색조건 지정을 위한 category 가져옴
    CategoryDAO categoryDAO = CategoryDAO.getInstance();
    List<CategoryDTO> categoryList = categoryDAO.getCategoryList();
    pageContext.setAttribute("categoryList", categoryList);

    // 검색을 위한 param 설정
    Map<String, Object> param = new HashMap<String, Object>();
    String searchWord = request.getParameter("searchWord");
    String category = request.getParameter("category");
    pageContext.setAttribute("category", category);
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");

    param.put("searchWord", searchWord);
    param.put("category", category);
    param.put("startDate", request.getParameter("startDate"));
    param.put("endDate", request.getParameter("endDate"));

    // 페이지네이션 구현
    ArticleDAO articleDAO = ArticleDAO.getInstance();
    int totalCount = articleDAO.getArticleCount(param);
    int pageSize = 5;
    int blockPage = 10;

    int pageNum;
    String pageTemp = request.getParameter("pageNum");
    if (!validationChecker.CheckStringIsNullOrEmpty(pageTemp)) {
        pageNum = Integer.parseInt(pageTemp);
        //session.setAttribute("pageNum", pageNum);
    } else {
        pageNum = 1;
    }

    int rowStart = (pageNum - 1) * pageSize + 1;
    int rowEnd = rowStart + pageSize - 1;
    param.put("rowStart", rowStart);
    param.put("rowEnd", rowEnd);

    // 검색조건에 따른 게시글 가져옴
    List<ArticleDTO> articleList = articleDAO.getArticleList(param);


%>
<div class="container-fluid">
    <h1>자유 게시판 - 목록</h1>
    <form method="get">
        <div class="row border align-content-center p-2">
            <div class="col-1 align-self-center">
                등록일
            </div>
            <div class="col-2">
                <input type="date" class="form-control" name="startDate" value="<%= startDate%>">
            </div>
            <div class="col-1 align-self-center text-center">
                ~
            </div>
            <div class="col-2">
                <input type="date" class="form-control" name="endDate" value="<%= endDate%>">
            </div>
            <div class="col-2">
                <select class="form-control" name="category">
                    <option value="">카테고리</option>

                    <c:forEach items="${categoryList}" var="categoryDTO">
                        <option value="${categoryDTO.getName()}"
                                <c:if test="${categoryDTO.getName() eq category}">selected</c:if>>${categoryDTO.getName()}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-2">
                <input type="text" class="form-control" name="searchWord"
                       placeholder="검색어 입력(제목+작성자+내용)"
                       value="<%= (!validationChecker.CheckStringIsNullOrEmpty(searchWord)) ? searchWord : "" %>">
            </div>
            <div class="col-2">
                <input type="hidden" name="pageNum" value="1">
                <input type="submit" class="btn btn-primary" value="검색">
            </div>
        </div>
        <div class="row">
            <div class="col-12">
                총 <%=totalCount%>건
            </div>
        </div>
        <div class="row">
            <div class="col-12">

                <table class="table">
                    <thead>
                    <tr>
                        <td>카테고리</td>
                        <td>제목</td>
                        <td>작성자</td>
                        <td>조회수</td>
                        <td>등록 일시</td>
                        <td>수정 일시</td>
                    </tr>
                    </thead>
                    <%
                        if (articleList.isEmpty()) {
                    %>
                    <tbody>
                    <tr>
                        <td colspan="6">검색 결과가 없습니다.</td>
                    </tr>
                    </tbody>
                    <%
                    } else {
                        for (ArticleDTO articleDTO : articleList) {
                    %>
                    <tbody>
                    <tr>
                        <td><%= articleDTO.getCategory() %>
                        </td>
                        <td>
                            <a href="View.jsp?articleId=<%= articleDTO.getArticleId() %>${searchKeeperSearchParams}">
                                <%
                                    // TODO: 모든 처리 부분은 가급적이면 유틸로 처리하기
                                    // COMMENT: 일단 사용 부분이 이곳 하나, 여기서는 보류하고 Servlet 방식에서 새롭게 고민
                                    if (articleDTO.getTitle().length() > 80) {
                                        out.print(articleDTO.getTitle().substring(0, 80) + "...");
                                    } else {
                                        out.print(articleDTO.getTitle());
                                    }
                                %>
                            </a>
                            <span>
                                <%= (articleDTO.getFileExistFlag()) ? "\uD83D\uDCCE" : "" %>
                            </span>
                        </td>
                        <td><%= articleDTO.getWriter() %>
                        </td>
                        <td><%= articleDTO.getViewCount() %>
                        </td>
                        <td><%= (articleDTO.isCreatedDateValid()) ? articleDTO.getCreatedDate()
                                : "-" %>
                        </td>
                        <td><%= (articleDTO.isModifiedDateValid()) ? articleDTO.getModifiedDate()
                                : "-" %>
                        </td>
                    </tr>
                    </tbody>
                    <%
                            }
                        }
                    %>
                </table>
            </div>
        </div>
        <div class="row">
            <div class="col-12">
                <ul class="pagination d-flex justify-content-center"
                <%= ArticlePage.pagingStr(totalCount, pageSize, blockPage, pageNum,
                        request.getRequestURI(), param)%>
                </ul>
            </div>
        </div>


        <div class="row">
            <div class="col-12 d-flex justify-content-end">
                <input type="button" class="btn btn-primary" value="글쓰기"
                       onclick="location.href='Write.jsp?${searchKeeperSearchParams}'">
            </div>
        </div>
    </form>
</div>
</body>
</html>
