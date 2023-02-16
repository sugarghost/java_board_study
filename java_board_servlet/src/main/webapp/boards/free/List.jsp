<%@ page import="com.board.servlet.yoony.util.ValidationChecker" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: YK
  Date: 2023-02-14
  Time: 오후 4:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/common/Encode.jsp"></jsp:include>
    <title>자유게시판 - 목록</title>
    <jsp:include page="/common/Bootstrap.jsp"></jsp:include>
</head>
<body>
<div class="container-fluid">
    <h1>자유게시판 - 목록</h1>
    <form method="get">
        <div class="row border align-content-center p-2">
            <div class="col-1 align-self-center">
                등록일
            </div>
            <div class="col-2">
                <input type="date" class="form-control" name="startDate" value="${startDate}">
            </div>
            <div class="col-1 align-self-center text-center">
                ~
            </div>
            <div class="col-2">
                <input type="date" class="form-control" name="endDate" value="${endDate}">
            </div>
            <div class="col-2">
                <select class="form-control" name="categoryId">
                    <option value="">카테고리</option>

                    <c:forEach items="${categoryMap}" var="category">
                        <option value="${category.key}"
                                <c:if test="${category.key eq categoryId}">selected</c:if>>${category.value}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-2">
                <input type="text" class="form-control" name="searchWord"
                       placeholder="검색어 입력(제목+작성자+내용)"
                       value="${(!(searchWord.isEmpty())) ? searchWord : "" }">
            </div>
            <div class="col-2">
                <input type="hidden" name="pageNum" value="1">
                <input type="submit" class="btn btn-primary" value="검색">
            </div>
        </div>
        <div class="row">
            <div class="col-12">
                총 ${pageDTO.getTotalCount()}건
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

                    <c:if test="${articleList.isEmpty()}">
                        <tbody>
                        <tr>
                            <td colspan="6">검색 결과가 없습니다.</td>
                        </tr>
                        </tbody>
                    </c:if>
                    <c:forEach items="${articleList}" var="articleDTO">
                        <tbody>
                        <tr>
                            <td> ${categoryMap[articleDTO.getCategoryId()]}
                            </td>
                            <td>
                                <a href="View.jsp?articleId=${articleDTO.getArticleId()}${searchManager.getSearchParamsQuery()}">
                                    <%
                                        // TODO: subString 80자 공통 모듈화 해서 불러오기
                                    %>
                                        ${articleDTO.title}
                                </a>
                                <span>
                                <%
                                    // TODO: File 여부 확인해서 파일이 있으면 파일 아이콘 띄우기
                                %>
                            </span>
                            </td>
                            <td>${articleDTO.getWriter()}
                            </td>
                            <td>${articleDTO.getViewCount()}
                            </td>
                            <td>${(articleDTO.isCreatedDateValid()) ? articleDTO.getCreatedDate()
                                    : "-" }
                            </td>
                            <td>${(articleDTO.isModifiedDateValid()) ? articleDTO.getModifiedDate()
                                    : "-"}
                            </td>
                        </tr>
                        </tbody>
                    </c:forEach>
                </table>
            </div>
        </div>
        <jsp:include page="/common/Paging.jsp">
            <jsp:param value="${request.getRequestURI()}" name="requestUrl"/>
            <jsp:param value="${pageDTO}" name="pageDTO"/>
            <jsp:param value="${searchManager}" name="searchManager"/>
        </jsp:include>


        <div class="row">
            <div class="col-12 d-flex justify-content-end">
                <input type="button" class="btn btn-primary" value="글쓰기"
                       onclick="location.href='Write.jsp?${searchManager.getSearchParamsQuery()}'">
            </div>
        </div>
    </form>
</div>
</body>
</html>
