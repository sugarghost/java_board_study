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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>자유 게시판 - 등록</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
          integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N"
          crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"
            integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
            integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js"
            integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+"
            crossorigin="anonymous"></script>
</head>
<body>
<%
    // 검색조건 지정을 위한 category 가져옴
    CategoryDAO categoryDAO = CategoryDAO.getInstance();
    List<CategoryDTO> categoryList = categoryDAO.getCategoryList();
    pageContext.setAttribute("categoryList", categoryList);

    // 검색을 위한 param 설정
    Map<String, Object> param = new HashMap<String, Object>();
    // 인코딩같은 메터적인 것들은 따로 상위에 빼서 사용하는걸 추천함
    request.setCharacterEncoding("utf-8");
    String searchWord = request.getParameter("searchWord");
    String category = request.getParameter("category");
    pageContext.setAttribute("category", category);
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");

    // 공통적인 NUll 체크 기능은 Util로 따로 뺴서 사용하는게 좋음
    // equals 체크시 변수를 먼저 체크하기보단 ""을 기준을 Equals 체크하는게 좋음
    if (searchWord != null && !searchWord.equals("")) {
        // 세션은 동일한 브라우저에서 페이지를 새로 켜도 조건이 계속 물려져서 감
        // 검색 조건은 session을 사용하면 안됨
        // 검색 조건은 request 컨텍스트에서 넘어가면 안됨
        session.setAttribute("searchWord", searchWord);
    } else {
        searchWord = (String) session.getAttribute("searchWord");
    }
    param.put("searchWord", searchWord);

    if (category != null && !category.equals("")) {
        session.setAttribute("category", category);
    } else {
        category = (String) session.getAttribute("category");
    }
    param.put("category", category);

    if (startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("")) {
        session.setAttribute("startDate", request.getParameter("startDate"));
        session.setAttribute("endDate", request.getParameter("endDate"));
    } else {
        startDate = (String) session.getAttribute("startDate");
        endDate = (String) session.getAttribute("endDate");
    }
    param.put("startDate", request.getParameter("startDate"));
    param.put("endDate", request.getParameter("endDate"));

    // 페이지네이션 구현
    ArticleDAO articleDAO = ArticleDAO.getInstance();
    int totalCount = articleDAO.getArticleCount(param);
    int pageSize = 5;
    int blockPage = 10;

    int pageNum;
    String pageTemp = request.getParameter("pageNum");
    if (pageTemp != null && !pageTemp.equals("")) {
        pageNum = Integer.parseInt(pageTemp);
        session.setAttribute("pageNum", pageNum);
    } else {
        pageNum = (session.getAttribute("pageNum") == null) ? 1
                : (int) session.getAttribute("pageNum");
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
                       value="<%= (searchWord != null && !searchWord.equals("")) ? searchWord : "" %>">
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
                            <a href="View.jsp?articleId=<%= articleDTO.getArticleId() %>">
                                <%
                                    // 모든 처리 부분은 가급적이면 유틸로 처리하기
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
                       onclick="location.href='Write.jsp'">
            </div>
        </div>
    </form>
</div>
</body>
</html>
