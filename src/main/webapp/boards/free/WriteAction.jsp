<%@ page import="com.board.jsp.yoony.article.ArticleDAO" %>
<%@ page import="com.board.jsp.yoony.article.ArticleDTO" %><%--
  Created by IntelliJ IDEA.
  User: YK
  Date: 2023-02-06
  Time: 오후 4:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java"%>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>게시판 - 등록</title>
    <script>
      function articleInsertSuccess() {
        alert("게시글 등록 성공");
        location.href = "List.jsp";
      }

      function articleInsertFailed() {
        alert("게시글 등록 실패");
        history.back();
      }
    </script>
</head>
<body>
<%
    ArticleDTO articleDTO = new ArticleDTO();
    ArticleDAO articleDAO = ArticleDAO.getInstance();
    request.setCharacterEncoding("utf-8");
    articleDTO.setTitle(request.getParameter("title"));
    articleDTO.setContent(request.getParameter("content"));
    articleDTO.setWriter(request.getParameter("writer"));
    articleDTO.setCategory(request.getParameter("category"));
    articleDTO.setPassword(request.getParameter("password"));

    int articleInsertResult = articleDAO.insertArticle(articleDTO);

    if (articleInsertResult == 1) {
        out.println("<script>articleInsertSuccess()</script>");
    } else {
        out.println("<script>articleInsertFailed()</script>");
    }
%>
</body>
</html>
