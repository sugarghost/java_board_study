<%@ page import="com.board.jsp.yoony.article.ArticleDAO" %>
<%@ page import="com.board.jsp.yoony.comment.CommentDAO" %>
<%@ page import="com.board.jsp.yoony.comment.CommentDTO" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2023-02-12
  Time: 오전 12:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <%@ include file="/common/Encode.jsp" %>
    <title>게시판 - 댓글</title>
    <!--
    articleInsertSuccess() 등의 사후 처리 function을 사용해봤지만, JSP, HTML 실행 순서로 오류남
    JAVA단에서 sendRedirect 사용하는 방향으로 변경
    -->
</head>
<body>
<%@ include file="/common/SearchKeeper.jsp" %>
<%
    ArticleDAO articleDAO = ArticleDAO.getInstance();
    int articleId = Integer.parseInt(request.getParameter("articleId"));
    String content = request.getParameter("content");

    CommentDAO commentDAO = CommentDAO.getInstance();
    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setArticleId(articleId);
    commentDTO.setContent(content);
    int result = commentDAO.insertComment(commentDTO);
    if (result != 0) {
        response.sendRedirect("View.jsp?articleId=" + articleId + searchKeeperSearchParams);
    } else {
        response.sendRedirect(request.getHeader("referer") + "&commentError=1");
    }
%>
</body>
</html>
